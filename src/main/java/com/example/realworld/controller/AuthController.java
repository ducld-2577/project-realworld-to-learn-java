package com.example.realworld.controller;

import com.example.realworld.dto.LoginRequestDTO;
import com.example.realworld.dto.LoginResponseDTO;
import com.example.realworld.model.User;
import com.example.realworld.repository.UserRepository;
import com.example.realworld.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        Optional<User> userOpt = userRepository.findByEmail(request.getUser().getEmail());

        if (userOpt.isEmpty()) {
            System.out.println("User not found.");
            return ResponseEntity.badRequest().body("Invalid email or password");
        }

        if (!passwordEncoder.matches(request.getUser().getPassword(), userOpt.get().getPassword())) {
            System.out.println("Password does not match.");
            return ResponseEntity.badRequest().body("Invalid email or password");
        }

        String token = jwtService.generateToken(userOpt.get());
        return ResponseEntity.ok(new LoginResponseDTO(new LoginResponseDTO.UserResponseDTO(userOpt.get().getEmail(), token)));
    }
}
