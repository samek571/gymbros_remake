package com.samuel.gymtracker.service;

import com.samuel.gymtracker.model.Exercise;
import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.model.MuscleGroup;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;



class CustomTemplateManagerTest {

    private static final Path FILE = Path.of("src/main/resources/custom_templates.json");

    private ExerciseCatalog mockCatalogWithDefaults() {
        ExerciseCatalog catalog = new ExerciseCatalog();
        catalog.addCustomExercise(new Exercise("Test 1", Set.of(MuscleGroup.BICEPS), Set.of(), 10, 1.0, 0));
        catalog.addCustomExercise(new Exercise("Test 2", Set.of(MuscleGroup.TRICEPS), Set.of(), 10, 1.0, 0));
        catalog.addCustomExercise(new Exercise("Test 3", Set.of(MuscleGroup.LATS), Set.of(), 10, 1.0, 0));
        catalog.addCustomExercise(new Exercise("Test 4", Set.of(MuscleGroup.GLUTES), Set.of(), 10, 1.0, 0));
        catalog.addCustomExercise(new Exercise("Test 5", Set.of(MuscleGroup.CORE), Set.of(), 10, 1.0, 0));
        return catalog;
    }

    @Test
    void sdgfsf() {
        ExerciseCatalog catalog = mockCatalogWithDefaults();
        Scanner sc = new Scanner("""
            MyRoutine
            Test 1
            Test 2
            Test 3
            Test 4
            Test 5
            done
            """);

        CustomTemplateManager.createTemplate(catalog, sc);

        Map<String, List<Exercise>> templates = CustomTemplateManager.getAll();
        assertTrue(templates.containsKey("MyRoutine"));
        assertEquals(5, templates.get("MyRoutine").size());
        List<Exercise> materialized = CustomTemplateManager.materialise("MyRoutine", catalog);
        assertEquals(5, materialized.size());
        assertEquals("Test 1", materialized.getFirst().getName());
    }


    @Test
    void adsaddt43() {
        ExerciseCatalog catalog = mockCatalogWithDefaults();
        Scanner scAdd = new Scanner("""
                TempRoutine
                Test 1
                Test 2
                Test 3
                Test 4
                Test 5
                done
                """);
        CustomTemplateManager.createTemplate(catalog, scAdd);

        assertTrue(CustomTemplateManager.getAll().containsKey("TempRoutine"));

        Scanner scDel = new Scanner("TempRoutine\n");
        CustomTemplateManager.deleteTemplate(scDel);

        assertFalse(CustomTemplateManager.getAll().containsKey("TempRoutine"));
    }


    @Test
    void q21wde() {
        ExerciseCatalog catalog = mockCatalogWithDefaults();
        Scanner sc = new Scanner("""
                RoutineA
                Test 1
                Test 2
                Test 3
                Test 4
                Test 5
                done
                """);

        CustomTemplateManager.createTemplate(catalog, sc);
        Map<String, List<Exercise>> all = CustomTemplateManager.getAll();
        assertEquals(1, all.size());
        assertTrue(all.containsKey("RoutineA"));
    }

    @Test
    void fafqwer3() throws IOException {
        Files.writeString(FILE, "{ invalid json");
        ExerciseCatalog dummyCatalog = new ExerciseCatalog();
        CustomTemplateManager.init(dummyCatalog);
        Map<String, List<Exercise>> data = CustomTemplateManager.getAll();
        assertTrue(data.isEmpty(), "Should fall back to empty map on parse error");
    }

}
