package com.samuel.gymtracker.model.session;

import com.samuel.gymtracker.model.Exercise;
import com.samuel.gymtracker.model.MuscleGroup;
import com.samuel.gymtracker.service.WorkoutRepository;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class WorkoutIntegrationTest {
    @Test
    void quick_bench_test() throws Exception {
        Exercise ex = new Exercise(
                "Bench Press",
                Set.of(MuscleGroup.UPPER_CHEST, MuscleGroup.TRICEPS),
                Set.of(MuscleGroup.FRONT_DELT),
                10.0, 1.2, 0.0);

        WorkoutSession session = new WorkoutSession();
        session.addEntry(SessionEntry.of(ex, 2, 120.0, 0, 1.0));
        session.finish();

        Path tempHistoryFile = Path.of("src/main/resources/workout_history_test.json");

        if (Files.exists(tempHistoryFile)) {
            Files.delete(tempHistoryFile);
        }

        WorkoutHistory history = new WorkoutHistory();
        history.addSession(session);
        WorkoutRepository.saveHistory(tempHistoryFile, history);
        assertTrue(Files.exists(tempHistoryFile));
        assertTrue(Files.size(tempHistoryFile) > 10);
    }
}
