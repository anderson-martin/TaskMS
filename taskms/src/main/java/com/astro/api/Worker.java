package com.astro.api;

import java.util.Set;

/**
 * Created by rohan on 2/2/17.
 */
public interface Worker extends Person {
    /**
     *
     * @return the set of group this worker belong to
     */
    Set<Group> getGruop();
}
