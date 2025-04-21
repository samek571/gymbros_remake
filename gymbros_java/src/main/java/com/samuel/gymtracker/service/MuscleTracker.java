package com.samuel.gymtracker.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.gymtracker.model.MuscleGroup;
import com.samuel.gymtracker.model.session.SessionEntry;
import com.samuel.gymtracker.model.session.WorkoutSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;
import java.util.*;

public final class MuscleTracker {
    private MuscleTracker() {}

    private static final Path _file = Path.of("src/main/resources/muscle_progress.json");
    private static final ObjectMapper _mapper = new ObjectMapper().findAndRegisterModules();
    public static void updateProgress(WorkoutSession session) {
        try {
            Map<String, MuscleProgress> progress = loadProgress();

            for (SessionEntry entry : session.getEntries()) {
                var ex = entry.getExercise();
                var reps = entry.getReps();
                var seconds = entry.getSeconds();

                for (MuscleGroup mg : ex.getPrimary()) {
                    progress.computeIfAbsent(mg.name(), k -> new MuscleProgress())
                            .add(reps, seconds);
                }
                for (MuscleGroup mg : ex.getSecondary()) {
                    progress.computeIfAbsent(mg.name(), k -> new MuscleProgress())
                            .add(reps, seconds);
                }
            }

            saveProgress(progress);
        } catch (IOException e) {
            System.out.println("Failed updating muscle progress: " + e.getMessage());
        }
    }

    private static Map<String, MuscleProgress> loadProgress() throws IOException {
        if (!Files.exists(_file)) {
            Files.createDirectories(_file.getParent());
            return new HashMap<>();
        }
        try (InputStream in = Files.newInputStream(_file)) {
            return _mapper.readValue(in, new TypeReference<>() {});
        }
    }

    private static void saveProgress(Map<String, MuscleProgress> progress) throws IOException {
        try (OutputStream out = Files.newOutputStream(_file)) {
            _mapper.writerWithDefaultPrettyPrinter().writeValue(out, progress);
        }
    }
}
