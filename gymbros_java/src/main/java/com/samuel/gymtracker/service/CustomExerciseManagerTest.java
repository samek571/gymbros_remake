package com.samuel.gymtracker.service;
import com.samuel.gymtracker.model.MuscleGroup;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class CustomExerciseManagerTest {

    @Test
    void aaaa() {
        Set<MuscleGroup> muscles = CustomExerciseManager.parsePrimaryMuscles("lats biceps");
        assertTrue(muscles.contains(MuscleGroup.LATS));
        assertTrue(muscles.contains(MuscleGroup.BICEPS));
        assertEquals(2, muscles.size());
    }

    @Test
    void ads() {
        Set<MuscleGroup> muscles = CustomExerciseManager.parsePrimaryMuscles("core");
        assertTrue(muscles.contains(MuscleGroup.CORE));
        assertEquals(1, muscles.size());
    }

    @Test
    void asdd() {
        assertThrows(IllegalArgumentException.class, () -> {
            CustomExerciseManager.parsePrimaryMuscles("banana apple");
        });
    }

    @Test
    void qweasd() {
        Set<MuscleGroup> muscles = CustomExerciseManager.parseSecondaryMuscles("biceps picovina triceps");
        assertTrue(muscles.contains(MuscleGroup.BICEPS));
        assertTrue(muscles.contains(MuscleGroup.TRICEPS));
        assertEquals(2, muscles.size());
    }

    @Test
    void qsweasd() {
        Set<MuscleGroup> muscles = CustomExerciseManager.parseSecondaryMuscles("piceps picovina tricepz");
        assertEquals(0, muscles.size());
    }

    @Test
    void rasd() {
        Set<MuscleGroup> muscles = CustomExerciseManager.parseSecondaryMuscles("");
        assertEquals(0, muscles.size());
    }

    @Test
    void rasdasd() {
        Set<MuscleGroup> muscles = CustomExerciseManager.parseSecondaryMuscles(" ");
        assertEquals(0, muscles.size());
    }
}
