package com.astro.api;

import com.astro.api.personalInfo.Address;
import com.astro.api.personalInfo.Email;
import com.astro.api.personalInfo.PhoneNum;

import java.util.Date;

/**
 * Created by rohan on 2/2/17.
 */
public interface Person {
    /**
     * Get first name of this person
     * First name is a non empty sequence of character that contain chars a-z A-Z
     *
     * @return name of this person
     */
    String getFirstName();

    /**
     * Get last name of this person
     * First name is a non empty sequence of character that contain chars a-z A-Z
     *
     * @return
     */
    String getLastName();


    /**
     * Get email of this person
     * @return email of this person
     */
    Email getEmail();

    /**
     * Get address of this person
     * @return address of this person
     */
    Address getAddess();

    /**
     * Get phone number of this person
     * @return phone number of this person
     */
    PhoneNum getPhoneNum();

    /**
     * Get date of birth of this person
     *
     * @return phone number of this person
     */
    // TODO: this Data data type should be replaced for the corresponding immutable data type
    Date getDOB();
}
