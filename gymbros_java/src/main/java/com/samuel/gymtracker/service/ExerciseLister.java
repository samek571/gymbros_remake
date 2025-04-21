package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.Exercise;
import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.model.MuscleGroup;
import java.util.Scanner;

public final class ExerciseLister {
    private ExerciseLister() {}

    private static final Scanner scanner = new Scanner(System.in);
    public static void listAllExercises(ExerciseCatalog catalog) {
        System.out.println("\n=== All Exercises ===");
        for (Exercise exercise : catalog.all()) {
            System.out.println("- " + exercise.getName());
        }
    }

    public static void listExercisesForMuscle(ExerciseCatalog catalog) {
        System.out.print("Enter muscle group: ");
        String muscleInput = scanner.nextLine().trim().toUpperCase();
        MuscleGroup targetMuscle;

        try {
            targetMuscle = MuscleGroup.valueOf(muscleInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Not tracking that muscle group...: " + muscleInput);
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
