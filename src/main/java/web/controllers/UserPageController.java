package web.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import web.model.Role;
import web.model.User;
import web.service.UserService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserPageController {
    private UserService userService;

    public UserPageController(UserService service) {
        this.userService = service;
    }

    @GetMapping(value = "")
    public String UserPage(Authentication auth, ModelMap model) {
        User user = (User) auth.getPrincipal();
        String headEmail = user.getEmail();
        model.addAttribute("headEmail", headEmail);
        String headRoles = Arrays.toString(user.getRoles().toArray());
        model.addAttribute("headRoles", headRoles);
        model.addAttribute("userPage", user);
        return "userPage";
    }
}