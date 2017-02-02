package com.astro.api;

import java.time.ZonedDateTime;
import java.util.List;

/**
 * Created by rohan on 2/2/17.
 */
public interface Task extends TargetedMessage{

    /**
     * Set the deadline for this task
     * @param deadline the deadline of this task
     * @throws IllegalArgumentException if the deadline to be set is behind the moment the method is invoked (exclusive)
     */
    void setDeadline(ZonedDateTime deadline);

    /**
     * Get the deadline of this task
     * @return deadline of this task
     */
    ZonedDateTime getDeadline();

    /**
     * Check if the Task deadline has been passed
     * The deadline is passed if it's behind the moment the method is invoked (exclusive)
     * @return true if the deadline has been passed
     */
    boolean isOverDue();
}
