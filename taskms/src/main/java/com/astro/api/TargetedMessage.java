package com.astro.api;

import java.util.List;

/**
 * Created by rohan on 2/2/17.
 */
public interface TargetedMessage extends Message {
    public enum Status {
        NOTREAD,
        READ,
        RESOLVED,
        UNRESOLVED
    }

    /**
     * Get the status of this issue
     * @return status of this issue
     */
    TargetedMessage.Status getStatus();

    /**
     * Change the status of this message.
     * Status of this message can only be changed in sequence like
     * UNREAD -> READ -> UNRESOLVED -> RESOLVED
     * Whereas READ as UNRESOLVED can be skipped
     * For example:
     *  UNREAD -true-> READ -false-> UNREAD -true-> RESOLVED -false-> UNRESOLVED
     * @param newStatus status to be set
     * @return true if status of this task change
     */
    boolean setStatus(Status newStatus);

    /**
     * Get the list of persons responsible for this Targeted Message
     * @return
     */
    List<Person> getResponsiblePersons();
}
