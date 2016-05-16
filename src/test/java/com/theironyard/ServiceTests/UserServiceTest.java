package com.theironyard.ServiceTests;

import com.theironyard.TestApplication;
import com.theironyard.users_core.service.UserService;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class UserServiceTest {

    private static final Logger LOG = LoggerFactory.getLogger(UserServiceTest.class);
    private final String SUCCESS_MESSAGE = "Success";
    private final String SUCCESS_CODE = "200";

    private UserService userService;

    @Autowired
    public UserServiceTest(UserService userService) {
        this.userService = userService;
    }


}
