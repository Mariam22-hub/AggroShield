package com.example.UserManagementService.user.service;

import com.example.UserManagementService.user.dto.ChangePasswordRequest;
import com.example.UserManagementService.user.model.User;

import java.security.Principal;
import java.util.List;

public interface UserServiceInterface {
    public User addUser(User user);
    public User updateUser(Long id, User updatedUser);
    public User getUserById(Long id);
    public void deleteUser(Long id);
    public List<User> getAllUsers();
//    void changePassword(ChangePasswordRequest request, Principal connectedUser);
}