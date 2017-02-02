package com.astro.api.message;

/**
 * Created by rohan on 2/2/17.
 */
public interface Issue extends Message {
    /**
     * Mark the status of this issue as Status.RESOLVED
     * @return true if the status of this issue changes
     */
    boolean markAsResolved();
}
