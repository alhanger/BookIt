package com.theironyard.bands_core.service;

import com.theironyard.bands_core.model.Band;
import com.theironyard.entity_repositories.BandRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ahanger on 5/15/2016.
 */
@Service
public class BandService {

    private static final Logger LOG = LoggerFactory.getLogger(BandService.class);

    private BandRepository bandRepository;

    @Autowired
    public BandService(BandRepository bandRepository) {
        this.bandRepository = bandRepository;
    }


    public void addBand(Band band) {

    }
}
