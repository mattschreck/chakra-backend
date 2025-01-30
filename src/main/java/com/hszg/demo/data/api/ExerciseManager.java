package com.hszg.demo.data.api;

import com.hszg.demo.model.Exercise;
import java.util.List;

public interface ExerciseManager {
    // Holt alle Übungen für einen bestimmten User
    List<Exercise> getAllExercisesForUser(Long userId);

    // Legt eine neue Übung für diesen User an
    Exercise addExercise(Long userId, Exercise exercise);
}