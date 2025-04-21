package com.samuel.gymtracker.model.session;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samuel.gymtracker.model.Exercise;

public final class SessionEntry {

    private final Exercise exercise;
    private final int reps;
    private final double weight;
    private final int seconds;
    private final double xp;

    @JsonCreator
    public SessionEntry(
            @JsonProperty("exercise") Exercise exercise,
            @JsonProperty("reps") int reps,
            @JsonProperty("weight") double weight,
            @JsonProperty("seconds") int seconds,
            @JsonProperty("xp") double xp
    ) {
        this.exercise = exercise;
        this.reps = reps;
        this.weight = weight;
        this.seconds = seconds;
        this.xp = xp;
    }

    public static SessionEntry of(Exercise ex, int reps, double weight, int seconds, double profileFactor) {
        if (reps < 0 || weight < 0 || seconds < 0)
            throw new IllegalArgumentException("only non-negative values allowed!");

        double xp = ex.ScoreCalculator(reps, weight, seconds, profileFactor);
        return new SessionEntry(ex, reps, weight, seconds, xp);
    }

    //useful getters for tests for now, necesarry for exercise
    public Exercise getExercise() { return exercise; }
    public int getReps() { return reps; }
    public double getWeight() { return weight; }
    public int getSeconds() { return seconds; }
    public double getXpEarned() { return xp; }

    @Override
    public String toString() {
        return exercise.getName() + "  reps=" + reps + "  weight=" + weight +
                "  sec=" + seconds + "  XP=" + xp;
    }
}
