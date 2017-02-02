package com.astro.api;

import java.time.ZonedDateTime;
import java.util.Map;

/**
 * Created by rohan on 2/2/17.
 */
public interface Message {
    // Observers

    Person getPublisher();

    /**
     * Get the published time of the current content of this message
     *
     * @return published time of this message
     */
    ZonedDateTime getPublishedTime();

    /**
     * Get the current content of this message
     * Current content of this message is the most recent content in
     * the edit history of this message
     *
     * Content of the message is a non empty string of characters
     *
     * @return the current content of this message
     */
    String getContent();

    // Adding functionality to update the message
    // Making this a mutable data type will add complexity

    /**
     * Get edit history of the message.
     * Edit history is a mapping from time to the content of the message updated at that time
     *
     * @return edit history of the message
     */
    Map<ZonedDateTime, String> getEditHistory();

    // Mutators

    /**
     * Edit the message by replacing the current content with the new content
     *
     * @param editedMessage a new message to replace the current content of this message
     * @throws IllegalArgumentException if the editedMessage is empty
     */
    void update(String editedMessage);
}
