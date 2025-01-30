package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.UserManager;
import com.hszg.demo.model.User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PropertiesUserManagerImpl implements UserManager {

    private final PropertiesStorage storage;

    public PropertiesUserManagerImpl(PropertiesStorage storage) {
        this.storage = storage;
    }

    @Override
    public User registerUser(String name, String email, String password) throws Exception {
        // Check: existiert E-Mail bereits?
        User existing = findByEmail(email);
        if (existing != null) {
            throw new Exception("E-Mail bereits vorhanden!");
        }
        // Neues User-Objekt
        User newUser = new User(name, email, password);
        // Wir generieren pseudo-IDs und Token
        long pseudoId = System.currentTimeMillis();
        newUser.setId(pseudoId);
        newUser.setToken(UUID.randomUUID().toString());

        // In .properties ablegen
        // user.<email>.id, user.<email>.name, etc.
        String prefix = "user."+email;
        storage.put(prefix+".id", String.valueOf(pseudoId));
        storage.put(prefix+".name", name);
        storage.put(prefix+".password", password);
        storage.put(prefix+".token", newUser.getToken());

        return newUser;
    }

    @Override
    public User findByEmail(String email) {
        String prefix = "user."+email;
        String idStr = storage.get(prefix+".id");
        if (idStr == null) {
            // E-Mail existiert nicht
            return null;
        }
        User u = new User();
        u.setId(Long.valueOf(idStr));
        u.setEmail(email);
        u.setName(storage.get(prefix+".name"));
        u.setPassword(storage.get(prefix+".password"));
        u.setToken(storage.get(prefix+".token"));
        return u;
    }

    @Override
    public User loginUser(String email, String password) {
        User found = findByEmail(email);
        if (found != null && found.getPassword().equals(password)) {
            return found;
        }
        return null;
    }
}