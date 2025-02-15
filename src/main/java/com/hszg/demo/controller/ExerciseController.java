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

    // GET /api/exercises/{userId}
    @GetMapping("/{userId}")
    public List<Exercise> getAllExercisesForUser(@PathVariable Long userId) {
        return exerciseManager.getAllExercisesForUser(userId);
    }

    // POST /api/exercises/{userId}
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Exercise createExercise(@PathVariable Long userId, @RequestBody Exercise exercise) {
        return exerciseManager.addExercise(userId, exercise);
    }

    // NEU: DELETE /api/exercises/{userId}/{exerciseId}
    @DeleteMapping("/{userId}/{exerciseId}")
    public DeleteResponse deleteExercise(@PathVariable Long userId,
                                         @PathVariable String exerciseId) {
        boolean success = exerciseManager.deleteExercise(userId, exerciseId);
        if (success) {
            return new DeleteResponse(true, "Übung gelöscht.");
        } else {
            return new DeleteResponse(false, "Übung nicht gefunden.");
        }
    }

    // Kleiner Hilfs-Datentransfer für die JSON-Antwort
    static class DeleteResponse {
        public boolean success;
        public String message;

        public DeleteResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
