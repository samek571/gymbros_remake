package com.samuel.gymtracker.util;

/**
 * util class for konsole operations - clearing screen and sleeping
 * non-instantiable class, provides static helper methods to interact with the terminal
 */
public class ConsoleUtils {

    /**
     * private constructor preventing instantiation
     */
    private ConsoleUtils() {}


    /**
     * clears konsole screen using ANSI codes
     * designed for Unix-like terminals only!
     */
    public static void clearConsole() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * pauses current thread for some time
     * @param millis to sleep
     */
    public static void sleep(long millis) {
        try {Thread.sleep(millis);}
        catch (InterruptedException ignored) {} //thread interruption ignored without notif
    }
}
