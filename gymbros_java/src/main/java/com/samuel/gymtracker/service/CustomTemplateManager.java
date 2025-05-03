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

/**
 * handles creation, loading, deletion, and persistence of user workout template
 * templates are stored as mapping from template name to list of exxs
 */
public final class CustomTemplateManager {
    /**
     * private constructor preventing instantiation
     */
    private CustomTemplateManager() {}
    
    
    private static final Path _file = Path.of("src/main/resources/custom_templates.json");
    private static final ObjectMapper _mapper = new ObjectMapper().findAndRegisterModules();
    private static final Map<String, List<Exercise>> templates = new TreeMap<>();


    /**
     * template map init by loading saved data from disk
     * exs are resolved using provided catalog
     * @param catalog ex catalog resolves exercise names
     */
    public static void init(ExerciseCatalog catalog) {
        templates.clear();
        if (!Files.exists(_file)) return;

        try (InputStream in = Files.newInputStream(_file)) {
            Map<String, List<String>> namesOnly = _mapper.readValue(in, new TypeReference<>() {});
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

    /**
     * persists current state of all custom templates to JSON
     */
    public static void save() {
        Map<String, List<String>> export = new TreeMap<>();
        for (var entry : templates.entrySet()) {
            List<String> names = entry.getValue().stream()
                    .map(Exercise::getName)
                    .toList();
            export.put(entry.getKey(), names);
        }

        try {
            Files.createDirectories(_file.getParent());
            try (OutputStream out = Files.newOutputStream(_file)) {
                _mapper.writerWithDefaultPrettyPrinter().writeValue(out, export);
            }
        } catch (IOException e) {
            System.out.println("Error saving templates: " + e.getMessage());
        }
    }

    /**
     * lists all custom template names in memory righnow
     */
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

    /**
     * prompts user to create new template
     * exercises are resolved from catalog
     * empty or shitty are skipped
     * @param catalog catalog used to resolve exercises
     * @param scanner scanner used for user input
     */
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


    /**
     * deletes a custom template by name, if it exists
     * @param scanner scanner used for reading template name
     */
    public static void deleteTemplate(Scanner scanner) {
        System.out.print("Enter name of the template to delete: ");
        String name = scanner.nextLine().trim();
        if (templates.remove(name) != null) {
            save();
            System.out.println("Template Deleted.");
        } else {
            System.out.println("Template not found.");
        }
    }

    /**
     * reeturns an unmodifiable view of all templates
     * @return map of template names to exercise lists
     */
    public static Map<String, List<Exercise>> getAll() {
        return Collections.unmodifiableMap(templates);
    }


    /**
     * resolves template name exxs using catalog
     * @param myRoutine template name to resolve
     * @param catalog exercise catalog used for name lookup
     * @return list of resolved exercises, empty if not found
     */
    public static List<Exercise> materialise(String myRoutine, ExerciseCatalog catalog) {
        List<Exercise> exerciseNames = templates.get(myRoutine);
        if (exerciseNames == null || exerciseNames.isEmpty()) return List.of();

        List<Exercise> collect = exerciseNames.stream()
                .map((Exercise name) -> catalog.get(String.valueOf(name))).filter(Objects::nonNull)
                .collect(Collectors.toList()); //idk if this is some error honestly

        return Collections.unmodifiableList(collect);
    }

}
