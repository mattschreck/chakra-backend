package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.UserManager;
import com.hszg.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemoryUserManagerImpl implements UserManager {

    private final List<User> userList = new ArrayList<>();

    @Override
    public User registerUser(String name, String email, String password) throws Exception {
        // Check: Existiert E-Mail schon?
        for (User u : userList) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                throw new Exception("E-Mail bereits vorhanden");
            }
        }
        // Neues User-Objekt anlegen
        User newUser = new User(name, email, password);
        userList.add(newUser);
        return newUser;
    }

    @Override
    public User findByEmail(String email) {
        for (User u : userList) {
            if (u.getEmail().equalsIgnoreCase(email)) {
                return u;
            }
        }
        return null;
    }

    @Override
    public User loginUser(String email, String password) {
        User found = findByEmail(email);
        if (found != null && found.getPassword().equals(password)) {
            return found; // Login erfolgreich
        }
        return null; // Falsches Passwort oder kein User
    }
}
