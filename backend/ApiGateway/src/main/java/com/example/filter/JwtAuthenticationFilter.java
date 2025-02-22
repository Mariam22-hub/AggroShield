package com.example.filter;


import com.example.DTO.ApiResponse;
import com.example.DTO.CustomUserDetails;
import com.example.DTO.UserSession;
import com.example.service.UserSessionService;
import com.example.util.JWTUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.core.io.buffer.DataBuffer;
import java.util.List;
import java.util.Optional;

public class JwtAuthenticationFilter implements WebFilter {


    private final UserSessionService userSessionService;
    private final String secretKey;
    private final ObjectMapper objectMapper;

    public static final String USERID = "X-User-Id";
    public static final String ROLE_FIELD = "X-User-Role";
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String AUTHORIZATION_HEADER = "authorization";
    public static final String JWT_TOKEN_PREFIX = "Bearer ";
    public static final String UNAUTHORIZED_ACCESS = "unauthorized access";
    public static final String LOGIN_URL = "/login";
    public static final String REGISTER_URL = "/register";

    public JwtAuthenticationFilter(UserSessionService userSessionService, String secretKey) {
        this.userSessionService = userSessionService;
        this.secretKey = secretKey;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {

        if(exchange.getRequest().getPath().value().endsWith(LOGIN_URL)
                || exchange.getRequest().getPath().value().endsWith(REGISTER_URL)) {

            return chain.filter(exchange);
        }
        HttpHeaders httpHeaders=exchange.getRequest().getHeaders();
        List<String> authorization = httpHeaders.get(AUTHORIZATION_HEADER);

        if (isAuthHeaderValid(authorization)) {
            String token = authorization.get(0).replace(JWT_TOKEN_PREFIX, "");
            try {
                if (JWTUtils.isTokenValid(token, secretKey)) {
                    Claims claims = JWTUtils.extractAllClaims(token, secretKey);
                    String userid = claims.getSubject();
                    String role = claims.get("role", String.class);

                    return validateSessionToken(token,getUserSession(userid),exchange)
                        .switchIfEmpty(Mono.defer(()->{
                            String tokenRole = claims.get("role").toString();
                            SecurityContext securityContext = getSecurityContext(userid, tokenRole);

                            return chain.filter(exchange.mutate()
                                    .request(exchange.getRequest().mutate()
                                            .header(USERID, userid)
                                            .header("X-User-Role", role)
                                            .build())
                                    .build()).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
                        }));
                }

            }
            catch (Exception e){
                return handleUnauthorizedResponse(exchange);
            }
        }
        return handleUnauthorizedResponse(exchange);

    }

    private SecurityContext getSecurityContext(String userId, String tokenRole) {
        UserDetails user = new CustomUserDetails(userId, List.of(new SimpleGrantedAuthority(tokenRole)));

        List<SimpleGrantedAuthority> authorities = user.getAuthorities().stream()
                .map(role -> new SimpleGrantedAuthority(ROLE_PREFIX + role)).toList();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user, null, authorities);

        SecurityContext context = new SecurityContextImpl(authenticationToken);
        return context;
    }


    private Mono<Void> validateSessionToken(String authToken, Optional<UserSession> session, ServerWebExchange exchange) {
        if (session.isEmpty() || !session.get().getJwtToken().equals(authToken)) {
            return handleUnauthorizedResponse(exchange);
        }
        return Mono.empty();  // Return an empty Mono if the token is valid (no action needed)
    }

     private Optional<UserSession> getUserSession (String userID) {
        return userSessionService.getUserSession(userID);

     }



    private Mono<Void> handleUnauthorizedResponse(ServerWebExchange exchange) {
        ServerHttpResponse response = mutateResponseToUnauthorized(exchange.getResponse());
        return response.setComplete();
    }

    private ServerHttpResponse mutateResponseToUnauthorized(ServerHttpResponse originalResponse) {

        try {
            DataBuffer dataBuffer = originalResponse.bufferFactory().wrap(objectMapper.writeValueAsBytes(ApiResponse.buildUnauthorizedAccessObject()));
            originalResponse.getHeaders().add("content-type", "application/json");
            originalResponse.setStatusCode(HttpStatusCode.valueOf(401));
            originalResponse.writeWith(Mono.just(dataBuffer)).subscribe();
        }
        catch (JsonProcessingException ex) {
            ex.printStackTrace();
        }
        return originalResponse;
    }

    private boolean isAuthHeaderValid(List<String> authorization) {
        return authorization != null && ! authorization.isEmpty()
                && authorization.get(0).startsWith(JWT_TOKEN_PREFIX);
    }
}