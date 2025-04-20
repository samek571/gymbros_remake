package com.samuel.gymtracker.model;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class ExerciseCatalog {
    private static final ObjectMapper _mapper = new ObjectMapper();
    private final Map<String, Exercise> builtIns = new ConcurrentHashMap<>();
    private final Map<String, Exercise> customs  = new ConcurrentHashMap<>();

    public void loadBuiltInsFromClasspath(String resourcePath) throws IOException {
        try (InputStream in = ExerciseCatalog.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new IOException("Resource not found: " + resourcePath);
            loadMap(in, builtIns);
        }
    }

    public void loadCustomFromFile(Path file) throws IOException {
        if (!Files.exists(file)) return;
        try (InputStream in = Files.newInputStream(file)) {
            var list = _mapper.readValue(in, new TypeReference<List<Exercise>>() {});
            for (Exercise ex : list) {
                var key = ex.getName().toLowerCase();
                customs.put(key, ex);
            }
        }
    }


    private static void loadMap(InputStream in, Map<String, Exercise> target) throws IOException {
        var list = _mapper.readValue(in, new TypeReference<List<Exercise>>() {});
        for (Exercise ex : list) {
            var key = ex.getName().toLowerCase();
            target.put(key, ex);
        }
    }

    public void addCustomExercise(Exercise exercise) {
        var key = exercise.getName().toLowerCase();
        if (builtIns.containsKey(key) || customs.putIfAbsent(key, exercise) != null) {
            throw new IllegalArgumentException("Exercise already exists: " + exercise.getName());
        }
    }

    public boolean removeCustomExercise(String name) {

        return customs.remove(name.toLowerCase()) != null;
    }

    public void saveCustomToFile(Path file) throws IOException {
        Files.createDirectories(file.getParent());
        List<Exercise> list = new ArrayList<>(customs.values());
        try (OutputStream out = Files.newOutputStream(file)) {
            _mapper.writerWithDefaultPrettyPrinter().writeValue(out, list);
        }
    }

    // Save ONE new exercise (append-like behavior logically)
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


    public Exercise get(String name) {
        var key = name.toLowerCase();
        Exercise ex = customs.get(key);
        return (ex != null) ? ex : builtIns.get(key);
    }

    public Set<Exercise> all() {
        Map<String, Exercise> merged = new HashMap<>(builtIns);
        merged.putAll(customs);
        return Collections.unmodifiableSet(new HashSet<>(merged.values()));
    }

    public boolean isBuiltIn(String name) {
        return builtIns.containsKey(name.toLowerCase());
    }
}
