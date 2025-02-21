package com.hszg.demo.data.impl;

import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * Utility-Klasse zum Speichern und Laden einer .properties-Datei.
 * Hinweis: Auf Heroku sind Daten flüchtig, da sie bei einem Dyno-Neustart verloren gehen.
 */
@Component
public class PropertiesStorage {

    // Properties-Objekt zum Speichern der Schlüssel/Werte
    private final Properties props = new Properties();
    // Dateipfad für die Properties-Datei
    private final String filePath;

    public PropertiesStorage() {
        // Pfad kann individuell angepasst werden – hier wird "data.properties" im Arbeitsverzeichnis verwendet
        this.filePath = "data.properties";
        load();
    }

    // Lädt die Properties-Datei
    private void load() {
        try (InputStream in = new FileInputStream(filePath)) {
            props.load(in);
        } catch (IOException e) {
            System.out.println("PropertiesStorage: Datei nicht gefunden oder Fehler beim Laden.");
        }
    }

    // Speichert die Properties in die Datei
    private void save() {
        try (OutputStream out = new FileOutputStream(filePath)) {
            props.store(out, "Chakra Properties Storage");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Setzt einen Wert und speichert die Änderungen (thread-sicher)
    public synchronized void put(String key, String value) {
        props.setProperty(key, value);
        save();
    }

    // Gibt den Wert zu einem Schlüssel zurück (thread-sicher)
    public synchronized String get(String key) {
        return props.getProperty(key);
    }

    // Entfernt einen Schlüssel und speichert die Änderungen (thread-sicher)
    public synchronized void remove(String key) {
        props.remove(key);
        save();
    }

    // Liefert alle E-Mails, die in den Properties gespeichert sind
    public List<String> getAllEmails() {
        Set<String> keys = props.stringPropertyNames(); // Alle Schlüssel abrufen
        Set<String> emails = new HashSet<>();
        for (String key : keys) {
            // Schlüssel haben das Format "user.<email>.id"
            if (key.startsWith("user.") && key.endsWith(".id")) {
                // E-Mail-Teil extrahieren
                String emailPart = key.substring("user.".length(), key.lastIndexOf(".id"));
                emails.add(emailPart);
            }
        }
        return new ArrayList<>(emails);
    }

}
