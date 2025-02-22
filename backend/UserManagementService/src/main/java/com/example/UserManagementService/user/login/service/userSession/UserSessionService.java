package com.example.UserManagementService.user.login.service.userSession;

import com.example.UserManagementService.DTO.UserSession;
import com.example.UserManagementService.user.login.repository.UserSessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserSessionService implements UserSessionServiceInterface{
    private final UserSessionRepository userSessionRepository;

    @Override
    public Optional<UserSession> getUserSession(String userId) {
        return userSessionRepository.findById(userId);
    }

    @Override
    public void saveUserSession(UserSession userSession) {
        userSessionRepository.save(userSession);
    }

}