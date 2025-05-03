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


/**
 * repository for storing and retrieving workout session data in regards to disk
 * JSON ser via Jackson
 */
public final class WorkoutRepository {
    private WorkoutRepository() {}
    private static final ObjectMapper _mapper = new ObjectMapper().findAndRegisterModules();


    /**
     * loads workout history from JSON
     * @param file path to JSON
     * @return loaded WorkoutHistory object
     * @throws IOException file reading or parsing failed
     */
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


    /**
     * saves workout history to JSON
     * @param file path to JSON
     * @param history workout history object to save
     * @throws IOException writing fails
     */
    public static void saveHistory(Path file, WorkoutHistory history) throws IOException {
        Files.createDirectories(file.getParent());
        try (OutputStream out = Files.newOutputStream(file)) {
            _mapper.writerWithDefaultPrettyPrinter().writeValue(out, history.getSessions());
        }
    }

    /**
     * convenience method to append a single session saved history
     * automatically loads and updates `workout_history.json`
     * @param session to persist
     */
    public static void saveSession(WorkoutSession session) {
        try {
            Path file = Path.of("src/main/resources/workout_history.json");
            WorkoutHistory history;
            if (Files.exists(file)) {
                history = loadHistory(file);
            } else {
                history = new WorkoutHistory();
            }
            history.addSession(session);
            saveHistory(file, history);
        } catch (IOException e) {
            System.out.println("Error saving: " + e.getMessage());
        }
    }

}
