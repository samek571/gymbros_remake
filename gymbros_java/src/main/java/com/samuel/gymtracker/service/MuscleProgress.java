package com.samuel.gymtracker.service;


/**
 * tracks aggregated progress for a muscle group during workouts
 * stores cumulative counts of repetitions, time in seconds, number of distinct logged ex entries
 */
public class MuscleProgress {
    /**
     * Private constructor to prevent instantiation.
     */
    public MuscleProgress() {}

    private int totalReps = 0;
    private int totalSeconds = 0;
    private int exercisesLogged = 0;

    /**
     * adds data from new exercise log to this progress tracker
     * @param reps number of reps
     * @param seconds duration in seconds
     */
    public void add(int reps, int seconds) {
        this.totalReps += reps;
        this.totalSeconds += seconds;
        this.exercisesLogged += 1;
    }


    /**
     * totalReps
     * @return total number of reps logged
     */
    public int getTotalReps() { return totalReps; }
    /**
     * totalSeconds
     * @return total number of seconds logged
     */
    public int getTotalSeconds() { return totalSeconds; }
    /**
     * exercisesLogged
     * @return number of individual ex entries logged
     */
    public int getExercisesLogged() { return exercisesLogged; }
}
