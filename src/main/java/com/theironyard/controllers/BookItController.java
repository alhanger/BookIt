package com.theironyard.controllers;


import com.theironyard.entities.*;
import com.theironyard.services.*;
import com.theironyard.utils.PasswordHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.util.*;


@RestController
public class BookItController {

    public final String API_KEY = "YlX4r2ab8xzzlYDB";

    @Autowired
    UserRepository users;

    @Autowired
    BandRepository bands;

    @Autowired
    EventRepository events;

    @Autowired
    PicFileRepository pics;

    @RequestMapping("/login")
    public void login(HttpSession session, @RequestBody User params, HttpServletResponse response) throws Exception {

        User user = users.findOneByUsername(params.username);
        if (user == null) {
            response.sendRedirect("/create-account");
        }
        else if (!PasswordHash.validatePassword(params.password, user.password)) {
            throw new Exception("Wong password.");
        }

        session.setAttribute("username", params.username);
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @RequestMapping("/get-user")
    public User getUser(HttpSession session, HttpServletResponse response) throws Exception {
        User user = users.findOneByUsername((String)session.getAttribute("username"));

        if (user == null) {
            response.sendRedirect("/login/");
        }

        return user;
    }

    @RequestMapping("/create-account")
    public void createAccount(@RequestBody User user,
                              /*@RequestParam(value = "file", required = false) MultipartFile file,*/
                              HttpSession session)
            throws Exception {
        user.password = PasswordHash.createHash(user.password);

        /*
        if (file != null) {
            File f = File.createTempFile("pic", file.getOriginalFilename(), new File("public/assets"));
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(file.getBytes());

            PicFile profPic = new PicFile();
            profPic.originalName = file.getOriginalFilename();
            profPic.name = file.getName();
            user.pic = profPic;
            pics.save(profPic);
        }
        */

        users.save(user);

        session.setAttribute("username", user.username);
    }

    @RequestMapping("/edit-account")
    public void editAccount(HttpSession session,
                            @RequestBody User user
                            /*@RequestParam(value = "file", required = false) MultipartFile file*/)
            throws Exception {
        String name = (String) session.getAttribute("username");
        User user2 = users.findOneByUsername(name);

        if (user2 == null && user2.id != user.id) {
            throw new Exception("Not logged in.");
        }

        user2.username = user.username;
        user2.password = PasswordHash.createHash(user.password);
        user2.firstName = user.firstName;
        user2.lastName = user.lastName;
        user2.city = user.city;
        user2.state = user.state;
        user2.email = user.email;
        user2.phoneNum = user.phoneNum;

        /*
        if (file != null) {
            File f = File.createTempFile("pic", file.getOriginalFilename(), new File("public/assets"));
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(file.getBytes());

            PicFile profPic = user2.pic;
            if (profPic != null) {
                File deleteFile = new File("public/assets", profPic.name);
                deleteFile.delete();
                pics.delete(profPic);
            }

            profPic = new PicFile();
            profPic.originalName = file.getOriginalFilename();
            profPic.name = file.getName();
            user2.pic = profPic;

            pics.save(profPic);
        }
        */

        users.save(user2);
    }

    @RequestMapping("/delete-account")
    public void deleteAccount(HttpSession session, String password) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username);

        if (!PasswordHash.validatePassword(password, user.password)) {
            throw new Exception("Incorrect password.");
        }

        users.delete(user);
    }

    @RequestMapping("/create-band")
    public void createBand(HttpSession session,
                           @RequestBody Band band
                           /*@RequestParam(value = "file", required = false) MultipartFile file*/)
            throws Exception {
        String username = (String) session.getAttribute("username");
        User user = users.findOneByUsername(username);

        /*
        File f = File.createTempFile("pic", file.getOriginalFilename(), new File("public/assets"));
        FileOutputStream fos = new FileOutputStream(f);
        fos.write(file.getBytes());

        PicFile bandPic = new PicFile();
        bandPic.originalName = file.getOriginalFilename();
        bandPic.name = file.getName();

        band.pic = bandPic;
        */
        band.user = user;

        //pics.save(bandPic);
        bands.save(band);
    }

    @RequestMapping(path = "/edit-band/{bandId}", method = RequestMethod.PUT)
    public void editBand(HttpSession session,
                         @PathVariable("bandId") int id,
                         @RequestBody Band band
                         /*@RequestParam(value = "file", required = false) MultipartFile file*/)
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

        /*
        if (file != null) {
            File f = File.createTempFile("pic", file.getOriginalFilename(), new File("public/assets"));
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(file.getBytes());

            PicFile profPic = band2.pic;
            if (profPic != null) {
                File deleteFile = new File("public/assets", profPic.name);
                deleteFile.delete();
                pics.delete(profPic);
            }

            profPic = new PicFile();
            profPic.originalName = file.getOriginalFilename();
            profPic.name = file.getName();
            band2.pic = profPic;

            pics.save(profPic);
        }
        */

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
    public String addEvent(@PathVariable("bandId") int id, @RequestBody Event event, HttpSession session) {
        Band band = bands.findOne(id); // band that you are trying to book a show for
        User user = (User) session.getAttribute("username"); // currently logged in user

        Event eventCheck = events.findOneByDate(event.date); // checks if the event being booked already exists
        ArrayList<Band> eventBands = (ArrayList<Band>) eventCheck.bands; // captures all bands booked for that event
        Band bandCheck = eventBands.get(0); // gets the first band listed on the event
        User userCheck = bandCheck.user; // gets the user that owns the first band on the event

        // the event already exists
        if (eventCheck != null) {
            // the event does not contain the band you are currently trying to book
            if (!eventBands.contains(band)) {
                // the logged in user does not own the band listed on the event
                if (user != userCheck) {
                    // the event has been confirmed by another band
                    if (eventCheck.isConfirmed == true) {
                        return String.format("This date has already been confirmed by %s. Contact %s %s at %s or %s for more details.",
                                bandCheck.name,
                                userCheck.firstName,
                                userCheck.lastName,
                                userCheck.phoneNum,
                                userCheck.email);
                    }
                    // another band is interested in the same date but has not confirmed
                    else if (eventCheck.isConfirmed == false) {
                        return "Another band is interested in this date, but has not confirmed it with the venue.";
                    }
                }
                // the logged in user owns the band listed on the event
                else if (user == userCheck) {
                    // the event is confirmed for a different band owned by the user
                    if (eventCheck.isConfirmed == true) {
                        return String.format("You've have already booked this date for %s.", bandCheck.name);
                    }
                    // the user is interested in the evnet for a different band but has not ocnfirmed it yet
                    else if (eventCheck.isConfirmed == false) {
                        return String.format("You've expressed interests in this date for %s, but have not confirmed it with the venue.",
                                bandCheck.name);
                    }
                }
            }
            // the event contains the band you are currently trying to book
            else if (eventBands.contains(band)) {
                // you have already confirmed this date with the venue
                if (eventCheck.isConfirmed == true) {
                    return String.format("You have already confirmed this date for %s.", band.name);
                }
                // you have not confirmed this date with the venue
                else if (eventCheck.isConfirmed == false) {
                    return String.format("You have already scheduled this date for %s, but have not confirmed it.",
                            band.name);
                }
            }
        }

        band.events.add(event);
        event.bands.add(band);
        bands.save(band);
        events.save(event);

        return String.format("The event has been added to %s's schedule.", band.name);
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
