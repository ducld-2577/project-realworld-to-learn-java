package com.example.realworld.service;

import com.example.realworld.dto.ProfileResponseDTO;
import com.example.realworld.dto.RegistrationReqDTO;
import com.example.realworld.dto.UpdateUserRequestDTO;
import com.example.realworld.dto.UpdateUserResponseDTO;
import com.example.realworld.model.User;
import com.example.realworld.model.UserFollow;
import com.example.realworld.model.UserFollowId;
import com.example.realworld.repository.UserFollowRepository;
import com.example.realworld.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserFollowRepository userFollowRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserFollowRepository userFollowRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userFollowRepository = userFollowRepository;
    }

    public User saveUser(RegistrationReqDTO registrationReqDto) {
        String encodedPassword = passwordEncoder.encode(registrationReqDto.getPassword());

        User newUser = new User();
        newUser.setEmail(registrationReqDto.getEmail());
        newUser.setUsername(registrationReqDto.getUsername());
        newUser.setPassword(encodedPassword);

        return userRepository.save(newUser);
    }

    @Transactional
    public UpdateUserResponseDTO updateUser(String currentUsername, UpdateUserRequestDTO request) {
        Optional<User> optionalUser = userRepository.findByUsername(currentUsername);

        if (!optionalUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User user = optionalUser.get();
        UpdateUserRequestDTO.UserDTO requestData = request.getUser();

        if (requestData.getEmail() != null) {
            user.setEmail(requestData.getEmail());
        }
        if (requestData.getUsername() != null) {
            user.setUsername(requestData.getUsername());
        }
        if (requestData.getPassword() != null) {
            user.setPassword(passwordEncoder.encode(requestData.getPassword()));
        }
        if (requestData.getBio() != null) {
            user.setBio(requestData.getBio());
        }
        if (requestData.getImage() != null) {
            user.setImage(requestData.getImage());
        }

        userRepository.save(user);

        UpdateUserResponseDTO.UserDTO responseUser = new UpdateUserResponseDTO.UserDTO(
                user.getEmail(), user.getUsername(), user.getBio(), user.getImage());

        return new UpdateUserResponseDTO(responseUser);
    }

    public ProfileResponseDTO getUserProfile(String username, Authentication authentication) {
        Optional<User> targetUserOpt = userRepository.findByUsername(username);

        if (targetUserOpt.isEmpty()) {
            return null;
        }

        User targetUser = targetUserOpt.get();
        boolean isFollowing = false;

        return new ProfileResponseDTO(targetUser.getUsername(), targetUser.getBio(),
                targetUser.getImage(), isFollowing);
    }

    public ProfileResponseDTO followUser(String usernameToFollow, Authentication authentication) {
        String currentUsername = authentication.getName();
        Optional<User> currentUserOpt = userRepository.findByUsername(currentUsername);
        Optional<User> targetUserOpt = userRepository.findByUsername(usernameToFollow);

        if (currentUserOpt.isEmpty() || targetUserOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        User currentUser = currentUserOpt.get();
        User targetUser = targetUserOpt.get();

        Optional<UserFollow> existingFollow =
                userFollowRepository.findByFollowerAndFollowing(currentUser, targetUser);

        if (existingFollow.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Already following this user");
        }

        UserFollowId userFollowId = new UserFollowId(currentUser.getId(), targetUser.getId());
        UserFollow follow = new UserFollow();
        follow.setId(userFollowId);
        follow.setFollower(currentUser);
        follow.setFollowing(targetUser);
        userFollowRepository.save(follow);

        return new ProfileResponseDTO(targetUser.getUsername(), targetUser.getBio(),
                targetUser.getImage(), true);
    }

    public ProfileResponseDTO unfollowUser(String username, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Optional<User> targetUser = userRepository.findByUsername(username);

        if (targetUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        UserFollowId userFollowId = new UserFollowId(currentUser.getId(), targetUser.get().getId());
        Optional<UserFollow> existingFollow = userFollowRepository.findById(userFollowId);
        if (existingFollow.isPresent()) {
            userFollowRepository.delete(existingFollow.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not following");
        }

        return new ProfileResponseDTO(targetUser.get().getUsername(), targetUser.get().getBio(),
                targetUser.get().getImage(), false);
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
