package com.astro.api;

/**
 * Created by rohan on 2/2/17.
 */

// Immutable data type that represent a role of a person in the system

public class Group {
    private String group;

    /**
     * Create the group with the given name
     * A name of a group must be a string contain at least one character from A-Z
     * @param group name of the group
     * @throws IllegalArgumentException if the name of the group does not adhere to the specified format
     */
    public Group(String group) {
        // TODO: check rep invariants
        this.group = group;
    }

    public String getGruopName() {
        return group;
    }
}
