package com.samuel.gymtracker.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

public final class Exercise {
    private final String name;

    //will be used later i just hate to think of the design later
    private final Set<MuscleGroup> primaryMuscles;
    private final Set<MuscleGroup> secondaryMuscles;
    private final double repsCoeff;
    private final double kgCoeff;
    private final double timeCoeff;

    //fucking camel case, the only camel i respect is toe and cigars, not the fucking typing
    @JsonCreator
    public Exercise(@JsonProperty("name") String name,
                    @JsonProperty("primary") Set<MuscleGroup> primaryMuscles,
                    @JsonProperty("secondary") Set<MuscleGroup> secondaryMuscles,
                    @JsonProperty("repsCoeff") double repsCoeff,
                    @JsonProperty("kgCoeff") double kgCoeff,
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
        this.kgCoeff = kgCoeff;
        this.timeCoeff = timeCoeff;
    }
    
    //essential for custom exe
    public String getName() { return name; }
    
    
    //not needing this yet, later for exercise once it is running
//    public Set<MuscleGroup> getPrimary() { return primaryMuscles; }
//    public Set<MuscleGroup> getSecondary() { return secondaryMuscles; }
//    public double computeScore(int reps, double kg, int seconds, double profileFactor) {
//        return (reps * repsCoeff) + (kg * kgCoeff * profileFactor) + (seconds * timeCoeff);
//    }

    @Override
    public String toString() { return name; }
}
