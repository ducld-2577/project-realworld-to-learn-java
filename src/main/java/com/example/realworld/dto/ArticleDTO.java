package com.example.realworld.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {
    private String slug;
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    @NotBlank
    private String body;
    private List<String> tagList;
    private String createdAt;
    private String updatedAt;
    private boolean favorited;
    private int favoritesCount;
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

