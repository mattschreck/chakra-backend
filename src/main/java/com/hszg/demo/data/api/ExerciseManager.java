package com.hszg.demo.data.api;

import com.hszg.demo.model.Exercise;

import java.util.List;

public interface ExerciseManager {
    List<Exercise> getAllExercises();
    Exercise addExercise(Exercise exercise);
}
