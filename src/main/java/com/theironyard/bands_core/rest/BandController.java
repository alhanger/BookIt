package com.theironyard.bands_core.rest;

import com.theironyard.bands_core.model.Band;
import com.theironyard.bands_core.model.BandResponse;
import com.theironyard.bands_core.service.BandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by ahanger on 5/15/2016.
 */
@RestController
@RequestMapping(path = "/bands")
public class BandController {

    //TODO: create custom exceptions

    private static final Logger log = LoggerFactory.getLogger(BandController.class);

    private final String STATUS_CODE = "200";
    private final String STATUS_MESSAGE = "Success";

    private BandService bandService;

    @Autowired
    public BandController(BandService bandService) {
        this.bandService = bandService;
    }

    @RequestMapping(value = "/{bandId}", method = RequestMethod.GET)
    public BandResponse getBand(@PathVariable int bandId) {
        log.info("GET request to /bands/" + bandId);

        Band band = bandService.getBandById(bandId);

        if (band != null && !band.toString().isEmpty()) {
            return new BandResponse(STATUS_CODE, STATUS_MESSAGE, band);
        } else {
            return new BandResponse(STATUS_CODE, STATUS_MESSAGE, "Band not found");
        }
    }

    @RequestMapping(value = "/add-band", method = RequestMethod.POST)
    public BandResponse addBand(@RequestBody Band band) {
        log.info("POST request to /bands/add-band");

        bandService.addBand(band);

        return new BandResponse(STATUS_CODE, STATUS_MESSAGE, band);
    }

    @RequestMapping(value = "/edit-band/{bandId}", method = RequestMethod.PATCH)
    public BandResponse editBand(@PathVariable int bandId, @RequestBody Band band) {
        log.info("PATCH request to /bands/edit-band/" + bandId);
        Band bandCheck = bandService.getBandById(bandId);

        if (bandCheck != null && !bandCheck.toString().isEmpty()) {
            log.info("Modifying band entry with id " + bandCheck.getId());
            bandService.modifyBand(band);
            return new BandResponse(STATUS_CODE, STATUS_MESSAGE, band);
        } else {
            return new BandResponse("500", "System error", "Unable to edit band");
        }
    }

    @RequestMapping(value = "/{bandId}",method = RequestMethod.DELETE)
    public BandResponse removeBand(@PathVariable int bandId) {
        log.info("DELETE request to /bands/" + bandId);

        Band band = bandService.getBandById(bandId);

        if (band != null && !band.toString().isEmpty()) {
            bandService.deleteBand(band.getId());
            return new BandResponse(STATUS_CODE, STATUS_MESSAGE, "Band successfully deleted");
        } else if (band == null) {
            return new BandResponse("500", "System error", "Band not found");
        } else {
            return new BandResponse("500", "System error");
        }
    }

}
