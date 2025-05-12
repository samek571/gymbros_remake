package com.samuel.gymtracker.model.session;

import com.samuel.gymtracker.model.Exercise;
import com.samuel.gymtracker.model.MuscleGroup;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SessionEntryTest {
    @Test
    void reasf3ef() {
        Exercise ex = new Exercise(
                "Pull-up",
                Set.of(MuscleGroup.LATS, MuscleGroup.BICEPS),
                Set.of(MuscleGroup.UPPER_BACK),
                18.0, 0.0, 0.0);

        SessionEntry e = SessionEntry.of(ex, 8, 0, 0, 1.0);
        assertEquals(144.0, e.getXp(), 0.01);
    }

    @Test
    void qweas() {
        Exercise ex = new Exercise(
                "Plank",
                Set.of(MuscleGroup.ABDOMINALS),
                Set.of(),
                0.0, 0.0, 5.0);

        SessionEntry e = SessionEntry.of(ex, 0, 0, 0, 1.0);
        assertEquals(0.0, e.getXp(), 0.01);
    }

    @Test
    void dd() {
        Exercise ex = new Exercise(
                "Running",
                Set.of(MuscleGroup.CARDIO),
                Set.of(),
                0.0, 0.0, 2.5);

        SessionEntry e = SessionEntry.of(ex, 0, 0, 90, 1.0);
        assertEquals(225.0, e.getXp(), 0.01);
    }

    @Test
    void asd() {
        Exercise ex = new Exercise(
                "Wall Sit",
                Set.of(MuscleGroup.QUADRICEPS),
                Set.of(),
                5.0, 0.0, 2.0);

        SessionEntry e = SessionEntry.of(ex, 10, 0, 60, 1.0);
        double expectedXp = (10 * 5.0) + (60 * 2.0);
        assertEquals(expectedXp, e.getXp(), 0.01);
    }

    @Test
    void adsdd() {
        Exercise ex = new Exercise(
                "Squat",
                Set.of(MuscleGroup.QUADRICEPS, MuscleGroup.GLUTES),
                Set.of(MuscleGroup.LOWER_BACK),
                10.0, 1.0, 0.0);

        SessionEntry e = SessionEntry.of(ex, 5, 100.0, 0, 1.0);
        assertEquals("Squat", e.getExercise().getName());
        assertEquals(5, e.getReps());
        assertEquals(100.0, e.getWeight(), 0.01);
        assertEquals(0, e.getSeconds());
    }

}
