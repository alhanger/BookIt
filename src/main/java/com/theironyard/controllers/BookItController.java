package com.theironyard.controllers;


import com.theironyard.entities.*;
import com.theironyard.services.*;
import com.theironyard.utils.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.convert.JodaTimeConverters;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class BookItController {

    public final String API_KEY = "YlX4r2ab8xzzlYDB";

    @Autowired
    UserRepository users;

    @Autowired
    BandRepository bands;

    @Autowired
    EventRepository events;


    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public void login(HttpSession session, @RequestBody User params) throws Exception {

        User user = users.findOneByUsername(params.username);
        if (user == null) {
            throw new Exception("User does not exists.");
        } else if (!PasswordHash.validatePassword(params.password, user.password)) {
            throw new Exception("Wrong password.");
        }

        session.setAttribute("username", params.username);
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @RequestMapping("/get-user")
    public User getUser(HttpSession session) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username);

        if (user == null) {
            throw new Exception("Not logged in.");
        }

        return user;
    }

    @RequestMapping(path = "/create-account", method = RequestMethod.POST)
    public void createAccount(@RequestBody User user, HttpSession session) throws Exception {
        User userCheck = users.findOneByUsername(user.username);

        if (userCheck == null) {
            user.password = PasswordHash.createHash(user.password);
            users.save(user);
            session.setAttribute("username", user.username);
        }
        else {
            throw new Exception("That username already exists.");
        }
    }

    @RequestMapping("/edit-account")
    public void editAccount(HttpSession session, @RequestBody User user) throws Exception {
        String name = (String) session.getAttribute("username");
        User userCheck = users.findOneByUsername(name);

        if (userCheck == null && userCheck.id != user.id) {
            throw new Exception("Not logged in.");
        }

        userCheck.username = user.username;
        userCheck.password = PasswordHash.createHash(user.password);
        userCheck.firstName = user.firstName;
        userCheck.lastName = user.lastName;
        userCheck.city = user.city;
        userCheck.state = user.state;
        userCheck.email = user.email;
        userCheck.phoneNum = user.phoneNum;

        users.save(userCheck);
    }

    @RequestMapping("/delete-account")
    public void deleteAccount(HttpSession session, @RequestBody User user) throws Exception {
        String username = (String) session.getAttribute("username");
        User userCheck = users.findOneByUsername(username);

        if (!PasswordHash.validatePassword(user.password, userCheck.password)) {
            throw new Exception("Incorrect password.");
        }

        List<Band> userBand = bands.findAllByUserId(userCheck.id);
        bands.delete(userBand);
        users.delete(user);
    }

    @RequestMapping("/create-band")
    public void createBand(HttpSession session, @RequestBody Band band) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username);

        band.user = user;
        bands.save(band);
    }

    @RequestMapping(path = "/edit-band/{bandId}", method = RequestMethod.PUT)
    public void editBand(HttpSession session,
                         @PathVariable("bandId") int id,
                         @RequestBody Band band)
            throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username);

        if (user == null) {
            throw new Exception("Not logged in.");
        }

        Band band2 = bands.findOne(id);
        band2.name = band.name;
        band2.city = band.city;
        band2.state = band.state;
        band2.genre = band.genre;
        band2.picURL = band.picURL;

        bands.save(band2);
    }

    @RequestMapping("/delete-band/{id}")
    public void deleteBand(@PathVariable("id") int id) {
        bands.delete(id);
    }

    @RequestMapping("/get-bands/{id}")
    public List<Band> getBands(@PathVariable("id") int id) {
        return bands.findAllByUserId(id);
    }

    @RequestMapping("/get-band/{id}")
    public Band getBand(@PathVariable("id") int id) {
        return bands.findOne(id);
    }

    @RequestMapping("/add-event/{bandId}")
    public Message addEvent(@PathVariable("bandId") int id, @RequestBody final Event event, HttpSession session) {
        Band band = bands.findOne(id); // <- band that you are trying to book a show for
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username); // <- currently logged in user

        //Event eventCheck = events.findFirstByDate(event.date);
        Event eventCheck = events.findFirstByDateAndVenueName(event.date, event.venueName); // <- checks if the event being booked already exists

        String now = LocalDateTime.now().toString();

        // checks if user is booking a date in the past
        if (event.dateFormat.compareTo(now) < 0) {
            Message pastDateMessage = new Message("You've selected an invalid date.");
            return pastDateMessage;
        }

        // the event already exists
        if (eventCheck != null) {
            List<Band> eventBands = eventCheck.bands; // <- captures all bands booked for that event
            // the event does not contain the band you are currently trying to book
            if (!eventBands.contains(band)) {
                Band bandCheck = eventBands.get(0); // <- gets the first band listed on the event
                User userCheck = bandCheck.user; // <- gets the user that owns the first band on the event
                // the logged in user does not own the band listed on the event
                if (user != userCheck) {
                    // the event has been confirmed by another band
                    if (eventCheck.isConfirmed == true) {
                        Message differentUserBandConfirmed = new Message(String.format("This date has already been confirmed by %s, but not updated by the venue. Contact %s %s at %s or %s for more details.",
                                bandCheck.name,
                                userCheck.firstName,
                                userCheck.lastName,
                                userCheck.phoneNum,
                                userCheck.email));
                        return differentUserBandConfirmed;
                    }
                    // another band is interested in the same date but has not confirmed
                    else if (eventCheck.isConfirmed == false) {
                        Message differentUserBandUnconfirmed = new Message("Another band is interested in this date, but has not confirmed it with the venue.");
                        return differentUserBandUnconfirmed;
                    }
                }
                // the logged in user owns the band listed on the event
                else if (user == userCheck) {
                    // the event is confirmed for a different band owned by the user
                    if (eventCheck.isConfirmed == true) {
                        Message differentBandConfirmed = new Message(String.format("You've have already booked this date for %s.", bandCheck.name));
                        return differentBandConfirmed;
                    }
                    // the user is interested in the evnet for a different band but has not confirmed it yet
                    else if (eventCheck.isConfirmed == false) {
                        Message differentBandUnconfirmed = new Message(String.format("You've expressed interests in this date for %s, but have not confirmed it with the venue.",
                                bandCheck.name));
                        return differentBandUnconfirmed;
                    }
                }
            }
            // the event contains the band the user is currently trying to book
            else if (eventBands.contains(band)) {
                // the user has already confirmed this date with the venue
                if (eventCheck.isConfirmed == true) {
                    Message sameBandConfirmed = new Message(String.format("You have already confirmed this date for %s.", band.name));
                    return sameBandConfirmed;
                }
                // the user has not confirmed this date with the venue
                else if (eventCheck.isConfirmed == false) {
                    Message sameBandUnconfirmed = new Message(String.format("You have already scheduled this date for %s, but have not confirmed it.",
                            band.name));
                    return sameBandUnconfirmed;
                }
            }
        }

        // checks if the current band already has a show scheduled on this date
        ArrayList<Event> dateCheck = band.events.stream()
                                        .filter(e -> {
                                            return e.date.equals(event.date);
                                        })
                                        .collect(Collectors.toCollection(ArrayList<Event>::new));

        if (dateCheck.size() != 0) {
            Message dateConflict = new Message(String.format("You have already scheduled a show for %s on this date.", band.name));
            return dateConflict;
        }

        Event event2 = event;
        band.events.add(event2);
        bands.save(band);
        events.save(event2);

        Message addEventMessage = new Message(String.format("The event has been added to %s's schedule.", band.name));
        return addEventMessage;
    }

    @RequestMapping("/get-events/{bandId}")
    public Collection<Event> getEvents(@PathVariable("bandId") int id) {
        return bands.findOne(id).events;
    }

    @RequestMapping("/get-event/{eventId}")
    public Event getEvent(@PathVariable("eventId") int id) {
        Event show = events.findOne(id);
        return show;
    }

    @RequestMapping("/edit-event/{eventId}")
    public void editEvent(@PathVariable("eventId") int eventId, @RequestBody Event event) {
        Event event2 = events.findOne(eventId);

        event2.date = event.date;
        event2.dateYear = event.dateYear;
        event2.dateMonth = event.dateMonth;
        event2.dateDay = event.dateDay;
        event2.venueName = event.venueName;
        event2.venueAddress = event.venueAddress;
        event2.venuePhoneNum = event.venuePhoneNum;
        event2.venueWebsite = event.venueWebsite;
        event2.venueLong = event.venueLong;
        event2.venueLat = event.venueLat;
        event2.isConfirmed = event.isConfirmed;

        events.save(event2);
    }

    @RequestMapping(path = "/delete-event/{eventId}", method = RequestMethod.DELETE)
    public void deleteEvent(@PathVariable("eventId") int id) {
        Band band = bands.findByEventsId(id);
        Event event = events.findOne(id);
        band.events.remove(event);
        events.delete(event);
    }

    // returns a list of venues in a city
    @RequestMapping(path = "/search-venues/{location}", method = RequestMethod.GET)
    public ArrayList<HashMap> getVenues(@PathVariable("location") String location) {
        String request = "http://api.songkick.com/api/3.0/search/venues.json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request)
                .queryParam("query", location)
                .queryParam("apikey", API_KEY)
                .queryParam("per_page", 10);

        RestTemplate query = new RestTemplate();
        HashMap search = query.getForObject(builder.build().encode().toUri(), HashMap.class);
        HashMap resultsPage = (HashMap) search.get("resultsPage");
        HashMap results = (HashMap) resultsPage.get("results");
        ArrayList<HashMap> venues = (ArrayList<HashMap>) results.get("venue");

        return venues;
    }

    // returns a particular venue's calendar
    @RequestMapping(path = "/get-calendar/{venueId}", method = RequestMethod.GET)
    public ArrayList<HashMap> getCalendar(@PathVariable("venueId") int venueId) {
        String request = "http://api.songkick.com/api/3.0/venues/" + venueId + "/calendar.json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request)
                .queryParam("apikey", API_KEY);

        RestTemplate query = new RestTemplate();
        HashMap search = query.getForObject(builder.build().encode().toUri(), HashMap.class);
        HashMap resultsPage = (HashMap) search.get("resultsPage");
        HashMap results = (HashMap) resultsPage.get("results");
        ArrayList<HashMap> events = (ArrayList<HashMap>) results.get("event");

        if (events == null) {
            events = new ArrayList<HashMap>();
        }

        return events;
    }

    @RequestMapping("/get-venue-details/{venueId}")
    public HashMap getVenueDetails(@PathVariable("venueId") int venueId) {
        String request = "http://api.songkick.com/api/3.0/venues/" + venueId + ".json";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(request)
                .queryParam("apikey", API_KEY);

        RestTemplate query = new RestTemplate();
        HashMap search = query.getForObject(builder.build().encode().toUri(), HashMap.class);
        HashMap resultsPage = (HashMap) search.get("resultsPage");
        HashMap results = (HashMap) resultsPage.get("results");
        HashMap venue = (HashMap) results.get("venue");

        return venue;
    }
}