package com.example.UserManagementService.user.login.service.register;

import com.example.UserManagementService.DTO.UserSession;
import com.example.UserManagementService.auth.dto.RegisterRequest;
import com.example.UserManagementService.email.EmailTemplateName;
import com.example.UserManagementService.email.service.EmailServiceInterface;
import com.example.UserManagementService.user.dto.TokenDTO;
import com.example.UserManagementService.user.login.service.JWT.JwtGeneratorService;
import com.example.UserManagementService.user.login.service.JWT.JwtGeneratorServiceInterface;
import com.example.UserManagementService.user.login.service.userSession.UserSessionService;
import com.example.UserManagementService.user.login.service.userSession.UserSessionServiceInterface;
import com.example.UserManagementService.user.model.Role;
import com.example.UserManagementService.user.model.User;
import com.example.UserManagementService.user.model.VerificationCode;
import com.example.UserManagementService.user.repository.UserRepository;
import com.example.UserManagementService.user.repository.VerificationCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterService implements RegisterServiceInterface{

    private final VerificationCodeRepository verificationCodeRepository;
    private final EmailServiceInterface emailService;
    private final UserRepository userRepository;
    private final JwtGeneratorServiceInterface jwtGeneratorService;
    private final UserSessionServiceInterface userSessionService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Value("${mailing.frontend.activation.url}")
    String activationUrl;

    @Override
    public TokenDTO register(RegisterRequest registerRequest) throws MessagingException {
        // Validate email uniqueness
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        if (userRepository.existsByUsername(registerRequest.getEmail())) {
            throw new RuntimeException("Username already in use");
        }

        // Create and save user
        User user = createUser(registerRequest);
        userRepository.save(user);
        sendValidationEmail(user);

        // Generate token
        TokenDTO token = buildTokenDTO(user);

        // Save session
        saveSession(String.valueOf(user.getId()), token.getToken());

        return token;
    }

    private User createUser(RegisterRequest registerRequest) {
        return User.builder()
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .email(registerRequest.getEmail())
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .phoneNumber(registerRequest.getPhoneNumber())
                .carModel(registerRequest.getCarModel())
                .vehicleNumber(registerRequest.getVehicleNumber())
                .role(Role.valueOf("USER"))
                .build();
    }

    private TokenDTO buildTokenDTO(User user) {
        String token = jwtGeneratorService.generateToken(user);
        return new TokenDTO(token);
    }

    private void saveSession(String userId, String jwtToken) {
        UserSession userSession = UserSession.builder()
                .userID(userId)
                .jwtToken(jwtToken)
                .build();
        userSessionService.saveUserSession(userSession);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newActivationToken = generateAndSaveActivationCode(user);
        // send email
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                EmailTemplateName.ACTIVATE_ACCOUNT,
                activationUrl,
                newActivationToken,
                "Account Activation"
        );
    }

    private String generateAndSaveActivationCode(User user) {
        String generatedCode = generateActivationCode(6);
        var verificationCode = VerificationCode.builder()
                .code(generatedCode)
                .createdAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(10))
                .user(user)
                .build();

        verificationCodeRepository.save(verificationCode);
        return generatedCode;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder sb = new StringBuilder();
        SecureRandom random = new SecureRandom();

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            sb.append(characters.charAt(index));
        }
        return sb.toString();
    }

    @Override
    @Transactional
    public void activateAccount(String incoming_code) throws MessagingException {
        VerificationCode savedVerificationCode = verificationCodeRepository.findByCode(incoming_code)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (LocalDateTime.now().isAfter(savedVerificationCode.getExpiresAt())) {
            sendValidationEmail(savedVerificationCode.getUser());
            throw new RuntimeException("Activation Code has expired. A new code has been send to the same email address");
        }

        var user = userRepository.findById(savedVerificationCode.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setEnabled(true);
        userRepository.save(user);

        savedVerificationCode.setValidatedAt(LocalDateTime.now());
        verificationCodeRepository.save(savedVerificationCode);
    }
}