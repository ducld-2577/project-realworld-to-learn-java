package com.example.realworld.service;

import com.example.realworld.dto.RegistrationReqDTO;
import com.example.realworld.model.User;
import com.example.realworld.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User saveUser(RegistrationReqDTO registrationReqDto) {
        String encodedPassword = passwordEncoder.encode(registrationReqDto.getPassword());

        User newUser = new User();
        newUser.setEmail(registrationReqDto.getEmail());
        newUser.setUsername(registrationReqDto.getUsername());
        newUser.setPassword(encodedPassword);

        return userRepository.save(newUser);
    }

    @Override
    public User loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public ResponseEntity<Map<String, Object>> getCurrentUserResponse(UserDetails userDetails) {
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

            response.put("user", userData);

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }
}
