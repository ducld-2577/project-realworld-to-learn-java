package com.example.realworld.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProfileResponseDTO {
    private String username;
    private String bio;
    private String image;
    private boolean following;
}
