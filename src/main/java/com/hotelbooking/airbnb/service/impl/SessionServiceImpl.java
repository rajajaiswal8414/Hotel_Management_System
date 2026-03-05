package com.hotelbooking.airbnb.service.impl;

import com.hotelbooking.airbnb.entity.Session;
import com.hotelbooking.airbnb.entity.User;
import com.hotelbooking.airbnb.repository.SessionRepository;
import com.hotelbooking.airbnb.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {

    private final SessionRepository sessionRepository;
    private static final int SESSION_LIMIT = 2;

    @Override
    @Transactional
    public void generateNewSession(User user, String refreshToken) {

        List<Session> userSessions = sessionRepository.findByUser(user);

        // If limit reached → remove oldest session
        if (userSessions.size() >= SESSION_LIMIT) {

            userSessions.sort(Comparator.comparing(Session::getLastUsedAt));

            Session oldestSession = userSessions.getFirst();

            sessionRepository.delete(oldestSession);
        }

        // Create new session
        Session newSession = Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();

        sessionRepository.save(newSession);
    }

    @Override
    @Transactional
    public boolean validateSession(String refreshToken) {
        return sessionRepository.findByRefreshToken(refreshToken)
                .map(session -> {
                    session.setLastUsedAt(LocalDateTime.now()); // Manually update
                    sessionRepository.save(session);
                    return true;
                })
                .orElse(false);
    }
}
