package com.samuel.gymtracker.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * util class that displays stored muscle training progress
 * data from JSON {@code src/main/resources/muscle_progress.json}
 */
public final class ViewMuscleProgress {
    /**
     * private constructor preventing instantiation
     */
    private ViewMuscleProgress() {}

    private static final Path _file = Path.of("src/main/resources/muscle_progress.json");
    private static final ObjectMapper _mapper = new ObjectMapper().findAndRegisterModules();


    /**
     * displays some summary of muscle training progress sorted by activity
     * reps, seconds, number of exss, logged per muscle group
     */
    public static void display() {
        try {
            if (!Files.exists(_file)) {
                System.out.println("No progress yet.");
                return;
            }

            Map<String, MuscleProgress> progress;
            try (InputStream in = Files.newInputStream(_file)) {
                progress = _mapper.readValue(in, new TypeReference<>() {});
            }

            if (progress.isEmpty()) {
                System.out.println("No muscles trained yet.");
                return;
            }

            List<Map.Entry<String, MuscleProgress>> _sorted = new ArrayList<>(progress.entrySet());
            _sorted.sort(Comparator.comparingInt((Map.Entry<String, MuscleProgress> e) ->
                    e.getValue().getTotalReps() + e.getValue().getTotalSeconds()).reversed());

            System.out.println("\n=== Muscle Progress ===");
            for (var entry : _sorted) {
                var muscle = entry.getKey();
                var stats = entry.getValue();
                System.out.printf("- %-15s | Reps: %4d | Time: %4d sec | Exercises: %2d%n",
                        muscle, stats.getTotalReps(), stats.getTotalSeconds(), stats.getExercisesLogged());
            }

        } catch (Exception e) {
            System.out.println("Failed to load muscle progress: " + e.getMessage());
        }
    }
}
