package com.samuel.gymtracker.model.session;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class WorkoutSession {

    private final UUID id;
    private final Instant startTime;
    private Instant endTime;
    private final List<SessionEntry> entries = new ArrayList<>();

    public WorkoutSession() {
        this.id = UUID.randomUUID();
        this.startTime = Instant.now();
    }

    public void addEntry(SessionEntry entry) {
        if (endTime != null) {
            throw new IllegalStateException("Cannot add sets after session is finished!");
        }
        entries.add(entry);
    }

    public void finish() {
        if (endTime != null) {
            throw new IllegalStateException("Session already finished.");
        }
        this.endTime = Instant.now();
    }

    public Duration getDuration() {
        if (endTime == null) {
            throw new IllegalStateException("Session is not finished yet.");
        }
        return Duration.between(startTime, endTime);
    }

    public double getTotalXp() {
        return entries.stream()
                .mapToDouble(SessionEntry::getXpEarned)
                .sum();
    }


    public Instant getStartTime() {
        return startTime;
    }

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

