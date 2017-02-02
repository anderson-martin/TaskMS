package com.astro.api;

import com.astro.api.message.Issue;
import com.astro.api.message.Task;

import java.util.List;

/**
 * Created by rohan on 2/2/17.
 */
// This class should communicate with data base, and persist the data
public class DataProvider {
    public List<Worker> getWorkers(Group group) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<Issue> getIssues(Group group) {
        throw new UnsupportedOperationException("Not implemented");
    }

    public List<Task> getTasks(Worker worker) {
        throw new UnsupportedOperationException("Not implemented");
    }

    // ... more useful operations goes here
}
