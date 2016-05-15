package com.theironyard.bands_core.rest;

import com.theironyard.bands_core.service.BandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ahanger on 5/15/2016.
 */
@RestController
@RequestMapping(path = "/band")
public class BandController {

    private static final Logger LOG = LoggerFactory.getLogger(BandController.class);

    private BandService bandService;

    @Autowired
    public BandController(BandService bandService) {
        this.bandService = bandService;
    }


}
