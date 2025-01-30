package com.hszg.demo.controller;

import com.hszg.demo.data.api.ExerciseManager;
import com.hszg.demo.model.Exercise;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {

    private final ExerciseManager exerciseManager;

    public ExerciseController(ExerciseManager exerciseManager) {
        this.exerciseManager = exerciseManager;
    }

    @GetMapping("/{userId}")
    public List<Exercise> getAllExercisesForUser(@PathVariable Long userId) {
        return exerciseManager.getAllExercisesForUser(userId);
    }

    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Exercise createExercise(@PathVariable Long userId, @RequestBody Exercise exercise) {
        return exerciseManager.addExercise(userId, exercise);
    }
}