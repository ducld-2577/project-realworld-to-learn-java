package com.example.realworld.controller;

import com.example.realworld.dto.RegistrationReqDTO;
import com.example.realworld.model.User;
import com.example.realworld.service.UserService;
import com.example.realworld.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegistrationReqDTO registrationReqDto) {
        String encodedPassword = passwordEncoder.encode(registrationReqDto.getPassword());

        User newUser = new User();
        newUser.setEmail(registrationReqDto.getEmail());
        newUser.setUsername(registrationReqDto.getUsername());
        newUser.setPassword(encodedPassword);

        User savedUser = userService.saveUser(newUser);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<User> userOptional = userRepository.findByUsername(userDetails.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            Map<String, Object> response = new HashMap<>();
            Map<String, Object> userData = new HashMap<>();

            userData.put("email", user.getEmail());
            userData.put("username", user.getUsername());
            userData.put("bio", user.getBio());
            userData.put("image", user.getImage());
            userData.put("token", "jwt_token_here");
            response.put("user", userData);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
