package com.hszg.demo.controller;

import com.hszg.demo.data.api.UserManager;
import com.hszg.demo.model.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

    private final UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String name = payload.get("name");
        String email = payload.get("email");
        String password = payload.get("password");

        try {
            User newUser = userManager.registerUser(name, email, password);
            response.put("success", true);
            response.put("message", "Registrierung erfolgreich!");
            response.put("userId", newUser.getId());
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String email = payload.get("email");
        String password = payload.get("password");

        User user = userManager.loginUser(email, password);
        if (user != null) {
            response.put("success", true);
            response.put("message", "Login erfolgreich!");
            response.put("token", user.getToken());
            response.put("userId", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
        } else {
            response.put("success", false);
            response.put("message", "E-Mail oder Passwort falsch!");
        }
        return response;
    }
}