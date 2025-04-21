package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.App;
import java.util.Scanner;

public final class SettingsMenu {
    private SettingsMenu() {}

    private static final Scanner scanner = new Scanner(System.in);
    public static void start(ExerciseCatalog catalog) {
        boolean inSettings = true;
        while (inSettings) {
            System.out.println("\n=== Settings ===");
            System.out.println("1. List exercises");
            System.out.println("2. Add custom exercise");
            System.out.println("3. Delete custom exercise");
            System.out.println("4. View muscle progress");
            System.out.println("5. Back to main menu");

            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> {
                    for (var exercise : catalog.all()) {
                        System.out.println("- " + exercise.getName());
                    }
                }
                case "2" -> CustomExerciseManager.addCustomExercise(catalog);
                case "3" -> CustomExerciseManager.deleteCustomExercise(catalog, App.custom_exercises_json);
                case "4" -> ViewMuscleProgress.display();
                case "5" -> inSettings = false;
                default -> System.out.println("Invalid input. Try again.");
            }
        }
    }
}
