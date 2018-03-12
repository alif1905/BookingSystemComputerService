package com.example.aliff.bookingsystemcomputerservice;

/**
 * Created by Aliff on 8/3/2018.
 */

public class auth {

    public String email;
    public String accessLevel;

    public auth(String email, String accessLevel) {
        this.email =email;
        this.accessLevel = accessLevel;
    }




    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }


}
