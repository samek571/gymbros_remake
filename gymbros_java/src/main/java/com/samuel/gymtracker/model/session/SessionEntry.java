package com.samuel.gymtracker.model.session;

import com.samuel.gymtracker.model.Exercise;

public final class SessionEntry {

    private final Exercise exercise;
    private final int reps;
    private final double weight;
    private final int time;
    private final double xp;

    private SessionEntry(Exercise ex, int reps, double weight, int time, double profileFactor) {
        this.exercise = ex;
        this.reps = reps;
        this.weight = weight;
        this.time = time;
        this.xp = (reps * ex.getRepsCoeff()) + (weight*ex.getWeightCoeff() * profileFactor) + (time * ex.getTimeCoeff());
    }

    public static SessionEntry of(Exercise ex, int reps, double weight, int time, double profileFactor)
    {
        if (reps < 0 || weight < 0 || time < 0)
            throw new IllegalArgumentException("only non negative values!");

        double xp = ex.ScoreCalculator(reps, weight, time, profileFactor);
        return new SessionEntry(ex, reps, weight, time, xp);
    }

    //useful getters for tests for now, necesarry for exercise
    public Exercise getExercise() { return exercise; }
    public int getReps() { return reps; }
    public double getWeight() { return weight; }
    public int getSeconds() { return time; }
    public double getXpEarned() { return xp; }

    @Override public String toString() {
        return exercise.getName() + "  reps=" + reps + "  weight=" + weight +
                "  sec=" + time + "  XP=" + xp;
    }
}


