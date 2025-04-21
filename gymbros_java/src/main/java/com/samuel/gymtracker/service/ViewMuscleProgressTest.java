package com.samuel.gymtracker.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ViewMuscleProgressTest {

    private static final Path FILE = Path.of("src/main/resources/muscle_progress.json");

    @BeforeEach
    void cleanBefore() throws Exception {
        Files.deleteIfExists(FILE);
    }

    @Test
    void empt() {
        ViewMuscleProgress.display();
        assertTrue(true);
    }

    @Test
    void ok() throws Exception {
        var progress = new TreeMap<String, MuscleProgress>();
        var mp = new MuscleProgress();
        mp.add(50, 300);
        progress.put("CHEST", mp);

        Files.createDirectories(FILE.getParent());
        var _mapper = new com.fasterxml.jackson.databind.ObjectMapper().findAndRegisterModules();
        _mapper.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), progress);

        ViewMuscleProgress.display();
        assertTrue(true);
    }

    @Test
    void ok_long() throws Exception {
        var progress = new TreeMap<String, MuscleProgress>();

        var chest = new MuscleProgress();
        chest.add(50, 100);

        var biceps = new MuscleProgress();
        biceps.add(30, 20);

        var triceps = new MuscleProgress();
        triceps.add(10, 40);

        var abs = new MuscleProgress();
        biceps.add(80, 120);

        progress.put("CHEST", chest);
        progress.put("BICEPS", biceps);
        progress.put("TRICEPS", triceps);
        progress.put("ABS", abs);

        Files.createDirectories(FILE.getParent());
        var _mapper = new ObjectMapper().findAndRegisterModules();
        _mapper.writerWithDefaultPrettyPrinter().writeValue(FILE.toFile(), progress);
        ViewMuscleProgress.display();
        assertTrue(true);
    }

}
