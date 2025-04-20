package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.Exercise;
import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.model.MuscleGroup;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public final class CustomExerciseManager {
    private CustomExerciseManager() {}

    private static final Scanner scanner = new Scanner(System.in);

    public static void deleteCustomExercise(ExerciseCatalog catalog, Path savePath) {
        try {
            System.out.print("enter the name of the custom exercise to delete: ");

            //i would normally put this into one line but since we are in a production...
            //many things are nonsensly big
            String name = scanner.nextLine().trim();
            boolean removed = catalog.removeCustomExercise(name);
            if (removed) {
                System.out.println("exercise deleted successfully.");
                Files.createDirectories(savePath.getParent());
                catalog.saveCustomToFile(savePath);
            } else {
                System.out.println("invalid custom exercise");
            }
        } catch (Exception e) {
            System.out.println("deletion failed: " + e.getMessage());
        }
    }

    public static void addCustomExercise(ExerciseCatalog catalog) {
        try {
            System.out.print("enter exercise name: ");
            String name = scanner.nextLine().trim();

            System.out.print("enter primary muscles (space separated): ");
            Set<MuscleGroup> primary = parsePrimaryMuscles(scanner.nextLine());

            System.out.print("optionally, enter secondary muscles (space separated, empty ok): ");
            Set<MuscleGroup> secondary = parseSecondaryMuscles(scanner.nextLine());

            Exercise exercise = new Exercise(name, primary, secondary, 0, 0, 0);
            try {
                catalog.addCustomExercise(exercise);
                Path savePath = Path.of("src/main/resources/custom_exercises.json"); //should be variable
                Files.createDirectories(savePath.getParent());
                catalog.saveCustomExercise(savePath, exercise);
                System.out.println("exercise saved");
            } catch (Exception e) {
                System.out.println("add/save of the exercise failed: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("adding exercise failed: " + e.getMessage());
        }
    }

    //public for my internal testing purpose
    public static Set<MuscleGroup> parsePrimaryMuscles(String input) {
        Set<MuscleGroup> muscles = Arrays.stream(input.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> MuscleGroup.valueOf(s.toUpperCase()))
                .collect(Collectors.toSet());

        if (muscles.isEmpty()) {
            throw new IllegalArgumentException("at least one primary muscle is being targeted... fix this shit");
        }

        return muscles;
    }

    public static Set<MuscleGroup> parseSecondaryMuscles(String input) {
        return Arrays.stream(input.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(CustomExerciseManager::safeMuscleGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    private static MuscleGroup safeMuscleGroup(String name) {
        try {
            return MuscleGroup.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException ex) {
            return null;
        }
    }
}
