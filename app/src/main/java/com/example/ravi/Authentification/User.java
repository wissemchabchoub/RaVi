package com.example.ravi.Authentification;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@IgnoreExtraProperties
public class User implements Serializable {

    public String id;
    public String email;
    public String name;
    public String lastName;
    public String nationality;
    public String phoneNumber;
    public String address;

    public Date birthDate;
    public Date creationDate;

    public boolean quizzDone;

    public HashMap<String, List<String>> preferences;

    public User() {
        preferences = new HashMap<>();
        creationDate = Calendar.getInstance().getTime();
        quizzDone = false;
    }

    public User(String email, String id) {
        this();
        this.email = email;
        this.id = id;
    }

    public void putPreference(String key, String value) {
        List<String> list = new ArrayList<>();
        if (this.preferences.containsKey(key)) list = this.preferences.get(key);
        list.add(value);
        this.preferences.put(key, list);
    }

    public boolean isQuizzDone() {
        return quizzDone;
    }
}
