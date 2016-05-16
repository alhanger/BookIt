package com.theironyard.ControllerTests;

import com.theironyard.TestApplication;
import com.theironyard.users_core.rest.UserController;
import com.theironyard.users_core.service.UserService;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

/**
 * Created by ahanger on 5/15/2016.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestApplication.class})
@WebAppConfiguration
public class UserControllerTest {

    private static final Logger LOG = LoggerFactory.getLogger(UserControllerTest.class);
    private final String SUCCESS_MESSAGE = "Success";
    private final String SUCCESS_CODE = "200";

    protected MockMvc mockMvc;
    protected UserController userController;
    protected UserService userService;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserController userController, UserService userService) {
        this.mockMvc = mockMvc;
        this.userController = userController;
        this.userService = userService;
    }
}
