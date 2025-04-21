package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.model.session.SessionEntry;
import com.samuel.gymtracker.model.session.WorkoutHistory;
import com.samuel.gymtracker.model.session.WorkoutSession;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;

// I think everything is pretty much self explanatory here
public final class WorkoutSessionManager {
    private WorkoutSessionManager() {}

    private static final Scanner scanner = new Scanner(System.in);
    public static void startSession(ExerciseCatalog catalog) {
        WorkoutSession session = new WorkoutSession();
        System.out.println("Workout started at " + session.getStartTime());

        boolean sessionRunning = true;
        while (sessionRunning) {
            System.out.println("\n=== Session Menu ===");
            System.out.println("Commands: log / finish / abort");
            System.out.print("Enter command: ");
            String _input = scanner.nextLine().trim().toLowerCase();

            switch (_input) {
                case "log" -> logExercise(catalog, session);
                case "finish" -> {
                    try {
                        session.finish();
                        System.out.println("Workout finished.");
                        System.out.println("Duration: " + session.getDuration());
                        System.out.println("Xp earned: " + session.getTotalXp());

                        Path historyFile = Path.of("src/main/resources/workout_history.json");

                        //again something that could be one line...fucking java
                        WorkoutHistory history = WorkoutRepository.loadHistory(historyFile);
                        history.addSession(session);
                        WorkoutRepository.saveHistory(historyFile, history);

                        System.out.println("History updated.");
                    } catch (IOException e) {
                        System.out.println("Error saving: " + e.getMessage());
                    }
                    sessionRunning = false;
                }

                case "abort" -> {
                    System.out.println("No data saved, workout halted.");
                    sessionRunning = false;
                }
                default -> System.out.println("Try again with normal command.");
            }
        }
    }

    private static void logExercise(ExerciseCatalog catalog, WorkoutSession session) {
        System.out.print("Exercise name: ");
        String name = scanner.nextLine().trim();
        var ex = catalog.get(name);
        
        if (ex == null) {System.out.println("Not found."); return; }

        System.out.print("REPS (Press \"Enter\" if not relevant or zero): ");
        String _reps = scanner.nextLine();
        int reps = _reps.isBlank() ? 0 : Integer.parseInt(_reps);

        System.out.print("WEIGHT (Press \"Enter\" if not relevant or zero): ");
        String _weight = scanner.nextLine();
        double weight = _weight.isBlank() ? 0.0 : Double.parseDouble(_weight);

        System.out.print("TIME (in seconds): ");
        String _time = scanner.nextLine();
        int time = _time.isBlank() ? 0 : Integer.parseInt(_time);


        SessionEntry entry = SessionEntry.of(ex, reps, weight, time, 1.0);
        session.addEntry(entry);
        System.out.println("Logged: " + entry);
    }
}
