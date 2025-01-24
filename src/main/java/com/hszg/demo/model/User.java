package com.hszg.demo.model;

import java.util.UUID;

public class User {

    private String id;       // Eindeutige ID (z.B. generiert)
    private String name;
    private String email;    // Muss eindeutig sein
    private String password; // Ungehasht (nur Demo)
    private String token;    // Z.B. Auth-Token

    public User() {
        // Leerer Konstruktor für Spring
    }

    public User(String name, String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.email = email;
        this.password = password;
        this.token = UUID.randomUUID().toString(); // z.B. generierter Token
    }

    // Getter/Setter
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
}

