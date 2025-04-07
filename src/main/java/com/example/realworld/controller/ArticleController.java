package com.example.realworld.controller;

import com.example.realworld.dto.ArticleListResponseDTO;
import com.example.realworld.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public ResponseEntity<ArticleListResponseDTO> listArticles(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String favorited,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset,
            Authentication authentication
    ) {
        Optional<String> tagOptional = Optional.ofNullable(tag);
        Optional<String> authorOptional = Optional.ofNullable(author);
        Optional<String> favoritedOptional = Optional.ofNullable(favorited);

        ArticleListResponseDTO response = articleService.getArticles(tagOptional, authorOptional, favoritedOptional, limit, offset, authentication);
        return ResponseEntity.ok(response);
    }

}
