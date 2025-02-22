package com.example.UserManagementService.auth;

import com.example.UserManagementService.user.model.User;
import com.example.UserManagementService.user.service.UserServiceInterface;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

@Component
public class UserSecurityContextFilter extends OncePerRequestFilter {

    private final UserServiceInterface userService;

    public UserSecurityContextFilter(UserServiceInterface userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // These were populated in the JWT filter in the Gateway
        String userId = request.getHeader("X-User-Id");
        String role = request.getHeader("X-User-Role");

        if (userId != null && role != null) {
            // Fetch the user from the database using the userId
            User user = userService.getUserById(Long.parseLong(userId));
            if (user != null) {
                SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + role);
                // Populate the SecurityContext with the User object as the principal
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        user, null, Collections.singletonList(authority));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }
}