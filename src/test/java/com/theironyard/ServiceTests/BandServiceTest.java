package com.theironyard.ServiceTests;

import com.theironyard.TestApplication;
import com.theironyard.bands_core.model.Band;
import com.theironyard.bands_core.service.BandService;
import com.theironyard.users_core.model.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Created by ahanger on 5/15/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestApplication.class})
@WebAppConfiguration
public class BandServiceTest {

    @Autowired
    private BandService bandService;

    @Test
    public void testAddBand() throws Exception {

        Band band = new Band();
        band.setName("Phish");
        band.setCity("Burlington");
        band.setState("VT");
        band.setGenre("Jam band");

        bandService.addBand(band);

        Band check = bandService.getBandByName("Phish");
        Assert.assertNotNull(check);

    }
}
