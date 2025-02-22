package com.example.UserManagementService.user.service;

import com.example.UserManagementService.exception.UserNotFoundException;
import com.example.UserManagementService.user.model.User;
import com.example.UserManagementService.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserService implements UserServiceInterface{

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        // Basic validation
        Optional.ofNullable(user)
                .orElseThrow(() -> new IllegalArgumentException("User object cannot be null"));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updatedUser) {
        Optional.ofNullable(updatedUser)
                .orElseThrow(() -> new IllegalArgumentException("Updated user cannot be null"));

        return userRepository.findById(id)
                .map(existingUser -> {
//                    existingUser.setUsername(updatedUser.getUsername());
                    existingUser.setEmail(updatedUser.getEmail());
                    existingUser.setPassword(updatedUser.getPassword());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public void deleteUser(Long id){
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new UserNotFoundException("User not found with id: " + id);
        }
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

//    @Override
//    public void changePassword(ChangePasswordRequest request, Principal connectedUser) {
//        var user = (User) ((UsernamePasswordAuthenticationToken) connectedUser).getPrincipal();
//
//    }
}