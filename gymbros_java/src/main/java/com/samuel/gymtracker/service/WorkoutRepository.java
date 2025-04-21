package com.samuel.gymtracker.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.gymtracker.model.session.WorkoutHistory;
import com.samuel.gymtracker.model.session.WorkoutSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class WorkoutRepository {
    private WorkoutRepository() {}

    private static final ObjectMapper _mapper = new ObjectMapper().findAndRegisterModules();
    public static WorkoutHistory loadHistory(Path file) throws IOException {
        WorkoutHistory history = new WorkoutHistory();
        if (!Files.exists(file)) return history;

        try (InputStream in = Files.newInputStream(file)) {
            List<WorkoutSession> sessions = _mapper.readValue(in, new TypeReference<>() {});
            for (var s : sessions) {
                history.addSession(s);
            }
        }
        return history;
    }

    public static void saveHistory(Path file, WorkoutHistory history) throws IOException {
        Files.createDirectories(file.getParent());
        try (OutputStream out = Files.newOutputStream(file)) {
            _mapper.writerWithDefaultPrettyPrinter().writeValue(out, history.getSessions());
        }
    }
}
