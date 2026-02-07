package com.example.hellospringboot.payment;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class PaymentService {

    private final List<PaymentAdapter> adapters;
    // idempotency store: key -> result
    private final Map<String, PaymentDTO> idempotencyStore = new ConcurrentHashMap<>();
    // list of processed payments (temporary in-memory store)
    private final List<PaymentRecord> records = new CopyOnWriteArrayList<>();

    public PaymentService(List<PaymentAdapter> adapters) {
        this.adapters = adapters;
    }

    public PaymentDTO process(PaymentRequest req, String idempotencyKey) {
        if (idempotencyKey != null && idempotencyStore.containsKey(idempotencyKey)) {
            return idempotencyStore.get(idempotencyKey);
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
            idempotencyStore.put(idempotencyKey, result);
        }
        // store record for reporting
        records.add(new PaymentRecord(idempotencyKey, result));
        return result;
    }

    public List<PaymentRecord> getAllRecords() {
        return records;
    }
}
