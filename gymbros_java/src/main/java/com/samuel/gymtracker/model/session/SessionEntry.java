package com.samuel.gymtracker.model.session;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.samuel.gymtracker.model.Exercise;


/**
 * represents one logged set during particular workout that contains performed exs, reps, weight, duration and XP
 */
public final class SessionEntry {

    private final Exercise exercise;
    private final int reps;
    private final double weight;
    private final int seconds;
    private final double xp;


    /**
     * constructs a session entry to deserialize
     * @param exercise exercise performed
     * @param reps number of reps
     * @param weight weight kg
     * @param seconds ex duration in seconds
     * @param xp xp points earned
     */
    @JsonCreator
    public SessionEntry(
            @JsonProperty("exercise") Exercise exercise,
            @JsonProperty("reps") int reps,
            @JsonProperty("weight") double weight,
            @JsonProperty("seconds") int seconds,
            @JsonProperty("xpEarned") @JsonAlias("xp")double xp
    ) {
        this.exercise = exercise;
        this.reps = reps;
        this.weight = weight;
        this.seconds = seconds;
        this.xp = xp;
    }


    /**
     * Factory method to create a SessionEntry with XP calculation.
     * @param ex             exercise performed
     * @param reps           reps done
     * @param weight         (additional) weight
     * @param seconds        time spent
     * @param profileFactor  factor adjusting XP
     * @return valid SessionEntry
     */
    public static SessionEntry of(Exercise ex, int reps, double weight, int seconds, double profileFactor) {
        if (reps < 0 || weight < 0 || seconds < 0)
            throw new IllegalArgumentException("only non-negative values allowed!");

        double xp = ex.ScoreCalculator(reps, weight, seconds, profileFactor);
        return new SessionEntry(ex, reps, weight, seconds, xp);
    }

    /**
     * ex linked with session entry
     * @return the {@link Exercise} linked with session entry
     */
    public Exercise getExercise() { return exercise; }
    /**
     * number of reps performed
     * @return number of reps performed
     */
    public int getReps() { return reps; }
    /**
     * weight used in the exercise
     * @return weight used in the exercise in kg
     */
    public double getWeight() { return weight; }

    /**
     * duration in seconds
     * @return duration in seconds
     */
    public int getSeconds() { return seconds; }

    /**
     * xp yield
     * @return xp earned from ex
     */
    public double getXp() { return xp; }

    @Override
    public String toString() {
        return exercise.getName() + "  reps=" + reps + "  weight=" + weight +
                "  sec=" + seconds + "  XP=" + xp;
    }
}
