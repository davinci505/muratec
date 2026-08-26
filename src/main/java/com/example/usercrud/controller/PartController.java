package com.example.usercrud.controller;

import com.example.usercrud.model.Part;
import com.example.usercrud.service.PartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/parts")
public class PartController {

    @Autowired
    private PartService service;

    @GetMapping
    public String list(@RequestParam(value = "popup", required = false) boolean popup, Model model) {
        model.addAttribute("popup", popup);
        return "parts/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("entity", new Part());
        return "parts/form";
    }

    @GetMapping("/bulk")
    public String showBulkForm(Model model) {
        return "parts/bulk-form";
    }

    @PostMapping
        public String create(@ModelAttribute("entity") Part entity,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "입력 값이 올바르지 않습니다.");
            return "parts/form";
        }
        
        // Check duplicate part number
        Optional<Part> existing = service.getByPartNumber(entity.getPartNumber());
        if (existing.isPresent()) {
            model.addAttribute("errorMessage", "이미 존재하는 품번입니다: " + entity.getPartNumber());
            return "parts/form";
        }
        
        service.save(entity);
        return "redirect:/parts";
    }

    @PostMapping("/bulk")
    @ResponseBody
    public String createBulk(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "error:파일이 비어있습니다.";
        }
        try {
            int saved = service.bulkImport(file);
            return "success:" + saved;
        } catch (Exception e) {
            return "error:" + e.getMessage();
        }
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Part entity = service.getById(id)
                .orElseThrow(() -> new RuntimeException("Part not found"));
        model.addAttribute("entity", entity);
        model.addAttribute("pageTitle", "부품 정보 수정 - BARATEC");
        return "parts/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                             @ModelAttribute("entity") Part entity,
                         BindingResult bindingResult,
                         Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "입력 값이 올바르지 않습니다.");
            return "parts/form";
        }
        
        // Check duplicate part number (excluding current)
        Optional<Part> existing = service.getByPartNumber(entity.getPartNumber());
        if (existing.isPresent() && !existing.get().getId().equals(id)) {
            model.addAttribute("errorMessage", "이미 존재하는 품번입니다: " + entity.getPartNumber());
            return "parts/form";
        }
        
        service.update(id, entity);
        return "redirect:/parts";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.delete(id);
        return "redirect:/parts";
    }


    
}