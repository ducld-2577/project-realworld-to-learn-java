package com.example.realworld.dto;

import javax.validation.constraints.NotBlank;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDTO {
    private Long id;
    @NotBlank
    private String body;
    private String createdAt;
    private String updatedAt;
    private AuthorDTO author;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class AuthorDTO {
        private String username;
        private String bio;
        private String image;
        private boolean following;
    }
}
