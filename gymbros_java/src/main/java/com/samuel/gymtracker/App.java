package com.samuel.gymtracker;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.service.SettingsMenu;
import com.samuel.gymtracker.service.ViewWorkoutHistory;
import com.samuel.gymtracker.service.WorkoutSessionManager;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * THE APPLICATION - entry point for the GymBros app
 * init and konsole prompt - starting workout, viewing workout history, settings customization and to analyze
 */
public class App {
    /**
     * Private constructor to prevent instantiation
     */
    private App() {}



    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Path to JSON storing user defined custom exs
     */
    public static final Path custom_exercises_json = Path.of("src/main/resources/custom_exercises.json");

    /**
     * innit and run of the main menu loop app
     * loading built-ins and custom exercises and waits for user input for further direction

     * @param args Command-line arguments (not used)
     * @throws Exception if loading exercises || resources fail
     */
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
