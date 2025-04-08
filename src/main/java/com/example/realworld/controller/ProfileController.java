package com.example.realworld.controller;

import com.example.realworld.dto.ProfileResponseDTO;
import com.example.realworld.service.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getProfile(@PathVariable String username,
            Authentication authentication) {
        ProfileResponseDTO profile = userService.getUserProfile(username, authentication);

        if (profile == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(profile);
    }

    @PostMapping("/{username}/follow")
    public ResponseEntity<ProfileResponseDTO> followUser(@PathVariable String username,
            Authentication authentication) {

        ProfileResponseDTO profile = userService.followUser(username, authentication);
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping("/{username}/follow")
    public ResponseEntity<ProfileResponseDTO> unfollowUser(@PathVariable String username,
            Authentication authentication) {

        ProfileResponseDTO profile = userService.unfollowUser(username, authentication);

        return ResponseEntity.ok(profile);
    }
}
