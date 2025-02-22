package com.example.redis;

import com.example.DTO.UserSession;
import org.springframework.data.repository.CrudRepository;

public interface UserSessionRepo extends CrudRepository<UserSession, String> {



}
