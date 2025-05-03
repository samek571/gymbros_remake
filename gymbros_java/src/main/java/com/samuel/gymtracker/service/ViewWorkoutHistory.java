package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.session.SessionEntry;
import com.samuel.gymtracker.model.session.WorkoutHistory;
import com.samuel.gymtracker.model.session.WorkoutSession;
import java.nio.file.Files;
import java.nio.file.Path;


/**
 * utility class providing functionality to view users workout history.
 * Loads data from {@code src/main/resources/workout_history.json} and prints structured summary of all completed workout sessions, XP and details
 * This class is not meant to be instantiated
 */
public final class ViewWorkoutHistory {
    /**
     * private constructor preventing instantiation
     */
    private ViewWorkoutHistory() {}


    /**
     * reads workout history from JSON and prints each session in human-readable format, session includes: Start time, duration, xp, list of individual ex entries
     * <p>
     * if history file does not exist or empty - warning message
     * exceptions during loading or parsing are caught and signalized
     * </p>
     */
    public static void viewWorkoutHistory() {
        try {
            Path historyFile = Path.of("src/main/resources/workout_history.json");
            if (!Files.exists(historyFile)) {
                System.out.println("No history persistent.");
                return;
            }

            WorkoutHistory history = WorkoutRepository.loadHistory(historyFile);
            if (history.getSessions().isEmpty()) {
                System.out.println("No sessions recorded.");
                return;
            }

            System.out.println("\n=== Workout History ===");
            for (WorkoutSession session : history.getSessions()) {
                System.out.println("Started at: " + session.getStartTime());
                System.out.println("Duration: " + session.getDuration());
                System.out.println("Total XP: " + session.getTotalXp());

                for (SessionEntry entry : session.getEntries()) {
                    System.out.println("  - " + entry.getExercise().getName() +
                            " | Reps: " + entry.getReps() +
                            " | Weight: " + entry.getWeight() +
                            " | Seconds: " + entry.getSeconds() +
                            " | XP: " + entry.getXpEarned());
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Failed to load workout history: " + e.getMessage());
        }
    }
}
