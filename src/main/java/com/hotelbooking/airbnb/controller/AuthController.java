package com.hotelbooking.airbnb.controller;

import com.hotelbooking.airbnb.dto.LoginRequestDto;
import com.hotelbooking.airbnb.dto.LoginResponseDto;
import com.hotelbooking.airbnb.dto.SignupRequestDto;
import com.hotelbooking.airbnb.dto.UserDto;
import com.hotelbooking.airbnb.service.AuthService;
import jakarta.validation.Valid;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<@NonNull UserDto> signUp(@Valid @RequestBody SignupRequestDto signupRequestDto){
        UserDto userDto = authService.signUp(signupRequestDto);
        return ResponseEntity.ok(userDto);
    }

    @PostMapping("/signin")
    public ResponseEntity<@NonNull LoginResponseDto> signIn(@Valid @RequestBody LoginRequestDto loginRequestDto){
        LoginResponseDto loginResponseDto = authService.signIn(loginRequestDto);
        return ResponseEntity.ok(loginResponseDto);
    }
}
