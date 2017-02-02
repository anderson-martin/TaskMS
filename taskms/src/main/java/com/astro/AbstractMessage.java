package com.astro;

import com.astro.api.Message;
import com.astro.api.Person;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by rohan on 2/2/17.
 */
abstract  class AbstractMessage implements Message {
    private ZonedDateTime recentContent;
    private Person publisher;
    private final Map<ZonedDateTime, String> editHistory;

    protected  AbstractMessage(Person publisher, String message) {
        if(message.isEmpty()) throw new IllegalArgumentException("a message cannot contain empty content");
        this.publisher = publisher;
        editHistory = new HashMap<>();
        recentContent =  ZonedDateTime.now();
        editHistory.put(recentContent, message);
    }

    @Override
    public Person getPublisher() {
        return publisher;
    }

    @Override
    public ZonedDateTime getPublishedTime() {
        return recentContent;
    }

    @Override
    public String getContent() {
        return editHistory.get(recentContent);
    }

    @Override
    public Map<ZonedDateTime, String> getEditHistory() {
        return editHistory;
    }

    @Override
    public void update(String editedMessage) {
        if(editedMessage.isEmpty()) throw new IllegalArgumentException("a message cannot contain empty content");
        recentContent = ZonedDateTime.now();
        editHistory.put(recentContent, editedMessage);
    }
}
