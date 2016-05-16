package com.theironyard.entity_repositories;

import com.theironyard.users_core.model.User;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by alhanger on 12/8/15.
 */
public interface UserRepository extends CrudRepository<User, Integer> {
    User findOneByUsername(String username);
    User findOneById(String id);
}
