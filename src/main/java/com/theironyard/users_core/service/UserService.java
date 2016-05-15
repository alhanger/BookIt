package com.theironyard.users_core.service;

import com.theironyard.entity_repositories.UserRepository;
import com.theironyard.users_core.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by ahanger on 4/28/2016.
 */
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String username) {
        LOG.info("Looking for user with username: " + username);
        User user = userRepository.findOneByUsername(username);

        if(user == null) {
            LOG.info("User not found");
        }

        return user;
    }


}
