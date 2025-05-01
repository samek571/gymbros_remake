package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.App;
import com.samuel.gymtracker.templates.TemplateSandboxManager;

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
            System.out.println("5. List exercises with given muscle");
            System.out.println("6. Create custom template");
            System.out.println("7. Back to main menu");

            System.out.print("Choose an option: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> ExerciseLister.listAllExercises(catalog);
                case "2" -> CustomExerciseManager.addCustomExercise(catalog);
                case "3" -> CustomExerciseManager.deleteCustomExercise(catalog, App.custom_exercises_json);
                case "4" -> ViewMuscleProgress.display();
                case "5" -> ExerciseLister.listExercisesForMuscle(catalog);
                case "6" -> TemplateSandboxManager.open(catalog, scanner);
                case "7" -> inSettings = false;
                default -> System.out.println("Invalid input. Try again.");
            }
        }
    }
}
