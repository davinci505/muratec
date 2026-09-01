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
import java.util.List;
import java.util.Optional;

@Service
public class PartService {

    @Autowired
    private PartRepository repository;

    public List<Part> getAll() {
        return repository.findAll();
    }

    @SuppressWarnings("null")
    public Optional<Part> getById(Long id) {
        return repository.findById(id);
    }

    public Optional<Part> getByPartNumber(String partNumber) {
        return repository.findByPartNumber(partNumber);
    }

    public List<Part> getByFilters(String partNumber, String partName) {
        boolean hasPartNumber = partNumber != null && StringUtils.hasText(partNumber);
        boolean hasPartName = partName != null && StringUtils.hasText(partName);

        String pn = hasPartNumber ? partNumber.trim() : null;
        String pnName = hasPartName ? partName.trim() : null;

        if (hasPartNumber && hasPartName) {
            return repository.findByPartNumberContainingIgnoreCaseAndPartNameContainingIgnoreCase(pn, pnName);
        }
        if (hasPartNumber) {
            return repository.findByPartNumberContainingIgnoreCase(pn);
        }
        if (hasPartName) {
            return repository.findByPartNameContainingIgnoreCase(pnName);
        }
        return repository.findAll();
    }

    // Pagination support
    @SuppressWarnings("null")
    public Page<Part> getByFilters(String partNumber, String partName, Pageable pageable) {
        boolean hasPartNumber = partNumber != null && StringUtils.hasText(partNumber);
        boolean hasPartName = partName != null && StringUtils.hasText(partName);

        String pn = hasPartNumber ? partNumber.trim() : null;
        String pnName = hasPartName ? partName.trim() : null;

        if (hasPartNumber && hasPartName) {
            return repository.findByPartNumberContainingIgnoreCaseAndPartNameContainingIgnoreCase(pn, pnName, pageable);
        }
        if (hasPartNumber) {
            return repository.findByPartNumberContainingIgnoreCase(pn, pageable);
        }
        if (hasPartName) {
            return repository.findByPartNameContainingIgnoreCase(pnName, pageable);
        }
        return repository.findAll(pageable);
    }

    public Part save(Part entity) {
        return repository.save(entity);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    @SuppressWarnings("null")
    public Part update(Long id, Part details) {
        Part entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Part not found with id: " + id));

        entity.setPartNumber(details.getPartNumber());
        entity.setPartName(details.getPartName());
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
                part.setPartName(getCellValueAsString(row.getCell(1)));
                part.setSpec(getCellValueAsString(row.getCell(2)));
                part.setPriceJpy(getCellValueAsBigDecimal(row.getCell(3)));

                if (part.getPartNumber() != null && !part.getPartNumber().trim().isEmpty()) {
                    // Check if part number already exists
                    Optional<Part> existing = repository.findByPartNumber(part.getPartNumber());
                    if (existing.isPresent()) {
                        // Update existing
                        Part existingPart = existing.get();
                        existingPart.setPartName(part.getPartName());
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

    /**
     * 샘플 엑셀 템플릿을 생성합니다. 헤더 + 샘플 데이터 2행이 포함됩니다.
     */
    public byte[] createTemplate() {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("부품 일괄등록");

            // 헤더 스타일
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerStyle.setAlignment(HorizontalAlignment.CENTER);

            // 헤더 행
            String[] headers = {"품번", "부품명", "스펙", "가격(엔화)"};
            Row headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // 샘플 데이터
            Object[][] sampleData = {
                {"PRT-001", "볼트 M6x20", "스테인리스 304", 150},
                {"PRT-002", "너트 M6",    "스테인리스 304", 80}
            };
            for (int r = 0; r < sampleData.length; r++) {
                Row row = sheet.createRow(r + 1);
                for (int c = 0; c < sampleData[r].length; c++) {
                    Cell cell = row.createCell(c);
                    Object v = sampleData[r][c];
                    if (v instanceof Number) {
                        cell.setCellValue(((Number) v).doubleValue());
                    } else {
                        cell.setCellValue(String.valueOf(v));
                    }
                }
            }

            // 컬럼 너비 자동
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream()) {
                workbook.write(out);
                return out.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("엑셀 템플릿 생성 실패: " + e.getMessage(), e);
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