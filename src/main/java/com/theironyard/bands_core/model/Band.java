package com.theironyard.bands_core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.events_core.model.Event;
import com.theironyard.users_core.model.User;

import javax.persistence.*;
import java.util.Collection;

/**
 * Created by alhanger on 12/8/15.
 */
@Entity
@Table (name = "bands")
public class Band {
    @Id
    @GeneratedValue
    public int id;
    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String city;
    @Column(nullable = false)
    public String state;
    @Column(nullable = false)
    public String genre;
    @Column
    public String picURL;
    @ManyToOne
    public User user;

    @ManyToMany(
            targetEntity = Event.class,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE}
    )
    @JoinTable(
            name = "BAND_EVENT",
            joinColumns = @JoinColumn(name = "BAND_ID"),
            inverseJoinColumns = @JoinColumn(name = "EVENT_ID")
    )
    @JsonIgnore
    public Collection<Event> events;

    public Band() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Collection getEvents() {
        return events;
    }
}