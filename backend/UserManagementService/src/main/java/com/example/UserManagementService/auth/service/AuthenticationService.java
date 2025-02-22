//package com.example.UserManagementService.auth.service;
//
//import com.example.UserManagementService.auth.dto.AuthenticationRequest;
//import com.example.UserManagementService.auth.dto.AuthenticationResponse;
//import com.example.UserManagementService.auth.dto.RegisterRequest;
//import com.example.UserManagementService.email.service.EmailService;
//import com.example.UserManagementService.email.EmailTemplateName;
//import com.example.UserManagementService.email.service.EmailServiceInterface;
//import com.example.UserManagementService.exception.UserAlreadyExists;
//import com.example.UserManagementService.user.model.VerificationCode;
//import com.example.UserManagementService.user.repository.UserRepository;
//import com.example.UserManagementService.config.JwtService;
//import com.example.UserManagementService.user.model.Role;
//import com.example.UserManagementService.user.model.User;
//import com.example.UserManagementService.user.repository.VerificationCodeRepository;
//import jakarta.mail.MessagingException;
//import jakarta.transaction.Transactional;
//import lombok.Data;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//
//import java.security.SecureRandom;
//import java.time.LocalDateTime;
//import java.util.HashMap;
//
//
//@Service
//@RequiredArgsConstructor
////@Builder
//@Data
//public class AuthenticationService implements AuthenticationServiceInterface{
//
//    private final UserRepository repository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtService jwtService;
//    private final AuthenticationManager authenticationManager;
//    private final VerificationCodeRepository verificationCodeRepository;
//    private final EmailServiceInterface emailService;
//
//    @Value("${mailing.frontend.activation.url}")
//    String activationUrl;
//
//    public AuthenticationResponse register(RegisterRequest request) throws MessagingException {
//        repository.findByEmail(request.getEmail()).ifPresent(s ->{
//            throw new UserAlreadyExists("User with this email already exists");
//        });
//
//        User user = User.builder()
//                .firstName(request.getFirstName())
//                .lastName(request.getLastName())
//                .email(request.getEmail())
//                .phoneNumber(request.getPhoneNumber())
//                .password(passwordEncoder.encode(request.getPassword()))
//                .role(Role.valueOf(request.getRole()))
//                .build();
//
//        repository.save(user);
//        /////
////        sendValidationEmail(user);
//
//        var jwtToken= JwtService.generateToken(user);
//        return  AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//    private void sendValidationEmail(User user) throws MessagingException {
//        var newActivationToken = generateAndSaveActivationCode(user);
//        // send email
//        emailService.sendEmail(
//                user.getEmail(),
//                user.fullName(),
//                EmailTemplateName.ACTIVATE_ACCOUNT,
//                activationUrl,
//                newActivationToken,
//                "Account Activation"
//        );
//    }
//
//    private String generateAndSaveActivationCode(User user) {
//        String generatedCode = generateActivationCode(6);
//        var verificationCode = VerificationCode.builder()
//                .code(generatedCode)
//                .createdAt(LocalDateTime.now())
//                .expiresAt(LocalDateTime.now().plusMinutes(10))
//                .user(user)
//                .build();
//
//        verificationCodeRepository.save(verificationCode);
//        return generatedCode;
//    }
//
//    private String generateActivationCode(int length) {
//        String characters = "0123456789";
//        StringBuilder sb = new StringBuilder();
//        SecureRandom random = new SecureRandom();
//
//        for (int i = 0; i < length; i++) {
//            int index = random.nextInt(characters.length());
//            sb.append(characters.charAt(index));
//        }
//        return sb.toString();
//    }
//
//    public AuthenticationResponse authenticate(AuthenticationRequest request) {
//
//        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
//                request.getEmail(),
//                request.getPassword())
//        );
//
//        var user = repository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        var claims = new HashMap<String, Object>();
//        claims.put("FullName", user.fullName());
//
////        var jwtToken= JwtService.generateToken(user);
//        var jwtToken= JwtService.generateToken(claims, user);
//        return  AuthenticationResponse.builder()
//                .token(jwtToken)
//                .build();
//    }
//
//    @Transactional
//    public void activateAccount(String incoming_code) throws MessagingException {
//        VerificationCode savedVerificationCode = verificationCodeRepository.findByCode(incoming_code)
//                .orElseThrow(() -> new RuntimeException("Invalid token"));
//
//        if (LocalDateTime.now().isAfter(savedVerificationCode.getExpiresAt())) {
//            sendValidationEmail(savedVerificationCode.getUser());
//            throw new RuntimeException("Activation Code has expired. A new code has been send to the same email address");
//        }
//
//        var user = repository.findById(savedVerificationCode.getUser().getId())
//                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//
//        user.setEnabled(true);
//        repository.save(user);
//
//        savedVerificationCode.setValidatedAt(LocalDateTime.now());
//        verificationCodeRepository.save(savedVerificationCode);
//    }
//}