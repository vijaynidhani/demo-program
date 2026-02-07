package com.example.hellospringboot.payment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.Optional;

@Service
public class PaymentService {

    private final List<PaymentAdapter> adapters;
    // idempotency store fallback (in-memory) - primary check is DB
    private final Map<String, PaymentDTO> idempotencyStore = new ConcurrentHashMap<>();

    private final PaymentRecordRepository repository;

    public PaymentService(List<PaymentAdapter> adapters, PaymentRecordRepository repository) {
        this.adapters = adapters;
        this.repository = repository;
    }

    public PaymentDTO process(PaymentRequest req, String idempotencyKey) {
        if (idempotencyKey != null) {
            Optional<PaymentRecord> existing = repository.findByIdempotencyKey(idempotencyKey);
            if (existing.isPresent()) {
                PaymentRecord r = existing.get();
                return new PaymentDTO(r.getAmount(), r.getToAccount(), r.getFromAccount(), r.getName());
            }
            if (idempotencyStore.containsKey(idempotencyKey)) {
                return idempotencyStore.get(idempotencyKey);
            }
        }

        PaymentAdapter chosen = null;
        for (PaymentAdapter a : adapters) {
            if (a.supports(req)) {
                chosen = a;
                break;
            }
        }
        if (chosen == null) {
            throw new IllegalArgumentException("No payment adapter found for request");
        }

        PaymentDTO result = chosen.process(req);

        if (idempotencyKey != null) {
            // persist record
            PaymentRecord record = new PaymentRecord(idempotencyKey, result);
            repository.save(record);
            idempotencyStore.put(idempotencyKey, result);
        } else {
            // still persist without idempotency key for reporting
            PaymentRecord rec = new PaymentRecord(null, result);
            repository.save(rec);
        }

        return result;
    }

    public List<PaymentRecord> getAllRecords() {
        return repository.findAll();
    }
}
