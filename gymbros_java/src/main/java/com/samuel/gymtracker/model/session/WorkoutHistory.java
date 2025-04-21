package com.samuel.gymtracker.model.session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WorkoutHistory {
    //storage
    private final List<WorkoutSession> sessions = new ArrayList<>();

    public void addSession(WorkoutSession session) {
        sessions.add(session);
    }

    public List<WorkoutSession> getSessions() {
        return Collections.unmodifiableList(sessions);
    }
}
