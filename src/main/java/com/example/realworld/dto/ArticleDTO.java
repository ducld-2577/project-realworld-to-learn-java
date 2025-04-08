package com.example.realworld.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleDTO {
    private String slug;
    private String title;
    private String description;
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

