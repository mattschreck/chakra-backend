package com.hszg.demo.data.impl;

import com.hszg.demo.data.api.ExerciseManager;
import com.hszg.demo.model.Exercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemoryExerciseManagerImpl implements ExerciseManager {

    private final List<Exercise> exerciseList = new ArrayList<>();

    @Override
    public List<Exercise> getAllExercises() {
        return exerciseList;
    }

    @Override
    public Exercise addExercise(Exercise exercise) {
        // In-Memory speichern
        // Du kannst hier auch eine ID generieren
        exerciseList.add(exercise);
        return exercise;
    }
}
