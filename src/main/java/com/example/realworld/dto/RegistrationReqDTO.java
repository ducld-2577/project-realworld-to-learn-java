package com.example.realworld.dto;

import lombok.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationReqDTO {

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Invalid email format")
    private String email;

    @Size(min = 8, message = "Username must be at least 8 characters long")
    private String username;

    @Size(min = 8, message = "Password must be at least 8 characters long")
    private String password;
}
