package com.example.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Data
@AllArgsConstructor
@RedisHash("user-session")

public class UserSession implements Serializable {

    @Id
    private String userID;
    private String jwtToken;

}
