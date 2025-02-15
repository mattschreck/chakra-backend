package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.UserManager;
import com.hszg.demo.model.User;
import org.springframework.stereotype.Service;

@Service
public class PropertiesUserManagerImpl implements UserManager {

    private final PropertiesStorage storage;

    public PropertiesUserManagerImpl(PropertiesStorage storage) {
        this.storage = storage;
    }

    @Override
    public User registerUser(String name, String email, String password) throws Exception {
        if (findByEmail(email) != null) {
            throw new Exception("E-Mail bereits vorhanden!");
        }
        long pseudoId = System.currentTimeMillis();
        User newUser = new User(name, email, password);
        newUser.setId(pseudoId);

        String prefix = "user." + email;
        storage.put(prefix + ".id", String.valueOf(pseudoId));
        storage.put(prefix + ".name", name);
        storage.put(prefix + ".password", password);

        return newUser;
    }

    @Override
    public User loginUser(String email, String password) {
        User found = findByEmail(email);
        if (found != null && found.getPassword().equals(password)) {
            return found;
        }
        return null;
    }

    @Override
    public User findByEmail(String email) {
        String prefix = "user." + email;
        String idStr = storage.get(prefix + ".id");
        if (idStr == null) {
            return null;
        }

        User u = new User();
        u.setId(Long.valueOf(idStr));
        u.setName(storage.get(prefix + ".name"));
        u.setEmail(email);
        u.setPassword(storage.get(prefix + ".password"));
        return u;
    }

    @Override
    public User updateUser(Long userId, String newEmail, String newPassword) throws Exception {
        // Finde den User
        User current = findByUserId(userId);
        if (current == null) {
            return null; // => "User nicht gefunden"
        }

        // E-Mail ändern?
        if (newEmail != null && !newEmail.trim().isEmpty()
                && !newEmail.equalsIgnoreCase(current.getEmail())) {

            if (findByEmail(newEmail) != null) {
                throw new Exception("Diese E-Mail ist bereits vergeben!");
            }

            // Alte Keys löschen
            String oldPrefix = "user." + current.getEmail();
            storage.remove(oldPrefix + ".id");
            storage.remove(oldPrefix + ".name");
            storage.remove(oldPrefix + ".password");

            // Neue Keys
            String newPrefix = "user." + newEmail;
            storage.put(newPrefix + ".id", String.valueOf(current.getId()));
            storage.put(newPrefix + ".name", current.getName() == null ? "" : current.getName());
            storage.put(newPrefix + ".password", current.getPassword() == null ? "" : current.getPassword());

            // Im User-Objekt ändern
            current.setEmail(newEmail);
        }

        // Passwort ändern?
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            current.setPassword(newPassword);
        }

        // Speichern (prefix = user.<aktuelleEmail>)
        String prefix = "user." + current.getEmail();
        storage.put(prefix + ".name", current.getName() == null ? "" : current.getName());
        storage.put(prefix + ".password", current.getPassword() == null ? "" : current.getPassword());

        return current;
    }

    // Hilfsmethode: user per ID finden
    private User findByUserId(Long wantedId) {
        for (String email : storage.getAllEmails()) {
            User u = findByEmail(email);
            if (u != null && u.getId().equals(wantedId)) {
                // Achte auf .equals(...) statt '=='
                return u;
            }
        }
        return null;
    }
}
