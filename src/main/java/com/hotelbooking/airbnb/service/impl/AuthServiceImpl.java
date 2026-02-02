package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.dto.LoginRequestDto;
import com.hotelbooking.airbnb.dto.LoginResponseDto;
import com.hotelbooking.airbnb.dto.SignupRequestDto;
import com.hotelbooking.airbnb.dto.UserDto;
import com.hotelbooking.airbnb.entity.User;
import com.hotelbooking.airbnb.exception.APIException;
import com.hotelbooking.airbnb.repository.UserRepository;
import com.hotelbooking.airbnb.security.jwt.JwtUtils;
import com.hotelbooking.airbnb.security.services.UserDetailsImpl;
import com.hotelbooking.airbnb.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public UserDto signUp(SignupRequestDto signupRequestDto) {
        Optional<User> user = userRepository.findByEmail(signupRequestDto.getEmail());
        if(user.isPresent()){
            throw new APIException(HttpStatus.CONFLICT, "Email already registered");
        }

        User newUser = modelMapper.map(signupRequestDto, User.class);
        newUser.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));

        User savedUser = userRepository.save(newUser);
        log.info("New user registered: {}", savedUser.getEmail());

        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public LoginResponseDto signIn(LoginRequestDto loginRequestDto) {
        Authentication authentication;
        try{
            authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword()));
        } catch (AuthenticationException exception){
            log.warn("Authentication failed for user {}", loginRequestDto.getEmail());
            throw new APIException(HttpStatus.UNAUTHORIZED, "Invalid email and password");
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (userDetails == null) {
            throw new APIException(HttpStatus.UNAUTHORIZED, "Authentication failed");
        }

        String accessToken = jwtUtils.generateToken(userDetails);

        return new LoginResponseDto(userDetails.getId(), accessToken, null);
    }
}
