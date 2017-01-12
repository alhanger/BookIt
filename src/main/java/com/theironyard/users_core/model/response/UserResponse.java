package com.theironyard.users_core.model.response;

import com.theironyard.users_core.model.User;
import com.theironyard.utils.RestResponse;

/**
 * Created by ahanger on 5/15/2016.
 */
public class UserResponse extends RestResponse {

    private User user;

    public UserResponse(String SUCCESS_MESSAGE, String SUCCESS_CODE, User user) {
        super(SUCCESS_MESSAGE, SUCCESS_CODE);
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
