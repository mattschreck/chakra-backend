package com.hszg.demo;

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

    // Konstruktor-Injection
    public ExerciseController(ExerciseManager exerciseManager) {
        this.exerciseManager = exerciseManager;
    }


    @GetMapping("me")
    public String runningTest() {
        return "worka";
    }



    // GET: Alle Exercises abrufen
    @GetMapping
    public List<Exercise> getAllExercises() {
        return exerciseManager.getAllExercises();
    }

    // POST: Neue Exercise anlegen
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Exercise createExercise(@RequestBody Exercise exercise) {
        return exerciseManager.addExercise(exercise);
    }
}
