package com.samuel.gymtracker;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.service.CustomExerciseManager;
import com.samuel.gymtracker.service.WorkoutSessionManager;
import java.nio.file.Path;
import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);
    public static final Path custom_exercises_json = Path.of("src/main/resources/custom_exercises.json");
    public static void main(String[] args) throws Exception {
        ExerciseCatalog catalog = new ExerciseCatalog();

        catalog.loadBuiltInsFromClasspath("/exercises.json");
        catalog.loadCustomFromFile(custom_exercises_json);

        boolean running = true;
        while (running) {
            System.out.println("\n=== Gym Tracker ===");
            System.out.println("1. List exercises");
            System.out.println("2. Add custom exercise");
            System.out.println("3. Delete custom exercise");
            System.out.println("4. Start workout session");
            System.out.println("5. Exit");

            System.out.print("Choose an option: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1 -> {
                    for (var exercise : catalog.all()) {
                        System.out.println("- " + exercise.getName());
                    }
                }

                case 2 -> CustomExerciseManager.addCustomExercise(catalog);
                case 3 -> CustomExerciseManager.deleteCustomExercise(catalog, custom_exercises_json);
                case 4 -> WorkoutSessionManager.startSession(catalog);
                case 5 -> running = false;

                default -> System.out.println("Try again...");
            }
        }
    }
}
