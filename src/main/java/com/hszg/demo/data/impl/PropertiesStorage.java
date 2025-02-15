package com.hszg.demo.data.impl;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * Utility-Klasse für das Speichern & Laden einer .properties-Datei.
 * Achtung: Auf Heroku ephemeral, d.h. Daten verschwinden bei Dyno-Neustart.
 */
@Component
public class PropertiesStorage {

    private final Properties props = new Properties();
    private final String filePath;

    public PropertiesStorage() {
        // Pfad kannst du individuell anpassen.
        // Hier wird die Datei "data.properties" direkt im Arbeitsverzeichnis angelegt.
        this.filePath = "data.properties";
        load();
    }

    private void load() {
        try (InputStream in = new FileInputStream(filePath)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("PropertiesStorage: Datei nicht gefunden oder Fehler beim Laden.");
        }
    }

    private void save() {
        try (OutputStream out = new FileOutputStream(filePath)) {
            props.store(out, "Chakra Properties Storage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Thread-sicher mit synchronized
    public synchronized void put(String key, String value) {
        props.setProperty(key, value);
        save();
    }

    public synchronized String get(String key) {
        return props.getProperty(key);
    }

    public synchronized void remove(String key) {
        props.remove(key);
        save();
    }
    // In class PropertiesStorage:

    public List<String> getAllEmails() {
        Set<String> keys = props.stringPropertyNames(); // props is a java.util.Properties
        Set<String> emails = new HashSet<>();
        for (String key : keys) {
            // key looks like "user.someemail@... .id"
            if (key.startsWith("user.") && key.endsWith(".id")) {
                // extract the email part
                String emailPart = key.substring("user.".length(), key.lastIndexOf(".id"));
                emails.add(emailPart);
            }
        }
        return new ArrayList<>(emails);
    }

}