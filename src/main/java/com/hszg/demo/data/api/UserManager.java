package com.hszg.demo.data.api;

import com.hszg.demo.model.User;

public interface UserManager {
    // Registriert einen neuen User
    User registerUser(String name, String email, String password) throws Exception;

    // Findet User anhand E-Mail
    User findByEmail(String email);

    // Login
    User loginUser(String email, String password);
}