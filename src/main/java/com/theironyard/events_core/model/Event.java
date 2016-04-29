package com.theironyard.events_core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.theironyard.bands_core.model.Band;

import javax.persistence.*;
import java.util.List;

/**
 * Created by alhanger on 12/8/15.
 */
@Entity
@Table (name = "events")
public class Event {
    @Id
    @GeneratedValue
    public int id;
    @Column(nullable = false)
    public String date;
    @Column
    public int dateYear;
    @Column
    public int dateMonth;
    @Column
    public int dateDay;
    @Column
    public String dateFormat;
    @Column(nullable = false)
    public String venueName;
    @Column
    public String venueAddress;
    @Column
    public String venuePhoneNum;
    @Column
    public String venueWebsite;
    @Column
    public int venueLong;
    @Column
    public int venueLat;
    @Column
    public boolean isConfirmed;

    @ManyToMany(
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            mappedBy = "events",
            targetEntity = Band.class
    )
    @JsonIgnore
    public List<Band> bands;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDateYear() {
        return dateYear;
    }

    public void setDateYear(int dateYear) {
        this.dateYear = dateYear;
    }

    public int getDateMonth() {
        return dateMonth;
    }

    public void setDateMonth(int dateMonth) {
        this.dateMonth = dateMonth;
    }

    public int getDateDay() {
        return dateDay;
    }

    public void setDateDay(int dateDay) {
        this.dateDay = dateDay;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getVenueAddress() {
        return venueAddress;
    }

    public void setVenueAddress(String venueAddress) {
        this.venueAddress = venueAddress;
    }

    public String getVenuePhoneNum() {
        return venuePhoneNum;
    }

    public void setVenuePhoneNum(String venuePhoneNum) {
        this.venuePhoneNum = venuePhoneNum;
    }

    public String getVenueWebsite() {
        return venueWebsite;
    }

    public void setVenueWebsite(String venueWebsite) {
        this.venueWebsite = venueWebsite;
    }

    public int getVenueLong() {
        return venueLong;
    }

    public void setVenueLong(int venueLong) {
        this.venueLong = venueLong;
    }

    public int getVenueLat() {
        return venueLat;
    }

    public void setVenueLat(int venueLat) {
        this.venueLat = venueLat;
    }

    public List<Band> getBands() {
        return bands;
    }

    public void setBands(List<Band> bands) {
        this.bands = bands;
    }

    public boolean isConfirmed() {
        return isConfirmed;
    }

    public void setConfirmed(boolean confirmed) {
        isConfirmed = confirmed;
    }
}