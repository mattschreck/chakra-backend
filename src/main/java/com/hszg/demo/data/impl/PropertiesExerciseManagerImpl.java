package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.ExerciseManager;
import com.hszg.demo.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PropertiesExerciseManagerImpl implements ExerciseManager {

    // Speicher, in dem die Übungsdaten abgelegt werden
    private final PropertiesStorage storage;

    public PropertiesExerciseManagerImpl(PropertiesStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Exercise> getAllExercisesForUser(Long userId) {
        // Schlüssel für die Liste der Übungs-IDs für den Benutzer
        String listKey = "exercises." + userId + ".list";
        String idList = storage.get(listKey);
        if (idList == null || idList.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] ids = idList.split(",");

        List<Exercise> result = new ArrayList<>();
        for (String id : ids) {
            String prefix = "exercises." + userId + "." + id;

            // Übungsfelder laden
            String title = storage.get(prefix + ".title");
            if (title == null) {
                continue; // Übung ignorieren, wenn der Titel fehlt (z.B. gelöscht)
            }
            String start = storage.get(prefix + ".start");
            String weightStr = storage.get(prefix + ".weight");
            String repsStr = storage.get(prefix + ".repetitions");
            String setsStr = storage.get(prefix + ".sets");
            String bodyPart = storage.get(prefix + ".bodyPart");

            // Neue Exercise-Instanz befüllen
            Exercise e = new Exercise();
            e.setId(id);
            e.setTitle(title);
            e.setStart(start);
            e.setWeight(weightStr == null ? null : Integer.valueOf(weightStr));
            e.setRepetitions(repsStr == null ? null : Integer.valueOf(repsStr));
            e.setSets(setsStr == null ? null : Integer.valueOf(setsStr));
            e.setBodyPart(bodyPart);

            result.add(e);
        }
        return result;
    }

    @Override
    public Exercise addExercise(Long userId, Exercise exercise) {
        // Erzeuge eine zufällige ID für die neue Übung
        String newId = UUID.randomUUID().toString();
        exercise.setId(newId);

        // Übungs-ID in die bestehende Liste eintragen
        String listKey = "exercises." + userId + ".list";
        String currentList = storage.get(listKey);
        if (currentList == null || currentList.trim().isEmpty()) {
            currentList = newId;
        } else {
            currentList += "," + newId;
        }
        storage.put(listKey, currentList);

        // Übungsfelder in den Speicher schreiben
        String prefix = "exercises." + userId + "." + newId;
        storage.put(prefix + ".title", exercise.getTitle() == null ? "" : exercise.getTitle());
        storage.put(prefix + ".start", exercise.getStart() == null ? "" : exercise.getStart());
        storage.put(prefix + ".weight", exercise.getWeight() == null ? "" : exercise.getWeight().toString());
        storage.put(prefix + ".repetitions", exercise.getRepetitions() == null ? "" : exercise.getRepetitions().toString());
        storage.put(prefix + ".sets", exercise.getSets() == null ? "" : exercise.getSets().toString());
        storage.put(prefix + ".bodyPart", exercise.getBodyPart() == null ? "" : exercise.getBodyPart());

        return exercise;
    }

    @Override
    public boolean deleteExercise(Long userId, String exerciseId) {
        // Lade die Liste der Übungs-IDs
        String listKey = "exercises." + userId + ".list";
        String idList = storage.get(listKey);
        if (idList == null || idList.isEmpty()) {
            return false; // Nichts zu löschen, Liste ist leer
        }

        String[] ids = idList.split(",");
        StringBuilder newList = new StringBuilder();
        boolean found = false;

        // Erstelle eine neue Liste ohne die zu löschende ID
        for (String id : ids) {
            if (id.equals(exerciseId)) {
                found = true;
                // Diese ID nicht übernehmen
            } else {
                if (newList.length() > 0) newList.append(",");
                newList.append(id);
            }
        }

        if (!found) {
            return false; // Übungs-ID nicht gefunden
        }

        // Speichere die aktualisierte Liste
        storage.put(listKey, newList.toString());

        // Entferne alle Eigenschaften der Übung
        String prefix = "exercises." + userId + "." + exerciseId;
        storage.remove(prefix + ".title");
        storage.remove(prefix + ".start");
        storage.remove(prefix + ".weight");
        storage.remove(prefix + ".repetitions");
        storage.remove(prefix + ".sets");

        return true;
    }
}
