package com.theironyard.users_core.rest;

import com.theironyard.bands_core.model.Band;
import com.theironyard.entity_repositories.BandRepository;
import com.theironyard.entity_repositories.UserRepository;
import com.theironyard.users_core.model.User;
import com.theironyard.utils.PasswordHash;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by ahanger on 4/28/2016.
 */
@RestController
public class UsersController {

    private static final Logger LOG = LoggerFactory.getLogger(UsersController.class);
    private UserRepository usersRepository;
    private BandRepository bandsRepository;

    @Autowired
    public UsersController(UserRepository usersRepository, BandRepository bandsRepository) {
        this.usersRepository = usersRepository;
        this.bandsRepository = bandsRepository;
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public void login(HttpSession session, @RequestBody User params) throws Exception {

        User user = usersRepository.findOneByUsername(params.getUsername());
        if (user == null) {
            throw new Exception("User does not exists.");
        } else if (!PasswordHash.validatePassword(params.getPassword(), user.getPassword())) {
            throw new Exception("Wrong password.");
        }

        session.setAttribute("username", params.getUsername());
    }

    @RequestMapping("/logout")
    public void logout(HttpSession session) {
        session.invalidate();
    }

    @RequestMapping("/get-user")
    public User getUser(HttpSession session) throws Exception {
        String username = (String) session.getAttribute("username");
        User user = usersRepository.findOneByUsername(username);

        if (user == null) {
            throw new Exception("Not logged in.");
        }

        return user;
    }

    @RequestMapping(path = "/create-account", method = RequestMethod.POST)
    public void createAccount(@RequestBody User user, HttpSession session) throws Exception {
        User userCheck = usersRepository.findOneByUsername(user.getUsername());

        if (userCheck == null) {
            user.setPassword(PasswordHash.createHash(user.getPassword()));
            usersRepository.save(user);
            session.setAttribute("username", user.getUsername());
        }
        else {
            throw new Exception("That username already exists.");
        }
    }

    @RequestMapping("/edit-account")
    public void editAccount(HttpSession session, @RequestBody User user) throws Exception {
        String name = (String) session.getAttribute("username");
        User userCheck = usersRepository.findOneByUsername(name);

        if (userCheck == null && userCheck.id != user.id) {
            throw new Exception("Not logged in.");
        }

        userCheck.setUsername(user.getUsername());
        userCheck.setPassword(PasswordHash.createHash(user.getPassword()));
        userCheck.setFirstName(user.getFirstName());
        userCheck.setLastName(user.getLastName());
        userCheck.setCity(user.getCity());
        user.setState(user.getState());
        userCheck.setEmail(user.getEmail());
        userCheck.setPhoneNum(user.getPhoneNum());

        usersRepository.save(userCheck);
    }

    @RequestMapping("/delete-account")
    public void deleteAccount(HttpSession session, @RequestBody User user) throws Exception {
        String username = (String) session.getAttribute("username");
        User userCheck = usersRepository.findOneByUsername(username);

        if (!PasswordHash.validatePassword(user.getPassword(), userCheck.getPassword())) {
            throw new Exception("Incorrect password.");
        }

        List<Band> userBand = bandsRepository.findAllByUserId(userCheck.id);
        bandsRepository.delete(userBand);
        usersRepository.delete(user);
    }
}
