package com.hotelbooking.airbnb.service;

import com.hotelbooking.airbnb.dto.LoginRequestDto;
import com.hotelbooking.airbnb.dto.LoginResponseDto;
import com.hotelbooking.airbnb.dto.SignupRequestDto;
import com.hotelbooking.airbnb.dto.UserDto;

public interface AuthService {
    UserDto signUp(SignupRequestDto signupRequestDto);

    LoginResponseDto signIn(LoginRequestDto loginRequestDto);
}
