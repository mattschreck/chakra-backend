package com.hszg.demo.controller;

import com.hszg.demo.data.api.ExerciseManager;
import com.hszg.demo.model.Exercise;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exercises")
@CrossOrigin(origins = "*")
public class ExerciseController {

    // Manager zur Verwaltung der Übungen
    private final ExerciseManager exerciseManager;

    public ExerciseController(ExerciseManager exerciseManager) {
        this.exerciseManager = exerciseManager;
    }

    // GET /api/exercises/{userId}
    // Ruft alle Übungen eines Benutzers ab
    @GetMapping("/{userId}")
    public List<Exercise> getAllExercisesForUser(@PathVariable Long userId) {
        return exerciseManager.getAllExercisesForUser(userId);
    }

    // POST /api/exercises/{userId}
    // Erstellt eine neue Übung für den Benutzer
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public Exercise createExercise(@PathVariable Long userId,
                                   @RequestBody Exercise exercise) {
        // Neue Übung hinzufügen (z.B. Setzen der ID etc.)
        return exerciseManager.addExercise(userId, exercise);
    }

    // DELETE /api/exercises/{userId}/{exerciseId}
    // Löscht eine Übung anhand der Übungs-ID
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

    // GET /api/exercises/{userId}/stats?start=YYYY-MM-DD&end=YYYY-MM-DD
    // Ermittelt Statistiken (Übungen pro Körperpartie) im angegebenen Zeitraum
    @GetMapping("/{userId}/stats")
    public Map<String, Integer> getStatsForUser(@PathVariable Long userId,
                                                @RequestParam String start,
                                                @RequestParam String end) {
        List<Exercise> all = exerciseManager.getAllExercisesForUser(userId);

        LocalDate startDate = LocalDate.parse(start);
        LocalDate endDate = LocalDate.parse(end);

        Map<String, Integer> result = new HashMap<>();

        for (Exercise e : all) {
            if (e.getStart() == null || e.getBodyPart() == null) {
                continue;  // Überspringt Übungen ohne Datum oder Körperpartie
            }
            LocalDate d = LocalDate.parse(e.getStart());
            // Prüft, ob das Datum im Bereich liegt
            if (!d.isBefore(startDate) && !d.isAfter(endDate)) {
                String bp = e.getBodyPart();
                result.put(bp, result.getOrDefault(bp, 0) + 1);
            }
        }

        return result;
    }

    // Hilfsklasse für die Antwort beim Löschen einer Übung
    static class DeleteResponse {
        public boolean success;
        public String message;

        public DeleteResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
    }
}
