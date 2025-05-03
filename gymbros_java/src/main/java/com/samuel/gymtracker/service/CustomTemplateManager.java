package com.samuel.gymtracker.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.gymtracker.model.Exercise;
import com.samuel.gymtracker.model.ExerciseCatalog;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public final class CustomTemplateManager {
    private CustomTemplateManager() {}

    private static final Path FILE = Path.of("src/main/resources/custom_templates.json");
    private static final ObjectMapper MAPPER = new ObjectMapper().findAndRegisterModules();
    private static final Map<String, List<Exercise>> templates = new TreeMap<>();

    public static void init(ExerciseCatalog catalog) {
        templates.clear();
        if (!Files.exists(FILE)) return;

        try (InputStream in = Files.newInputStream(FILE)) {
            Map<String, List<String>> namesOnly = MAPPER.readValue(in, new TypeReference<>() {});
            for (var entry : namesOnly.entrySet()) {
                List<Exercise> exercises = new ArrayList<>();
                for (String name : entry.getValue()) {
                    Exercise ex = catalog.get(name);
                    if (ex != null) exercises.add(ex);
                }
                templates.put(entry.getKey(), exercises);
            }
        } catch (IOException e) {
            System.out.println("Failed to load custom templates: " + e.getMessage());
        }
    }

    public static void save() {
        Map<String, List<String>> export = new TreeMap<>();
        for (var entry : templates.entrySet()) {
            List<String> names = entry.getValue().stream()
                    .map(Exercise::getName)
                    .toList();
            export.put(entry.getKey(), names);
        }

        try {
            Files.createDirectories(FILE.getParent());
            try (OutputStream out = Files.newOutputStream(FILE)) {
                MAPPER.writerWithDefaultPrettyPrinter().writeValue(out, export);
            }
        } catch (IOException e) {
            System.out.println("Error saving templates: " + e.getMessage());
        }
    }

    public static void listTemplates() {
        if (templates.isEmpty()) {
            System.out.println("No custom templates saved.");
            return;
        }

        System.out.println("Saved templates:");
        for (var name : templates.keySet()) {
            System.out.println("- " + name);
        }
    }

    public static void createTemplate(ExerciseCatalog catalog, Scanner scanner) {
        System.out.print("Enter name of the new template: ");
        String name = scanner.nextLine().trim();
        if (name.isEmpty()) {
            System.out.println("Template has to be non-empty.");
            return;
        }

        List<Exercise> list = new ArrayList<>();
        System.out.println("Enter exercise names for the template. Press ENTER to finish.");
        while (true) {
            System.out.print("Exercise name: ");
            if (!scanner.hasNextLine()) break;

            String exName = scanner.nextLine().trim();
            if (exName.equalsIgnoreCase("done") || exName.isEmpty()) break;

            Exercise ex = catalog.get(exName);
            if (ex == null) {
                System.out.println("Exercise not found, skipping.");
                continue;
            }
            list.add(ex);
        }

        if (list.isEmpty()) {
            System.out.println("No valid exercises provided. Halting.");
            return;
        }

        templates.put(name, list);
        save();
        System.out.println("Template '" + name + "' created with " + list.size() + " exercises.");
    }


    public static void deleteTemplate(Scanner scanner) {
        System.out.print("Enter name of the template to delete: ");
        String name = scanner.nextLine().trim();
        if (templates.remove(name) != null) {
            save();
            System.out.println("Deleted.");
        } else {
            System.out.println("Template not found.");
        }
    }

    public static Map<String, List<Exercise>> getAll() {
        return Collections.unmodifiableMap(templates);
    }


    public static List<Exercise> materialise(String myRoutine, ExerciseCatalog catalog) {
        List<Exercise> exerciseNames = templates.get(myRoutine);
        if (exerciseNames == null || exerciseNames.isEmpty()) return List.of();

        List<Exercise> collect = exerciseNames.stream()
                .map((Exercise name) -> catalog.get(String.valueOf(name)))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return Collections.unmodifiableList(collect);
    }

}
