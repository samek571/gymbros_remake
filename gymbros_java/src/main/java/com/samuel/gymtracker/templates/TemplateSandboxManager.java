package com.samuel.gymtracker.templates;

import com.samuel.gymtracker.model.ExerciseCatalog;
import com.samuel.gymtracker.service.CustomTemplateManager;

import java.util.Scanner;

public final class TemplateSandboxManager {
    private TemplateSandboxManager() {}

    public static void open(ExerciseCatalog catalog, Scanner scanner) {
        boolean loop = true;
        while (loop) {
            System.out.println("\n--- Template Sandbox ---");
            System.out.println("1  List custom templates");
            System.out.println("2  Create new template");
            System.out.println("3  Delete template");
            System.out.println("4  Back");
            System.out.print("Choose: ");
            switch (scanner.nextLine().trim()) {
                case "1" -> CustomTemplateManager.listTemplates();
                case "2" -> CustomTemplateManager.createTemplate(catalog, scanner);
                case "3" -> CustomTemplateManager.deleteTemplate(scanner);
                case "4" -> loop = false;
                default  -> System.out.println("Invalid.");
            }
        }
    }
}
