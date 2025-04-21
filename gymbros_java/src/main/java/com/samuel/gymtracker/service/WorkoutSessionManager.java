package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.model.session.SessionEntry;
import com.samuel.gymtracker.model.session.WorkoutSession;
import com.samuel.gymtracker.util.ConsoleUtils;

import java.util.Scanner;

// I think everything is pretty much self explanatory here
public final class WorkoutSessionManager {
    private WorkoutSessionManager() {}

    private static final Scanner scanner = new Scanner(System.in);
    public static void startSession(ExerciseCatalog catalog) {
        WorkoutSession session = new WorkoutSession();
        System.out.println("Workout started at " + session.getStartTime());

        long startTime = System.currentTimeMillis();
        boolean sessionRunning = true;
        while (sessionRunning) {
            refreshTime(session, startTime);

            System.out.print("Enter command: ");
            String _input = scanner.nextLine().trim().toLowerCase();
            switch (_input) {
                case "log" -> {
                    System.out.print("Exercise name: ");
                    String name = scanner.nextLine().trim();
                    var ex = catalog.get(name);
                    if (ex == null) {
                        System.out.println("Exercise not found :/");
                        ConsoleUtils.sleep(1000);
                        continue;
                    }

                    System.out.print("Reps (empty = 0): ");
                    int reps = _parser(scanner.nextLine(), Integer::parseInt, 0);

                    System.out.print("Weight (empty = 0): ");
                    double weight = _parser(scanner.nextLine(), Double::parseDouble, 0.0);

                    System.out.print("Seconds (empty = 0): ");
                    int seconds = _parser(scanner.nextLine(), Integer::parseInt, 0);

                    SessionEntry entry = SessionEntry.of(ex, reps, weight, seconds, 1.0);
                    session.addEntry(entry);
                }
                case "finish" -> {
                    session.finish();
                    System.out.println("Workout finished, good job nig.");
                    System.out.println("Duration: " + session.getDuration());
                    System.out.println("XP earned: " + session.getTotalXp());
                    WorkoutRepository.saveSession(session);
                    sessionRunning = false;
                }
                case "abort" -> {
                    System.out.println("Aborted successful.");
                    sessionRunning = false;
                }
                default -> {
                    System.out.println("Unknown procedure.");
                    ConsoleUtils.sleep(1000);
                }
            }
        }
    }

    private static void refreshTime(WorkoutSession session, long startTime) {
        ConsoleUtils.clearConsole();
        long secondsTotal = (System.currentTimeMillis() - startTime) / 1000;
        long minutes = secondsTotal / 60;
        long seconds = secondsTotal % 60;

        System.out.println("\n=== Current Workout Session ===");
        System.out.printf("Time elapsed: %02d:%02d\n", minutes, seconds);

        if (session.getEntries().isEmpty()) {
            System.out.println("nothing logged yet");
        } else {
            System.out.println("Logged:");
            for (var entry : session.getEntries()) {
                System.out.println("- " + entry.getExercise().getName() +
                        " | Reps: " + entry.getReps() +
                        " | Weight: " + entry.getWeight() +
                        " | Seconds: " + entry.getSeconds());
            }
        }
        System.out.println();
        System.out.println("[Commands] log / finish / abort");
    }
    
    //this is so weight and reps can work under one method due to int vs float
    private static <T> T _parser(String _input, Parser<T> parser, T defaultValue) {
        if (_input.isBlank()) return defaultValue;
        try {
            return parser.parse(_input.trim());
        } catch (Exception e) {
            return defaultValue;
        }
    }
    @FunctionalInterface
    private interface Parser<T> {
        T parse(String s) throws Exception;
    }

}
