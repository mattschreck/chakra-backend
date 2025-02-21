package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.UserManager;
import com.hszg.demo.model.User;
import org.springframework.stereotype.Service;

@Service
public class PropertiesUserManagerImpl implements UserManager {

    // Speicher für Benutzerdaten
    private final PropertiesStorage storage;

    public PropertiesUserManagerImpl(PropertiesStorage storage) {
        this.storage = storage;
    }

    // Registriert einen neuen Benutzer
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

    // Meldet einen Benutzer an
    @Override
    public User loginUser(String email, String password) {
        User found = findByEmail(email);
        if (found != null && found.getPassword().equals(password)) {
            return found;
        }
        return null;
    }

    // Sucht einen Benutzer anhand der E-Mail
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

    // Aktualisiert die Daten eines Benutzers (E-Mail und/oder Passwort)
    @Override
    public User updateUser(Long userId, String newEmail, String newPassword) throws Exception {
        // Benutzer anhand der ID suchen
        User current = findByUserId(userId);
        if (current == null) {
            return null; // Benutzer nicht gefunden
        }

        // E-Mail ändern, wenn nötig
        if (newEmail != null && !newEmail.trim().isEmpty()
                && !newEmail.equalsIgnoreCase(current.getEmail())) {

            if (findByEmail(newEmail) != null) {
                throw new Exception("Diese E-Mail ist bereits vergeben!");
            }

            // Alte Einträge löschen
            String oldPrefix = "user." + current.getEmail();
            storage.remove(oldPrefix + ".id");
            storage.remove(oldPrefix + ".name");
            storage.remove(oldPrefix + ".password");

            // Neue Einträge speichern
            String newPrefix = "user." + newEmail;
            storage.put(newPrefix + ".id", String.valueOf(current.getId()));
            storage.put(newPrefix + ".name", current.getName() == null ? "" : current.getName());
            storage.put(newPrefix + ".password", current.getPassword() == null ? "" : current.getPassword());

            current.setEmail(newEmail);
        }

        // Passwort ändern, wenn angegeben
        if (newPassword != null && !newPassword.trim().isEmpty()) {
            current.setPassword(newPassword);
        }

        // Aktualisierte Daten speichern
        String prefix = "user." + current.getEmail();
        storage.put(prefix + ".name", current.getName() == null ? "" : current.getName());
        storage.put(prefix + ".password", current.getPassword() == null ? "" : current.getPassword());

        return current;
    }

    // Hilfsmethode: Sucht einen Benutzer anhand der ID
    private User findByUserId(Long wantedId) {
        for (String email : storage.getAllEmails()) {
            User u = findByEmail(email);
            if (u != null && u.getId().equals(wantedId)) {
                return u;
            }
        }
        return null;
    }
}
