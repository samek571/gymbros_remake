package com.samuel.gymtracker.service;

public class MuscleProgress {
    private int totalReps = 0;
    private int totalSeconds = 0;
    private int exercisesLogged = 0;

    public void add(int reps, int seconds) {
        this.totalReps += reps;
        this.totalSeconds += seconds;
        this.exercisesLogged += 1;
    }

    public int getTotalReps() { return totalReps; }
    public int getTotalSeconds() { return totalSeconds; }
    public int getExercisesLogged() { return exercisesLogged; }
}
