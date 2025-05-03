package com.samuel.gymtracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;


/**
 * represents single exercise + targeted muscle groups and XP computation coeffs (which are wildely tuned - I am not paid for this shit luckily)
 * immutable and serializable via Jackson -> persistence
 */
public final class Exercise {
    private final String name; //exercise name
    private final Set<MuscleGroup> primaryMuscles; //primary muscle group for exercise
    private final Set<MuscleGroup> secondaryMuscles; //secondary muscle group for exercise
    private final double repsCoeff; //XP coefff for repetition
    private final double weightCoeff; //XP coeff for weight lifted
    private final double timeCoeff; //XP coeff for time doing the ex.

    /**
     * Creates a new Exercise instance. Must include at least one primary muscle group.
     *
     * @param name name of the exercise
     * @param primaryMuscles set of primary muscle groups
     * @param secondaryMuscles optional secondary muscle groups
     * @param repsCoeff XP multiplier per repetition
     * @param weightCoeff XP multiplier per kg
     * @param timeCoeff XP multiplier per second
     * @throws IllegalArgumentException if no primary muscle is specified || typo
     */
    @JsonCreator
    public Exercise(@JsonProperty("name") String name,
                    @JsonProperty("primary") Set<MuscleGroup> primaryMuscles,
                    @JsonProperty("secondary") Set<MuscleGroup> secondaryMuscles,
                    @JsonProperty("repsCoeff") double repsCoeff,
                    @JsonProperty("weightCoeff") double weightCoeff,
                    @JsonProperty("timeCoeff") double timeCoeff) {

        this.name = Objects.requireNonNull(name);

        if (primaryMuscles == null || primaryMuscles.isEmpty()) {
            throw new IllegalArgumentException("at least one primary muscle must be written: " + name);
        }

        this.primaryMuscles = Collections.unmodifiableSet(EnumSet.copyOf(primaryMuscles));

        if (secondaryMuscles == null || secondaryMuscles.isEmpty()) {
            this.secondaryMuscles = Collections.emptySet();
        } else {
            this.secondaryMuscles = Collections.unmodifiableSet(EnumSet.copyOf(secondaryMuscles));
        }

        this.repsCoeff = repsCoeff;
        this.weightCoeff = weightCoeff;
        this.timeCoeff = timeCoeff;
    }

    /**
     * name of the exercise
     * @return name of the exercise
     */
    public String getName() { return name; }
    /**
     * primary muscles ex
     * @return set of primary muscle groups targeted by ex
     */
    public Set<MuscleGroup> getPrimary() { return primaryMuscles; }
    /**
     * secondary muscles ex
     * @return set of secondary muscle groups targeted by ex
     */
    public Set<MuscleGroup> getSecondary() { return secondaryMuscles; }

    /**
     * ex name to str
     * @return name of ex as str
     */
    @Override
    public String toString() { return name; }

    /**
     * XP score calc particular exercise via weight formula

     * @param reps number of repetitions
     * @param kg weight lifted in kg
     * @param seconds duration in seconds
     * @param profileFactor user-specific weight multiplier - fat/skinny
     * @return total XP earned per set
     */
    public double ScoreCalculator(int reps, double kg, int seconds, double profileFactor) {
        return (reps * repsCoeff) + (kg * weightCoeff * profileFactor) + (seconds * timeCoeff);
    }
}
