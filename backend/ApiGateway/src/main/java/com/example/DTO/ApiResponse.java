package com.example.DTO;


import lombok.Data;

import static com.example.filter.JwtAuthenticationFilter.UNAUTHORIZED_ACCESS;

@Data

public class ApiResponse {
    private String message;
    private Object details;

    public static ApiResponse buildUnauthorizedAccessObject(){
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(UNAUTHORIZED_ACCESS);
        return apiResponse;
    }
}