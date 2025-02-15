package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.ExerciseManager;
import com.hszg.demo.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PropertiesExerciseManagerImpl implements ExerciseManager {

    private final PropertiesStorage storage;

    public PropertiesExerciseManagerImpl(PropertiesStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Exercise> getAllExercisesForUser(Long userId) {
        // IDs laden: exercises.<userId>.list
        String listKey = "exercises." + userId + ".list";
        String idList = storage.get(listKey);
        if (idList == null || idList.trim().isEmpty()) {
            return new ArrayList<>();
        }
        String[] ids = idList.split(",");

        List<Exercise> result = new ArrayList<>();
        for (String id : ids) {
            String prefix = "exercises." + userId + "." + id;

            String title = storage.get(prefix + ".title");
            if (title == null) {
                continue; // Falls gelöscht oder fehlerhaft
            }
            String start = storage.get(prefix + ".start");
            String weightStr = storage.get(prefix + ".weight");
            String repsStr   = storage.get(prefix + ".repetitions");
            String setsStr   = storage.get(prefix + ".sets");

            Exercise e = new Exercise();
            e.setId(id);
            e.setTitle(title);
            e.setStart(start);
            e.setWeight(weightStr == null ? null : Integer.valueOf(weightStr));
            e.setRepetitions(repsStr == null ? null : Integer.valueOf(repsStr));
            e.setSets(setsStr == null ? null : Integer.valueOf(setsStr));

            result.add(e);
        }
        return result;
    }

    @Override
    public Exercise addExercise(Long userId, Exercise exercise) {
        // Erzeuge zufällige ID
        String newId = UUID.randomUUID().toString();
        exercise.setId(newId);

        // In die IDs-Liste eintragen
        String listKey = "exercises." + userId + ".list";
        String currentList = storage.get(listKey);
        if (currentList == null || currentList.trim().isEmpty()) {
            currentList = newId;
        } else {
            currentList += "," + newId;
        }
        storage.put(listKey, currentList);

        // Felder speichern
        String prefix = "exercises." + userId + "." + newId;
        storage.put(prefix + ".title", exercise.getTitle() == null ? "" : exercise.getTitle());
        storage.put(prefix + ".start", exercise.getStart() == null ? "" : exercise.getStart());
        storage.put(prefix + ".weight", exercise.getWeight() == null ? "" : exercise.getWeight().toString());
        storage.put(prefix + ".repetitions", exercise.getRepetitions() == null ? "" : exercise.getRepetitions().toString());
        storage.put(prefix + ".sets", exercise.getSets() == null ? "" : exercise.getSets().toString());

        return exercise;
    }

    @Override
    public boolean deleteExercise(Long userId, String exerciseId) {
        // Lade Liste
        String listKey = "exercises." + userId + ".list";
        String idList = storage.get(listKey);
        if (idList == null || idList.isEmpty()) {
            return false; // Nichts zu löschen
        }

        String[] ids = idList.split(",");
        StringBuilder newList = new StringBuilder();
        boolean found = false;

        for (String id : ids) {
            if (id.equals(exerciseId)) {
                found = true;
                // nicht in newList aufnehmen
            } else {
                if (newList.length() > 0) newList.append(",");
                newList.append(id);
            }
        }

        if (!found) {
            return false; // ID nicht vorhanden
        }

        // Neue Liste speichern
        storage.put(listKey, newList.toString());

        // Properties entfernen
        String prefix = "exercises." + userId + "." + exerciseId;
        storage.remove(prefix + ".title");
        storage.remove(prefix + ".start");
        storage.remove(prefix + ".weight");
        storage.remove(prefix + ".repetitions");
        storage.remove(prefix + ".sets");

        return true;
    }
}
