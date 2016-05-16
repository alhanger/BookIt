package com.theironyard.ServiceTests;

import com.theironyard.TestApplication;
import com.theironyard.entity_repositories.UserRepository;
import com.theironyard.users_core.model.User;
import com.theironyard.users_core.service.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
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

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    @Before
    public void init() {
        userRepository.deleteAll();
    }

    @Test
    public void testCreateUser() throws Exception {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setCity("testCity");
        user.setState("testState");
        user.setEmail("testEmail");
        user.setPhoneNum("testPhoneNum");
        userRepository.save(user);

        User userCheck = userService.getUserByUsername(user.getUsername());

        Assert.assertNotNull(userCheck);
        Assert.assertNotNull(userCheck.getId());
    }

    @Test
    public void testModifyUser() throws Exception {
        User testModify = createTestUser();
        User testUser = createTestUser();
        testModify.setUsername("modifyUsername");
        testModify.setPhoneNum("modifyPhoneNum");
        testModify.setState("modifyState");
        testModify.setEmail("modifyEmail");

        Assert.assertNotNull(testModify.getUsername(), testUser.getUsername());
        Assert.assertNotEquals(testModify.getPhoneNum(), testUser.getPhoneNum());

        testUser = userService.modifyUser(testModify);

        Assert.assertEquals(testModify.getUsername(), testUser.getUsername());
        Assert.assertEquals(testModify.getPhoneNum(), testUser.getPhoneNum());
    }

    public User createTestUser() {
        User user = new User();
        user.setUsername("testUsername");
        user.setPassword("testPassword");
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setCity("testCity");
        user.setState("testState");
        user.setEmail("testEmail");
        user.setPhoneNum("testPhoneNum");
        userRepository.save(user);

        return userService.getUserById(user.getId());
    }
}
