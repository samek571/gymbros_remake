package com.samuel.gymtracker.model.session;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;



/**
 * represents a single workout session + start/end time + logged sets
 */
public final class WorkoutSession {

    private final UUID id;
    private final Instant startTime;
    private Instant endTime;
    private final List<SessionEntry> entries = new ArrayList<>();


    /**
     * new workout session innit at curr time
     */
    public WorkoutSession() {
        this.id = UUID.randomUUID();
        this.startTime = Instant.now();
    }

    /**
     * adds new set entry to the session
     * @param entry completed ex entry
     * @throws IllegalStateException session saved twice
     */
    public void addEntry(SessionEntry entry) {
        if (endTime != null) {
            throw new IllegalStateException("Cannot add sets after session is finished!");
        }
        entries.add(entry);
    }


    /**
     * marks session as completed + marks end time
     * @throws IllegalStateException if already marked as finished...
     */
    public void finish() {
        if (endTime != null) {
            throw new IllegalStateException("Session already finished.");
        }
        this.endTime = Instant.now();
    }


    /**
     * total sesh duration
     * @return sesh duration: end minus start
     * @throws IllegalStateException if session didnt end yet
     */
    public Duration getDuration() {
        if (endTime == null) {
            throw new IllegalStateException("Session is not finished yet.");
        }
        return Duration.between(startTime, endTime);
    }


    /**
     * computes total XP earned during this session
     * @return summing XP across all logged entries
     */
    public double getTotalXp() {return entries.stream().mapToDouble(SessionEntry::getXpEarned).sum();}

    /**
     * unmodifiable list of all set entries in particilaar session
     * @return List of {@link SessionEntry}s.
     */
    public List<SessionEntry> getEntries() {
        return Collections.unmodifiableList(entries);
    }

    /**
     * time at which we started
     * @return starting time
     */
    public Instant getStartTime() {return startTime;}

    @Override
    public String toString() {
        return "WorkoutSession{" +
                "id=" + id +
                ", start=" + startTime +
                ", end=" + endTime +
                ", sets=" + entries.size() +
                ", totalXp=" + getTotalXp() +
                '}';
    }
}

