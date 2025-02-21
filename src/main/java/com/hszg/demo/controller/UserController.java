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

    // Instanz des UserManager, der die Benutzerlogik übernimmt
    private final UserManager userManager;

    public UserController(UserManager userManager) {
        this.userManager = userManager;
    }

    // POST /api/users/register
    // Registriert einen neuen Benutzer
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

    // POST /api/users/login
    // Meldet einen Benutzer an
    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String email = payload.get("email");
        String password = payload.get("password");

        User user = userManager.loginUser(email, password);
        if (user != null) {
            response.put("success", true);
            response.put("message", "Login erfolgreich");
            response.put("token", "DUMMY-TOKEN");
            response.put("userId", user.getId());
            response.put("name", user.getName());
            response.put("email", user.getEmail());
        } else {
            response.put("success", false);
            response.put("message", "E-Mail oder Passwort falsch");
        }
        return response;
    }

    // PUT /api/users/{userId}
    // Aktualisiert die E-Mail und/oder das Passwort eines Benutzers
    @PutMapping("/{userId}")
    public Map<String, Object> updateUser(@PathVariable Long userId,
                                          @RequestBody Map<String, String> payload) {
        Map<String, Object> response = new HashMap<>();
        String newEmail = payload.get("email");
        String newPassword = payload.get("password");

        try {
            User updated = userManager.updateUser(userId, newEmail, newPassword);
            if (updated == null) {
                response.put("success", false);
                response.put("message", "User nicht gefunden.");
            } else {
                response.put("success", true);
                response.put("message", "Update erfolgreich!");
                if (newEmail != null && !newEmail.trim().isEmpty()) {
                    response.put("updatedEmail", updated.getEmail());
                }
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
        }
        return response;
    }
}
