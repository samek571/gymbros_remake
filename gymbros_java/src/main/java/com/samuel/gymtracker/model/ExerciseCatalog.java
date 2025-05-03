package com.samuel.gymtracker.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.ConcurrentHashMap;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


/**
 * catalog of exercises + built in and user custom made exercises
 * loading/saving from JSON, lookup by name, duplication detection
 * allows lookup and management of {@link Exercise} instances by name
 */
public final class ExerciseCatalog {
    /**
     * Private constructor to prevent instantiation
     */
    public ExerciseCatalog() { }


    private static final ObjectMapper _mapper = new ObjectMapper();
    private final Map<String, Exercise> _builtIns = new ConcurrentHashMap<>();
    private final Map<String, Exercise> _customs  = new ConcurrentHashMap<>();


    /**
     * loads built in ex from a JSON file
     * @param resourcePath classpath to the built in JSON resource
     * @throws IOException if resource not fount or fucked
     */
    public void loadBuiltInsFromClasspath(String resourcePath) throws IOException {
        try (InputStream in = ExerciseCatalog.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new IOException("Resource not found: " + resourcePath);
            _loadMapHelper(in, _builtIns);
        }
    }

    private static void _loadMapHelper(InputStream in, Map<String, Exercise> target) throws IOException {
        var list = _mapper.readValue(in, new TypeReference<List<Exercise>>() {});
        for (Exercise ex : list) {
            var key = ex.getName().toLowerCase();
            target.put(key, ex);
        }
    }

    /**
     * loads custom ex from a JSON file on fs
     * @param file path to the custom JSON file
     * @throws IOException if file is fucked or not fount
     */
    public void loadCustomFromFile(Path file) throws IOException {
        if (!Files.exists(file)) return;
        try (InputStream in = Files.newInputStream(file)) {
            var list = _mapper.readValue(in, new TypeReference<List<Exercise>>() {});
            for (Exercise ex : list) {
                var key = ex.getName().toLowerCase();
                _customs.put(key, ex);
            }
        }
    }

    /**
     * adds new custom ex if not existing
     * @param exercise the exercise to add
     * @throws IllegalArgumentException if name already exists
     */
    public void addCustomExercise(Exercise exercise) {
        var key = exercise.getName().toLowerCase();
        if (_builtIns.containsKey(key) || _customs.putIfAbsent(key, exercise) != null) {
            throw new IllegalArgumentException("Exercise already exists: " + exercise.getName());
        }
    }


    /**
     * removes custom exercise characterized by name
     * @param name case-insensitive name
     * @return true if removed, else false
     */
    public boolean removeCustomExercise(String name) {
        return _customs.remove(name.toLowerCase()) != null;
    }

    /**
     * saves all custom exercises to a file, overwriting existing content
     * @param file output path
     * @throws IOException if write fails
     */
    public void saveCustomToFile(Path file) throws IOException {
        Files.createDirectories(file.getParent());
        List<Exercise> list = new ArrayList<>(_customs.values());
        try (OutputStream out = Files.newOutputStream(file)) {
            _mapper.writerWithDefaultPrettyPrinter().writeValue(out, list);
        }
    }

    /**
     * appending single new custom exercise to existing file
     * @param file path to custom JSON
     * @param exercise exercise to save
     * @throws IOException if file or write fails
     */
    public void saveCustomExercise(Path file, Exercise exercise) throws IOException {
        Files.createDirectories(file.getParent());
        List<Exercise> allExercises;

        if (Files.exists(file)) {
            // Load existing exercises
            try (InputStream in = Files.newInputStream(file)) {
                allExercises = _mapper.readValue(in, new TypeReference<List<Exercise>>() {});
            }
        } else {
            // No file exists yet
            allExercises = new ArrayList<>();
        }

        // Add new exercise
        allExercises.add(exercise);

        // Save updated list
        try (OutputStream out = Files.newOutputStream(file)) {
            _mapper.writerWithDefaultPrettyPrinter().writeValue(out, allExercises);
        }
    }

    /**
     * searches exercise by name, in both custom also built in
     * @param name case-insensitive name
     * @return the matching ex, or null if not fount
     */
    public Exercise get(String name) {
        var key = name.toLowerCase();
        Exercise ex = _customs.get(key);
        return (ex != null) ? ex : _builtIns.get(key);
    }

    /**
     * returns merged view of all exs
     * @return unmodifiable set of all exs
     */
    public Set<Exercise> all() {
        Map<String, Exercise> merged = new HashMap<>(_builtIns);
        merged.putAll(_customs);
        return Collections.unmodifiableSet(new HashSet<>(merged.values()));
    }
}
