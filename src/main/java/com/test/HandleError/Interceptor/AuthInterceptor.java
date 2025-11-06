package com.test.HandleError.Interceptor;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;

import com.test.HandleError.Utils.JwtUtils;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtils jwtUtils;

    public AuthInterceptor(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
            @NonNull Object handler) throws Exception {
        String autherHeader = request.getHeader("Authorization");
        if (autherHeader == null || !autherHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized header");
        }

        String token = autherHeader.substring(7);
        if(jwtUtils.isTokenExpired(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unautgorized token expired");
        }
        if(!jwtUtils.isTokenValid(token)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,"Unautgorized token invalid");
        }
        String email = jwtUtils.getEmailFromToken(token);
        
        if (email == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized email");
        }
        request.setAttribute("email", email);
        return true;
    }
}
