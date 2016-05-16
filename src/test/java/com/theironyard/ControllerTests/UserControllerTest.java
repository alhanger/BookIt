package com.theironyard.ControllerTests;

import com.theironyard.TestApplication;
import com.theironyard.entity_repositories.BandRepository;
import com.theironyard.entity_repositories.UserRepository;
import com.theironyard.users_core.model.User;
import com.theironyard.users_core.rest.UserController;
import com.theironyard.users_core.service.UserService;
import com.theironyard.utils.PasswordHash;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

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
    protected BandRepository bandRepository;
    protected UserRepository userRepository;

    @Autowired
    public UserControllerTest(MockMvc mockMvc, UserController userController, UserService userService, BandRepository bandRepository, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.userController = userController;
        this.userService = userService;
        this.bandRepository = bandRepository;
        this.userRepository = userRepository;
    }

    @Before
    public void init() throws InvalidKeySpecException, NoSuchAlgorithmException {
        userRepository.deleteAll();
    }

    @Test
    public void testLogin() throws Exception {
        User user = createUser();

        //MvcResult result = mockMvc.perform(post("/users/login"))
    }

    public User createUser() throws InvalidKeySpecException, NoSuchAlgorithmException {
        User user = new User();
        user.setUsername("username");
        user.setPassword(PasswordHash.createHash("testpassword"));
        user.setFirstName("alex");
        user.setLastName("hanger");
        user.setCity("atlanta");
        user.setState("georgia");
        user.setEmail("hanger129@gmail.com");
        user.setPhoneNum("4043881262");
        userService.createUser(user);

        return userService.getUserByUsername("username");
    }
}
