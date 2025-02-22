package com.example.UserManagementService.user.login.service.login;


import com.example.UserManagementService.DTO.UserSession;
import com.example.UserManagementService.user.dto.LoginRequest;
import com.example.UserManagementService.user.dto.TokenDTO;
import com.example.UserManagementService.user.login.service.JWT.JwtGeneratorService;
import com.example.UserManagementService.user.login.service.JWT.JwtGeneratorServiceInterface;
import com.example.UserManagementService.user.login.service.userSession.UserSessionService;
import com.example.UserManagementService.user.login.service.userSession.UserSessionServiceInterface;
import com.example.UserManagementService.user.model.User;
import com.example.UserManagementService.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor

public class LoginService implements LoginServiceInterface{

    private final UserRepository userRepository;
    private final JwtGeneratorServiceInterface jwtGeneratorService;
    private final UserSessionServiceInterface userSessionService;
    private final PasswordEncoder passwordEncoder=new BCryptPasswordEncoder();

    @Override
    public TokenDTO login(LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("user not found"));
        validateUserCredentials(loginRequest, user);
///new

        if (!user.getRole().name().equalsIgnoreCase(loginRequest.getRole())) {
            if (user.getRole().name().equalsIgnoreCase("ADMIN")) {
                throw new RuntimeException("Admin cannot log in as User");
            } else if (user.getRole().name().equalsIgnoreCase("USER")) {
                throw new RuntimeException("User cannot log in as Admin");
            } else {
                throw new RuntimeException("Role mismatch");
            }
        }

        TokenDTO token=buildTokenDTO(user);
        saveSession(String.valueOf(user.getId()),token.getToken());

        return token;
    }

    private TokenDTO buildTokenDTO(User user) {
        String token = jwtGeneratorService.generateToken(user);
        return new TokenDTO(token);
    }

    private void validateUserCredentials(LoginRequest loginDTO, User user) {

        if (!validatePassword(loginDTO.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
    }

    private boolean validatePassword(String password, String encodedPassword) {
        return passwordEncoder.matches(password, encodedPassword);
    }

    private void saveSession(String userId, String jwtToken) {

        UserSession userSession = UserSession
                .builder()
                .userID(userId)
                .jwtToken(jwtToken)
                .build();

        userSessionService.saveUserSession(userSession);
    }

}