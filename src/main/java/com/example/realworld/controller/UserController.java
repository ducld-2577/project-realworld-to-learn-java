package com.example.realworld.controller;

import com.example.realworld.dto.LoginResponseDTO.UserResponseDTO;
import com.example.realworld.dto.RegistrationReqDTO;
import com.example.realworld.dto.UpdateUserRequestDTO;
import com.example.realworld.dto.UpdateUserResponseDTO;
import com.example.realworld.model.User;
import com.example.realworld.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.Map;


@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(
            @Valid @RequestBody RegistrationReqDTO registrationReqDto) {
        System.out.println("Registering......: " + registrationReqDto.getPassword());
        User savedUser = userService.saveUser(registrationReqDto);

        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        return userService.getCurrentUserResponse(userDetails);
    }

    @PutMapping()
    public ResponseEntity<UpdateUserResponseDTO> updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UpdateUserRequestDTO request) {
        UpdateUserResponseDTO updatedUser = userService.updateUser(userDetails.getUsername(), request);
        return ResponseEntity.ok(updatedUser);
    }
}
