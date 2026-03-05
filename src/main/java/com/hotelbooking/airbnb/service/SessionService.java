package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.entity.User;

public interface SessionService {
    void generateNewSession(User user, String refreshToken);

    boolean validateSession(String refreshToken);
}
