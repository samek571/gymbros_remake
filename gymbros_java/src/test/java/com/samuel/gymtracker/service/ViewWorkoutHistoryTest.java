package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.session.WorkoutHistory;
import com.samuel.gymtracker.model.session.WorkoutSession;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ViewWorkoutHistoryTest {
    private static final Path _file = Path.of("src/main/resources/workout_history_test.json");

    @Test
    void empty() throws IOException {
        Files.writeString(_file, "[]");
        WorkoutHistory history = WorkoutRepository.loadHistory(_file);
        assertTrue(history.getSessions().isEmpty());
        ViewWorkoutHistory.viewWorkoutHistory();
    }

    @Test
    void err() throws IOException {
        Files.writeString(_file, "{ this is not valid json }");
        ViewWorkoutHistory.viewWorkoutHistory();
    }

    @Test
    void normal() throws IOException {
        WorkoutHistory history = new WorkoutHistory();
        WorkoutSession session = new WorkoutSession();
        session.finish();
        history.addSession(session);

        WorkoutRepository.saveHistory(_file, history);
        ViewWorkoutHistory.viewWorkoutHistory();
    }
}
