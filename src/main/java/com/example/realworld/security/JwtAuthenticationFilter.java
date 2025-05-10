package com.example.realworld.security;

import com.example.realworld.service.JwtService;
import com.example.realworld.service.JwtUserService;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final JwtUserService jwtUserService;

    public JwtAuthenticationFilter(JwtService jwtService, JwtUserService jwtUserService) {
        this.jwtService = jwtService;
        this.jwtUserService = jwtUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {
        String token = extractJwtFromRequest(request);

        if (token != null && jwtService.validateToken(token)) {
            String email = jwtService.extractEmail(token);

            UserDetails userDetails = jwtUserService.loadUserByUsername(email);
            if (userDetails != null) {
                Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
                Authentication authentication =
                        new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request, response);
    }

    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
