package com.hszg.demo.data.api;

import com.hszg.demo.model.User;

public interface UserManager {
    // Registrieren
    User registerUser(String name, String email, String password) throws Exception;

    // Finden nach E-Mail
    User findByEmail(String email);

    // Optional: Login
    User loginUser(String email, String password);
}
