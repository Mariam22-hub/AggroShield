package com.example.service;

import com.example.DTO.UserSession;
import com.example.redis.UserSessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserSessionService {

    private final UserSessionRepo userSessionRepo;
    public Optional<UserSession> getUserSession(String userID) {
        return userSessionRepo.findById(userID);
    }

}
