package com.example.realworld.dto;

import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleListResponseDTO {
    private List<ArticleDTO> articles;
    private int articlesCount;
}
