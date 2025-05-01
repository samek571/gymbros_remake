package com.samuel.gymtracker;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.service.SettingsMenu;
import com.samuel.gymtracker.service.ViewWorkoutHistory;
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
            System.out.println("[Enter] Start workout");
            System.out.println("1. View workout history");
            System.out.println("2. Settings & more");
            System.out.println("3. Exit");

            System.out.print("Where to go: ");
            String input = scanner.nextLine();
            if (input.isBlank()) input = "0";

            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input, please try again.");
                continue;
            }

            switch (choice) {
                case 0 -> WorkoutSessionManager.startSession(catalog);
                case 1 -> ViewWorkoutHistory.viewWorkoutHistory();
                case 2 -> SettingsMenu.start(catalog);
                case 3 -> running = false;
                default -> System.out.println("Try again...");
            }
        }
    }
}
