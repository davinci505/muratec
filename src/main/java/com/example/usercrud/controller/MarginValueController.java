package com.example.usercrud.controller;

import com.example.usercrud.model.MarginRate;
import com.example.usercrud.service.MarginRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/margin-values")
public class MarginValueController {

    @Autowired
    private MarginRateService marginRateService;

    @GetMapping
    public String listMarginValues(Model model) {
        model.addAttribute("marginRates", marginRateService.getMarginValues());
        return "margin-values/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("marginValue", new MarginValueForm());
        return "margin-values/form";
    }

    @PostMapping
    public String createMarginValue(@ModelAttribute("marginValue") MarginValueForm form) {
        MarginRate marginRate = new MarginRate();
        marginRate.setName(form.getName());
        marginRate.setCategory("MARGIN_VALUE");
        marginRate.setYenExchangeRate(form.getYenExchangeRate());
        marginRate.setMarginRate(form.getMarginRate());
        marginRateService.saveMarginRate(marginRate);
        return "redirect:/margin-values";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MarginRate marginRate = marginRateService.getMarginRateById(id)
                .orElseThrow(() -> new RuntimeException("MarginRate not found"));
        MarginValueForm form = new MarginValueForm();
        form.setId(marginRate.getId());
        form.setName(marginRate.getName());
        form.setYenExchangeRate(marginRate.getYenExchangeRate());
        form.setMarginRate(marginRate.getMarginRate());
        model.addAttribute("marginValue", form);
        return "margin-values/form";
    }

    @PostMapping("/{id}")
    public String updateMarginValue(@PathVariable Long id,
                                    @ModelAttribute("marginValue") MarginValueForm form) {
        MarginRate details = new MarginRate();
        details.setName(form.getName());
        details.setYenExchangeRate(form.getYenExchangeRate());
        details.setMarginRate(form.getMarginRate());
        marginRateService.updateMarginValue(id, details);
        return "redirect:/margin-values";
    }

    @GetMapping("/delete/{id}")
    public String deleteMarginValue(@PathVariable Long id) {
        marginRateService.deleteMarginRate(id);
        return "redirect:/margin-values";
    }
}
