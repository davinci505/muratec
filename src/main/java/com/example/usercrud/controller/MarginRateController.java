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
@RequestMapping("/margin-rates")
public class MarginRateController {

    @Autowired
    private MarginRateService marginRateService;

    @GetMapping
    public String listMarginRates(Model model) {
        model.addAttribute("marginRates", marginRateService.getAllMarginRates());
        return "margin-rates/list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("marginRate", new MarginRate());
        return "margin-rates/form";
    }

    @PostMapping
    public String createMarginRate(@ModelAttribute MarginRate marginRate) {
        marginRateService.saveMarginRate(marginRate);
        return "redirect:/margin-rates";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        MarginRate marginRate = marginRateService.getMarginRateById(id)
                .orElseThrow(() -> new RuntimeException("MarginRate not found"));
        model.addAttribute("marginRate", marginRate);
        return "margin-rates/form";
    }

    @PostMapping("/{id}")
    public String updateMarginRate(@PathVariable Long id, @ModelAttribute MarginRate marginRate) {
        marginRateService.updateMarginRate(id, marginRate);
        return "redirect:/margin-rates";
    }

    @GetMapping("/delete/{id}")
    public String deleteMarginRate(@PathVariable Long id) {
        marginRateService.deleteMarginRate(id);
        return "redirect:/margin-rates";
    }
}
