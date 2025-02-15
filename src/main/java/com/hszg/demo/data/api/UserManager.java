package com.hszg.demo.data.api;

import com.hszg.demo.model.User;

public interface UserManager {
    // Registrieren
    User registerUser(String name, String email, String password) throws Exception;

    // Login
    User loginUser(String email, String password);

    // Finden
    User findByEmail(String email);

    // NEU: Update
    // Return: aktualisierter User oder null, wenn userId nicht gefunden
    // Exception, wenn Email schon vergeben
    User updateUser(Long userId, String newEmail, String newPassword) throws Exception;
}
