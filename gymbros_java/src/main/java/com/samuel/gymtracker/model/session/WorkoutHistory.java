package com.samuel.gymtracker.model.session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * represents users history of workout sessions
 * stores ordered list of completed {@link WorkoutSession}s
 */
public final class WorkoutHistory {
    /**
     * Private constructor to prevent instantiation.
     */
    public WorkoutHistory() {}

    //storage
    private final List<WorkoutSession> sessions = new ArrayList<>();

    /**
     * adds new session to workout history
     * @param session completed workout session
     */
    public void addSession(WorkoutSession session) {
        sessions.add(session);
    }

    /**
     * retrieves an unmodifiable list of all past workout sessions
     * @return list of recorded workout sessions
     */
    public List<WorkoutSession> getSessions() {
        return Collections.unmodifiableList(sessions);
    }
}
