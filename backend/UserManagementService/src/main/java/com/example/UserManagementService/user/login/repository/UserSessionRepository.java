package com.example.UserManagementService.user.login.repository;

import com.example.UserManagementService.DTO.UserSession;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepository extends CrudRepository<UserSession, String> {

}