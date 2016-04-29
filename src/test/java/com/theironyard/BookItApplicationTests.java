//package com.theironyard;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.theironyard.bands_core.model.Band;
//import com.theironyard.users_core.model.User;
//import com.theironyard.service.entity_repositories.BandRepository;
//import com.theironyard.service.entity_repositories.UserRepository;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.web.WebAppConfiguration;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import org.springframework.web.context.WebApplicationContext;
//import static org.junit.Assert.assertTrue;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = BookItApplication.class)
//@WebAppConfiguration
//public class BookItApplicationTests {
//
//	@Autowired
//    UserRepository users;
//
//    @Autowired
//    BandRepository bands;
//
//    @Autowired
//    WebApplicationContext wap;
//
//    MockMvc mockMvc;
//
//    @Before
//    public void before() {
//        users.deleteAll();
//        bands.deleteAll();
//        mockMvc = MockMvcBuilders.webAppContextSetup(wap).build();
//    }
//
//    @Test
//    public void testCreateAccount() throws Exception {
//        User user = new User();
//        user.username = "testUsername";
//        user.password = "testPassword";
//        user.firstName = "testFirstName";
//        user.lastName = "testLastName";
//        user.city = "testCity";
//        user.state = "testState";
//        user.email = "testEmail";
//        user.phoneNum = "testPhoneNum";
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(user);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/create-account")
//                .content(json)
//                .contentType("application/json")
//        );
//
//        assertTrue(users.count() == 1);
//    }
//
//    @Test
//    public void testCreateBand() throws Exception {
//        Band band = new Band();
//        band.name = "testBand";
//        band.city = "testCity";
//        band.state = "testState";
//        band.genre = "testGenre";
//
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(band);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/create-band")
//                .content(json)
//                .contentType("application/json")
//        );
//
//        assertTrue(users.count() == 1);
//    }
//}
