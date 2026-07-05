package com.example.usercrud.controller;

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
        return "redirect:/margin-rates";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        return "redirect:/margin-rates/new";
    }

    @PostMapping
    public String createMarginValue(@ModelAttribute("marginValue") MarginValueForm form) {
        return "redirect:/margin-rates";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        return "redirect:/margin-rates/edit/" + id;
    }

    @PostMapping("/{id}")
    public String updateMarginValue(@PathVariable Long id,
                                    @ModelAttribute("marginValue") MarginValueForm form) {
        return "redirect:/margin-rates";
    }

    @GetMapping("/delete/{id}")
    public String deleteMarginValue(@PathVariable Long id) {
        return "redirect:/margin-rates/delete/" + id;
    }
}
