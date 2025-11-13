package com.example.usercrud.controller;

import com.example.usercrud.model.Part;
import com.example.usercrud.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/parts")
public class PartController {
    
    @Autowired
    private PartService partService;
    
    @GetMapping
    public String listParts(Model model) {
        model.addAttribute("parts", partService.getAllParts());
        return "parts/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("part", new Part());
        return "parts/form";
    }
    
    @GetMapping("/bulk")
    public String showBulkCreateForm() {
        return "parts/bulk-form";
    }
    
    @PostMapping
    public String createPart(@ModelAttribute Part part) {
        partService.savePart(part);
        return "redirect:/parts";
    }
    
    @PostMapping("/bulk")
    @ResponseBody
    public String createBulkParts(@RequestBody java.util.List<Part> parts) {
        System.out.println("받은 부품 개수: " + parts.size());
        for (Part part : parts) {
            System.out.println("P-WBS: " + part.getPWbs() + ", Project Code: " + part.getProjectCode());
            partService.savePart(part);
        }
        return "success";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Part part = partService.getPartById(id)
                .orElseThrow(() -> new RuntimeException("Part not found"));
        model.addAttribute("part", part);
        return "parts/form";
    }
    
    @PostMapping("/{id}")
    public String updatePart(@PathVariable Long id, @ModelAttribute Part part) {
        partService.updatePart(id, part);
        return "redirect:/parts";
    }
    
    @GetMapping("/delete/{id}")
    public String deletePart(@PathVariable Long id) {
        partService.deletePart(id);
        return "redirect:/parts";
    }
    
    @PostMapping("/export/excel")
    public void exportToExcel(@RequestParam(value = "ids", required = false) String ids,
                              jakarta.servlet.http.HttpServletResponse response) throws Exception {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=parts_" + 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx");
        
        java.util.List<Part> parts;
        
        // ids가 제공되면 해당 ID의 부품만 가져오기
        if (ids != null && !ids.isEmpty()) {
            String[] idArray = ids.split(",");
            parts = new java.util.ArrayList<>();
            for (String idStr : idArray) {
                try {
                    Long id = Long.parseLong(idStr.trim());
                    partService.getPartById(id).ifPresent(parts::add);
                } catch (NumberFormatException e) {
                    // 잘못된 ID는 무시
                }
            }
        } else {
            // ids가 없으면 전체 가져오기
            parts = partService.getAllParts();
        }
        
        org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
        org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("Parts");
        
        // 헤더 스타일 생성 (회색 배경, 테두리)
        org.apache.poi.ss.usermodel.CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        headerStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        headerStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        headerStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        org.apache.poi.ss.usermodel.Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
        
        // 데이터 셀 스타일 생성 (테두리만)
        org.apache.poi.ss.usermodel.CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        dataStyle.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        dataStyle.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        dataStyle.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        
        // 헤더 행 생성
        org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "P-WBS", "Project Code", "Ser.No.", "Model", "Machine Name", 
            "Type", "No.Machine", "Unit Name", "Murata Parts No.", "New Parts No.", 
            "Description", "Manufacturer", "Manut.Parts No.", "Rank", "Parts(M)", "Parts(T)", "Unit price"};
        
        for (int i = 0; i < headers.length; i++) {
            org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }
        
        // 데이터 행 생성
        int rowNum = 1;
        for (Part part : parts) {
            org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
            
            createCell(row, 0, part.getId() != null ? part.getId().toString() : "", dataStyle);
            createCell(row, 1, part.getPWbs(), dataStyle);
            createCell(row, 2, part.getProjectCode(), dataStyle);
            createCell(row, 3, part.getSerNo(), dataStyle);
            createCell(row, 4, part.getModel(), dataStyle);
            createCell(row, 5, part.getMachineName(), dataStyle);
            createCell(row, 6, part.getType(), dataStyle);
            createCell(row, 7, part.getNoOfMachine() != null ? part.getNoOfMachine().toString() : "", dataStyle);
            createCell(row, 8, part.getUnitName(), dataStyle);
            createCell(row, 9, part.getMurataPartsNo(), dataStyle);
            createCell(row, 10, part.getNewPartsNo(), dataStyle);
            createCell(row, 11, part.getDescriptionOfParts(), dataStyle);
            createCell(row, 12, part.getManufacturer(), dataStyle);
            createCell(row, 13, part.getManutPartsNo(), dataStyle);
            createCell(row, 14, part.getRank(), dataStyle);
            createCell(row, 15, part.getNoOfPartsMachine() != null ? part.getNoOfPartsMachine().toString() : "", dataStyle);
            createCell(row, 16, part.getNoOfPartsTotal() != null ? part.getNoOfPartsTotal().toString() : "", dataStyle);
            createCell(row, 17, part.getUnitPrice(), dataStyle);
        }
        
        // 컬럼 너비 자동 조정
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }
        
        workbook.write(response.getOutputStream());
        workbook.close();
    }
    
    private void createCell(org.apache.poi.ss.usermodel.Row row, int column, String value, 
                           org.apache.poi.ss.usermodel.CellStyle style) {
        org.apache.poi.ss.usermodel.Cell cell = row.createCell(column);
        cell.setCellValue(value != null ? value : "");
        cell.setCellStyle(style);
    }
}
