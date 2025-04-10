package com.example.realworld.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateArticleRequestDTO {
    private String slug;
    private String title;
    private String description;
    private String body;
    private List<String> tagList;
}

