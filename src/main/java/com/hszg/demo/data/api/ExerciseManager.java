package com.hszg.demo.data.api;

import com.hszg.demo.model.Exercise;

import java.util.List;

public interface ExerciseManager {
    List<Exercise> getAllExercisesForUser(Long userId);
    Exercise addExercise(Long userId, Exercise exercise);

    //Zum Löschen
    boolean deleteExercise(Long userId, String exerciseId);
}
