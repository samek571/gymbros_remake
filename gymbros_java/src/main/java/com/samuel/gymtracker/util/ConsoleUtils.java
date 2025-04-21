package com.samuel.gymtracker.util;

public class ConsoleUtils {
    private ConsoleUtils() {}

    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }
}
