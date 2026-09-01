package com.example.usercrud.controller;

import com.example.usercrud.model.Formula;
import com.example.usercrud.service.FormulaService;
import com.example.usercrud.vo.FormulaRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/formulas")
public class FormulaRestController {

    @Autowired
    private FormulaService formulaService;

    /**
     * Tabulator용 목록 조회. items까지 한 번에 내려보냅니다.
     */
    @GetMapping
    public Map<String, Object> list() {
        List<Formula> formulas = formulaService.getAllWithItems();
        return Map.of(
                "data", formulas.stream().map(this::toMap).collect(Collectors.toList()),
                "last_page", 1
        );
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<Map<String, Object>> getOne(@PathVariable Long id) {
        return formulaService.getById(id)
                .map(this::toMap)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody FormulaRequest payload) {
        try {
            Formula saved = formulaService.save(payload);
            return ResponseEntity.ok(toMap(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id:\\d+}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody FormulaRequest payload) {
        try {
            Formula saved = formulaService.update(id, payload);
            return ResponseEntity.ok(toMap(saved));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        formulaService.delete(id);
        return ResponseEntity.ok(Map.of("deleted", id));
    }

    /**
     * 계산 API: GET /api/formulas/{code}/calculate?value=1000
     * code 대신 {id} 사용도 허용: /api/formulas/by-id/{id}/calculate?value=1000
     */
    @GetMapping("/{code:[A-Za-z0-9_\\-]+}/calculate")
    public ResponseEntity<?> calculate(@PathVariable String code,
                                       @RequestParam("value") BigDecimal value) {
        try {
            BigDecimal result = formulaService.calculate(code, value);
            Map<String, Object> body = new HashMap<>();
            body.put("code", code);
            body.put("sourceValue", value);
            body.put("result", result);
            return ResponseEntity.ok(body);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/by-id/{id:\\d+}/calculate")
    public ResponseEntity<?> calculateById(@PathVariable Long id,
                                           @RequestParam("value") BigDecimal value) {
        try {
            BigDecimal result = formulaService.calculateById(id, value);
            Map<String, Object> body = new HashMap<>();
            body.put("id", id);
            body.put("sourceValue", value);
            body.put("result", result);
            return ResponseEntity.ok(body);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private Map<String, Object> toMap(Formula formula) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", formula.getId());
        map.put("code", formula.getCode());
        map.put("description", formula.getDescription());
        List<Map<String, Object>> items = formula.getItems() == null
                ? List.of()
                : formula.getItems().stream().map(item -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", item.getId());
                    m.put("itemKey", item.getItemKey());
                    m.put("itemName", item.getItemName());
                    m.put("multiplier", item.getMultiplier());
                    m.put("sortOrder", item.getSortOrder());
                    return m;
                }).collect(Collectors.toList());
        map.put("items", items);
        return map;
    }
}
