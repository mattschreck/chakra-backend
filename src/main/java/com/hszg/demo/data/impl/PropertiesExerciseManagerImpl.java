package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.ExerciseManager;
import com.hszg.demo.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PropertiesExerciseManagerImpl implements ExerciseManager {

    private final PropertiesStorage storage;

    public PropertiesExerciseManagerImpl(PropertiesStorage storage) {
        this.storage = storage;
    }

    @Override
    public List<Exercise> getAllExercisesForUser(Long userId) {
        // Wir verwenden "exercises.<userId>.count" als Zähler
        String countStr = storage.get("exercises."+userId+".count");
        if (countStr == null) {
            return new ArrayList<>();
        }

        int count = Integer.parseInt(countStr);
        List<Exercise> result = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            String prefix = "exercises."+userId+"."+i;

            String title = storage.get(prefix+".title");
            if (title == null) {
                continue; // Falls Lücke
            }

            String start = storage.get(prefix+".start");
            String weightStr = storage.get(prefix+".weight");
            String repStr = storage.get(prefix+".repetitions");
            String setsStr = storage.get(prefix+".sets");

            Exercise e = new Exercise();
            e.setTitle(title);
            e.setStart(start);
            e.setWeight(weightStr == null ? null : Integer.valueOf(weightStr));
            e.setRepetitions(repStr == null ? null : Integer.valueOf(repStr));
            e.setSets(setsStr == null ? null : Integer.valueOf(setsStr));

            result.add(e);
        }
        return result;
    }

    @Override
    public Exercise addExercise(Long userId, Exercise exercise) {
        // Zähler auslesen
        String countStr = storage.get("exercises."+userId+".count");
        int count = (countStr == null) ? 0 : Integer.parseInt(countStr);

        count++;
        storage.put("exercises."+userId+".count", String.valueOf(count));

        String prefix = "exercises."+userId+"."+count;
        storage.put(prefix+".title", exercise.getTitle() == null ? "" : exercise.getTitle());
        storage.put(prefix+".start", exercise.getStart() == null ? "" : exercise.getStart());
        storage.put(prefix+".weight", exercise.getWeight() == null ? "" : exercise.getWeight().toString());
        storage.put(prefix+".repetitions", exercise.getRepetitions() == null ? "" : exercise.getRepetitions().toString());
        storage.put(prefix+".sets", exercise.getSets() == null ? "" : exercise.getSets().toString());

        return exercise;
    }
}