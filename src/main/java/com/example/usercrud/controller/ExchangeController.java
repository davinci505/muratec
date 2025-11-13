package com.example.usercrud.controller;

import com.example.usercrud.service.ExchangeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/exchange")
public class ExchangeController {

    private final ExchangeService exchangeService;

    public ExchangeController(ExchangeService exchangeService) {
        this.exchangeService = exchangeService;
    }

    @GetMapping
    public ResponseEntity<?> getExchange() {
        try {
            String raw = exchangeService.fetchExchangeRaw();
            if (raw == null) {
                return ResponseEntity.status(502).body("Empty response from upstream");
            }

            // Find numeric values in the raw response, prefer decimals
            Pattern p = Pattern.compile("(\\d{1,3}(?:,\\d{3})*(?:\\.\\d+)?)");
            Matcher m = p.matcher(raw);
            BigDecimal best = null;
            while (m.find()) {
                String s = m.group(1).replace(",", "");
                try {
                    BigDecimal v = new BigDecimal(s);
                    if (best == null || v.compareTo(best) > 0) {
                        best = v;
                    }
                } catch (Exception ignored) {
                }
            }

            if (best == null) {
                // fallback: return raw
                return ResponseEntity.ok().body("원문: " + raw);
            }

            // Format to 2 decimal places
            DecimalFormat df = new DecimalFormat("#,##0.00");
            String formatted = df.format(best);

            // The request URL requests 100 JPY -> KRW, so show that form
            String text = "100 엔 = " + formatted + " 원";
            return ResponseEntity.ok().body(text);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching exchange: " + e.getMessage());
        }
    }
}
