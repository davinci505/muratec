package com.example.usercrud.service;

import com.example.usercrud.model.Formula;
import com.example.usercrud.model.FormulaItem;
import com.example.usercrud.repository.FormulaItemRepository;
import com.example.usercrud.repository.FormulaRepository;
import com.example.usercrud.vo.FormulaRequest;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FormulaService {

    @Autowired
    private FormulaRepository formulaRepository;

    @Autowired
    private FormulaItemRepository formulaItemRepository;

    public List<Formula> getAll() {
        return formulaRepository.findAllByOrderByCodeAsc();
    }

    public List<Formula> getAllWithItems() {
        return formulaRepository.findAllWithItems();
    }

    @SuppressWarnings("null")
    public Optional<Formula> getById(Long id) {
        return formulaRepository.findById(id);
    }

    public Optional<Formula> getByCode(String code) {
        return formulaRepository.findByCodeWithItems(code);
    }

    public boolean existsByCode(String code) {
        return formulaRepository.existsByCode(code);
    }

    @Transactional
    public Formula save(FormulaRequest req) {
        if (req.getCode() != null && existsByCode(req.getCode())) {
            throw new IllegalArgumentException("이미 존재하는 계산법 코드입니다: " + req.getCode());
        }
        Formula formula = new Formula();
        formula.setCode(req.getCode());
        formula.setDescription(req.getDescription());
        applyItems(formula, req.getItems());
        return formulaRepository.save(formula);
    }

    @Transactional
    public Formula update(Long id, FormulaRequest req) {
        Formula formula = formulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Formula not found with id: " + id));

        if (req.getCode() != null && !req.getCode().equals(formula.getCode())) {
            if (existsByCode(req.getCode())) {
                throw new IllegalArgumentException("이미 존재하는 계산법 코드입니다: " + req.getCode());
            }
            formula.setCode(req.getCode());
        }

        formula.setDescription(req.getDescription());

        // 기존 items 물리적 삭제 (UNIQUE 제약 충돌 방지)
        formulaItemRepository.deleteByFormulaId(id);
        // 영속성 컨텍스트 초기화로 삭제된 엔티티가 items에 남지 않도록
        formula.getItems().clear();

        // 새 items 추가
        applyItems(formula, req.getItems());

        return formulaRepository.save(formula);
    }

    private void applyItems(Formula formula, List<FormulaRequest.Item> items) {
        if (items == null) {
            return;
        }
        int order = 0;
        for (FormulaRequest.Item src : items) {
            if (src.getMultiplier() == null) {
                continue;
            }
            String key = (src.getItemKey() == null || src.getItemKey().isBlank())
                    ? "ITEM_" + (order + 1)
                    : src.getItemKey().trim();
            Integer sortOrder = src.getSortOrder() == null ? order : src.getSortOrder();
            FormulaItem item = new FormulaItem();
            item.setItemKey(key);
            item.setItemName(src.getItemName());
            item.setMultiplier(src.getMultiplier());
            item.setSortOrder(sortOrder);
            formula.addItem(item);
            order++;
        }
    }

    @Transactional
    public void delete(@NonNull Long id) {
        formulaRepository.deleteById(id);
    }

    /**
     * 코드로 계산법을 찾아 곱셈을 수행합니다.
     * @param code 계산법 코드
     * @param sourceValue 원본 값
     * @return 곱셈 결과 (계산법 또는 sourceValue가 null이면 null)
     */
    public BigDecimal calculate(String code, BigDecimal sourceValue) {
        if (code == null || sourceValue == null) {
            return null;
        }
        return formulaRepository.findByCodeWithItems(code)
                .map(f -> f.calculate(sourceValue))
                .orElseThrow(() -> new RuntimeException("Formula not found: " + code));
    }

    /**
     * ID로 계산법을 찾아 곱셈을 수행합니다.
     */
    public BigDecimal calculateById(Long id, BigDecimal sourceValue) {
        if (id == null || sourceValue == null) {
            return null;
        }
        return formulaRepository.findById(id)
                .map(f -> f.calculate(sourceValue))
                .orElseThrow(() -> new RuntimeException("Formula not found: " + id));
    }
}
