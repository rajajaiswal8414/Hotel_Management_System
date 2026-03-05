package com.hotelbooking.airbnb.security.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import com.hotelbooking.airbnb.exception.APIException;
import com.hotelbooking.airbnb.security.services.UserDetailsImpl;
import org.springframework.http.HttpStatus;


@Component
public class AuthUtil {

    public UserDetailsImpl getPrincipal() {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new APIException(
                    HttpStatus.UNAUTHORIZED,
                    "User is not authenticated"
            );
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetailsImpl userDetails) {
            return userDetails;
        }

        throw new APIException(
                HttpStatus.UNAUTHORIZED,
                "Invalid authentication principal"
        );
    }

    public String loggedInEmail() {
        return getPrincipal().getEmail();
    }

    public Long loggedInUserId() {
        return getPrincipal().getId();
    }
}
