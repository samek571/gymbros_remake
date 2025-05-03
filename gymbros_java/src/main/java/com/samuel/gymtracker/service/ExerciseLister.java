package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.Exercise;
import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.model.MuscleGroup;
import java.util.Scanner;


/**
 * utility class for listing exxs from the catalog
 * allows users to list all available ex or filter by targeted muscle group
 */
public final class ExerciseLister {
    /**
     * private constructor preventing instantiation
     */
    private ExerciseLister() {}

    private static final Scanner scanner = new Scanner(System.in);

    /**
     * displays all exxs in catalog
     * @param catalog ex catalog to retrieve data from
     */
    public static void listAllExercises(ExerciseCatalog catalog) {
        System.out.println("\n=== All Exercises ===");
        for (Exercise exercise : catalog.all()) {
            System.out.println("- " + exercise.getName());
        }
    }

    /**
     * user inputs muscle group and gets listed exercises that hit given muscle
     * @param catalog ex catalog to search inside it
     */
    public static void listExercisesForMuscle(ExerciseCatalog catalog) {
        System.out.print("Enter muscle group: ");
        String muscleInput = scanner.nextLine().trim().toUpperCase();
        MuscleGroup targetMuscle;

        try {
            targetMuscle = MuscleGroup.valueOf(muscleInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Not supporting that muscle group, try again: " + muscleInput);
            return;
        }

        boolean exists = false;
        System.out.println("\n=== Exercises targeting " + targetMuscle + " ===");
        for (Exercise exercise : catalog.all()) {
            if (exercise.getPrimary().contains(targetMuscle) || exercise.getSecondary().contains(targetMuscle)) {
                System.out.println("- " + exercise.getName());
                exists = true;
            }
        }
        if (!exists) {
            System.out.println("No exercise fount for this muscle.");
        }
    }
}
