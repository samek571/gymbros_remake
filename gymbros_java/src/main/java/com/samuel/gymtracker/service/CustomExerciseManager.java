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


/**
 * provides CLI utilities to manage user defined exs
 * includes options to create and delete ex
 */
public final class CustomExerciseManager {
    /**
     * private constructor preventing instantiation
     */
    private CustomExerciseManager() {}


    private static final Scanner scanner = new Scanner(System.in);


    /**
     * deletes custom exercise by name
     * saves updated catalog to the file path
     * @param catalog catalog to modify
     * @param savePath file path where the updated catalog will be saved
     */
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

    /**
     * prompts user to enter data for new exercise, then saves it to the catalog and disk
     * @param catalog ex catalog to insert new exercise there
     */
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

    /**
     * parses set of muscle group names from input for primary muscles
     * throws if no valid muscles are provided
     *
     * @param input raw user input string
     * @return set of primary muscle groups
     */
    public static Set<MuscleGroup> parsePrimaryMuscles(String input) {
        Set<MuscleGroup> muscles = Arrays.stream(input.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> MuscleGroup.valueOf(s.toUpperCase()))
                .collect(Collectors.toSet());

        if (muscles.isEmpty()) {throw new IllegalArgumentException("at least one primary muscle is being targeted... fix this shitty input");}

        return muscles;
    }


    /**
     * parses a set of optional secondary muscle groups from input
     * invalid names are skipped
     * @param input raw user input string
     * @return set of secondary muscle groups
     */
    public static Set<MuscleGroup> parseSecondaryMuscles(String input) {
        return Arrays.stream(input.split(" "))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(CustomExerciseManager::safeMuscleGroup)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }


    /**
     * attempts to safely resolve muscle group enum from string
     * returns null if parsing fails
     * @param name muscle name string
     * @return corresponding MuscleGroup or null
     */
    private static MuscleGroup safeMuscleGroup(String name) {
        try {return MuscleGroup.valueOf(name.toUpperCase());}
        catch (IllegalArgumentException ex) {return null;}
    }
}
