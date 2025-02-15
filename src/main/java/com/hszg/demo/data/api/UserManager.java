package com.hszg.demo.data.api;

import com.hszg.demo.model.User;

public interface UserManager {
    User registerUser(String name, String email, String password) throws Exception;
    User loginUser(String email, String password);
    User findByEmail(String email);

    // Update
    User updateUser(Long userId, String newEmail, String newPassword) throws Exception;
}
