package com.ayush.kitchensink.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ayush.kitchensink.model.Member;
import com.ayush.kitchensink.service.MemberRegistration;

import jakarta.validation.Valid;


@Controller
public class MemberController {

    private final MemberRegistration memberRegistration;

    public MemberController(MemberRegistration memberRegistration) {
        this.memberRegistration = memberRegistration;
    }

    @GetMapping("/members/new")
    public String showRegistrationForm(Model model) {
        model.addAttribute("newMember", new Member());
        return "register";
    }

    @PostMapping("/members/register")
    public String register(@Valid @ModelAttribute("newMember") Member newMember, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "register";
        }

        try {
            memberRegistration.register(newMember);
            model.addAttribute("message", "Registration successful!");
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Registration failed: " + e.getMessage());
        }
        return "register";
    }
}
