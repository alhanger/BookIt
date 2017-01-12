package com.theironyard.bands_core.service;

import com.theironyard.bands_core.model.Band;
import com.theironyard.entity_repositories.BandDao;
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

    private BandDao bandDao;

    @Autowired
    public BandService(BandDao bandDao) {
        this.bandDao = bandDao;
    }


    public void addBand(Band band) {
        bandDao.save(band);
    }

    public Band getBandByName(String name) {
        return bandDao.findOneByName(name);
    }

    public Band getBandById (int id) {
        return bandDao.findOneById(id);
    }

    public Band modifyBand(Band band) {
        Band bandCheck = getBandById(band.getId());
        bandCheck.setName(band.getName());
        bandCheck.setCity(band.getCity());
        bandCheck.setState(band.getState());
        bandCheck.setGenre(band.getGenre());
        bandCheck.setUser(band.getUser());

        bandDao.save(bandCheck);
        return bandCheck;
    }

    public void deleteBand(int bandId) {
        Band band = getBandById(bandId);
        bandDao.delete(band);
    }
}
