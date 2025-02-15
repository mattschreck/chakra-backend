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
        // Check: E-Mail existiert schon?
        if (findByEmail(email) != null) {
            throw new Exception("E-Mail bereits vorhanden!");
        }
        long pseudoId = System.currentTimeMillis(); // Beispiel
        User newUser = new User(name, email, password);
        newUser.setId(pseudoId);

        String prefix = "user." + email;
        storage.put(prefix + ".id", String.valueOf(pseudoId));
        storage.put(prefix + ".name", name);
        storage.put(prefix + ".password", password);

        // optional: token oder andere Felder
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
        u.setEmail(email);
        u.setName(storage.get(prefix + ".name"));
        u.setPassword(storage.get(prefix + ".password"));
        return u;
    }

    @Override
    public User updateUser(Long userId, String newEmail, String newPassword) throws Exception {
        // 1) Finde den User per userId
        User current = findByUserId(userId);
        if (current == null) {
            return null; // user not found
        }

        // 2) E-Mail ändern?
        if (newEmail != null && !newEmail.trim().isEmpty()
                && !newEmail.equalsIgnoreCase(current.getEmail())) {

            // Check: existiert newEmail schon?
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
            // Name bleibt
            storage.put(newPrefix + ".name", current.getName() == null ? "" : current.getName());
            // PW bleibt
            storage.put(newPrefix + ".password", current.getPassword() == null ? "" : current.getPassword());

            // E-Mail im Objekt aktualisieren
            current.setEmail(newEmail);
        }

        // 3) Passwort ändern?
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            current.setPassword(newPassword);
        }

        // 4) Speichern: prefix = user.<aktuelleEmail>
        String prefix = "user." + current.getEmail();
        storage.put(prefix + ".password", current.getPassword());

        // Falls du Name änderst, kannst du das hier auch abfragen.

        return current;
    }

    // Hilfsmethode: user suchen per ID
    private User findByUserId(Long id) {
        // wir durchforsten alle user.*.id
        for (String email : storage.getAllEmails()) {
            User u = findByEmail(email);
            if (u != null && u.getId() == id) {
                return u;
            }
        }
        return null;
    }
}
