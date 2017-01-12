package com.theironyard.events_core.service;

import com.theironyard.bands_core.model.Band;
import com.theironyard.entities.Message;
import com.theironyard.entity_repositories.BandRepository;
import com.theironyard.entity_repositories.EventRepository;
import com.theironyard.entity_repositories.UserRepository;
import com.theironyard.events_core.model.Event;
import com.theironyard.users_core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ahanger on 5/15/2016.
 */
@Service
public class EventService {

    private static final Logger log = LoggerFactory.getLogger(EventService.class);

    private final String API_KEY = "YlX4r2ab8xzzlYDB";

    private EventRepository events;
    private BandRepository bands;
    private UserRepository users;

    @Autowired
    public EventService(EventRepository events, BandRepository bands, UserRepository users) {
        this.events = events;
        this.bands = bands;
        this.users = users;
    }

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

    @RequestMapping("/venue-details/{venueId}")
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

    @RequestMapping("/add-event/{bandId}")
    public Message addEvent(@PathVariable("bandId") int id,
                            @RequestBody final Event event,
                            HttpSession session) {
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
                                userCheck.getFirstName(),
                                userCheck.getLastName(),
                                userCheck.getPhoneNum(),
                                userCheck.getEmail()));
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
}
