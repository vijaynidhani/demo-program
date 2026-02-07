package com.example.hellospringboot.payment;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/payments")
public class ReportController {

    private final PaymentService paymentService;

    public ReportController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping(value = "/report", produces = MediaType.TEXT_HTML_VALUE)
    public String report() {
        List<PaymentRecord> records = paymentService.getAllRecords();
        StringBuilder sb = new StringBuilder();
        sb.append("<html><head><title>Payments Report</title></head><body>");
        sb.append("<h1>Payments Report</h1>");
        sb.append("<table border=1 style=width:100%><thead><tr><th>ID</th><th>Timestamp</th><th>Idempotency-Key</th><th>Amount</th><th>To</th><th>From</th><th>Name</th></tr></thead><tbody>");
        DateTimeFormatter fmt = DateTimeFormatter.ISO_INSTANT;
        for (PaymentRecord r : records) {
            sb.append("<tr>");
            sb.append("<td>").append(r.getId()).append("</td>");
            sb.append("<td>").append(fmt.format(r.getTimestamp())).append("</td>");
            sb.append("<td>").append(r.getIdempotencyKey() == null ? "" : r.getIdempotencyKey()).append("</td>");
            sb.append("<td>").append(r.getPayment().getAmount()).append("</td>");
            sb.append("<td>").append(r.getPayment().getToAccount()).append("</td>");
            sb.append("<td>").append(r.getPayment().getFromAccount()).append("</td>");
            sb.append("<td>").append(r.getPayment().getName()).append("</td>");
            sb.append("</tr>");
        }
        sb.append("</tbody></table></body></html>");
        return sb.toString();
    }
}
