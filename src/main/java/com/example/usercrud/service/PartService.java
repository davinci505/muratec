package com.example.usercrud.service;

import com.example.usercrud.model.Part;
import com.example.usercrud.repository.PartRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PartService {

    @Autowired
    private PartRepository repository;

    public List<Part> getAll() {
        return repository.findAll();
    }

    public Optional<Part> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Part> getByPartNumber(String partNumber) {
        return repository.findByPartNumber(partNumber);
    }

    public List<Part> getByFilters(String partNumber, String description) {
        boolean hasPartNumber = partNumber != null && StringUtils.hasText(partNumber);
        boolean hasDescription = description != null && StringUtils.hasText(description);

        String pn = hasPartNumber ? partNumber.trim() : null;
        String desc = hasDescription ? description.trim() : null;

        if (hasPartNumber && hasDescription) {
            return repository.findByPartNumberContainingIgnoreCaseAndDescriptionContainingIgnoreCase(pn, desc);
        }
        if (hasPartNumber) {
            return repository.findByPartNumberContainingIgnoreCase(pn);
        }
        if (hasDescription) {
            return repository.findByDescriptionContainingIgnoreCase(desc);
        }
        return repository.findAll();
    }

    // Pagination support
    public Page<Part> getByFilters(String partNumber, String description, Pageable pageable) {
        boolean hasPartNumber = partNumber != null && StringUtils.hasText(partNumber);
        boolean hasDescription = description != null && StringUtils.hasText(description);

        String pn = hasPartNumber ? partNumber.trim() : null;
        String desc = hasDescription ? description.trim() : null;

        if (hasPartNumber && hasDescription) {
            return repository.findByPartNumberContainingIgnoreCaseAndDescriptionContainingIgnoreCase(pn, desc, pageable);
        }
        if (hasPartNumber) {
            return repository.findByPartNumberContainingIgnoreCase(pn, pageable);
        }
        if (hasDescription) {
            return repository.findByDescriptionContainingIgnoreCase(desc, pageable);
        }
        return repository.findAll(pageable);
    }

    public Part save(Part entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Part update(Long id, Part details) {
        Part entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + id));

        entity.setPartNumber(details.getPartNumber());
        entity.setDescription(details.getDescription());
        entity.setSpec(details.getSpec());
        entity.setPriceJpy(details.getPriceJpy());

        return repository.save(entity);
    }

    public int bulkImport(MultipartFile file) {
        int saved = 0;
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue; // Skip header row

                Part part = new Part();
                part.setPartNumber(getCellValueAsString(row.getCell(0)));
                part.setDescription(getCellValueAsString(row.getCell(1)));
                part.setSpec(getCellValueAsString(row.getCell(2)));
                part.setPriceJpy(getCellValueAsBigDecimal(row.getCell(3)));

                if (part.getPartNumber() != null && !part.getPartNumber().trim().isEmpty()) {
                    // Check if part number already exists
                    Optional<Part> existing = repository.findByPartNumber(part.getPartNumber());
                    if (existing.isPresent()) {
                        // Update existing
                        Part existingPart = existing.get();
                        existingPart.setDescription(part.getDescription());
                        existingPart.setSpec(part.getSpec());
                        existingPart.setPriceJpy(part.getPriceJpy());
                        repository.save(existingPart);
                    } else {
                        // Create new
                        repository.save(part);
                    }
                    saved++;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Excel import failed: " + e.getMessage(), e);
        }
        return saved;
    }

    private String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                }
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null;
        }
    }

    private BigDecimal getCellValueAsBigDecimal(Cell cell) {
        if (cell == null) return null;
        try {
            switch (cell.getCellType()) {
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return null;
                    }
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                case STRING:
                    String val = cell.getStringCellValue().trim().replace(",", "");
                    return val.isEmpty() ? null : new BigDecimal(val);
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}