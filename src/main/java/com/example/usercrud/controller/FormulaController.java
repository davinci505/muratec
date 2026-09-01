package com.example.usercrud.controller;

import com.example.usercrud.model.Formula;
import com.example.usercrud.service.FormulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/formulas")
public class FormulaController {

    @Autowired
    private FormulaService formulaService;

    @GetMapping
    public String list(Model model) {
        return "formulas/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("formula", new Formula());
        model.addAttribute("pageTitle", "새 계산법 등록 - BARATEC");
        return "formulas/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Formula formula = formulaService.getById(id)
                .orElseThrow(() -> new RuntimeException("Formula not found: " + id));
        // 폼 화면에서도 항목을 함께 보여주기 위해 강제 초기화
        formula.getItems().size();
        model.addAttribute("formula", formula);
        model.addAttribute("pageTitle", "계산법 수정 - BARATEC");
        return "formulas/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        formulaService.delete(id);
        return "redirect:/formulas";
    }
}
