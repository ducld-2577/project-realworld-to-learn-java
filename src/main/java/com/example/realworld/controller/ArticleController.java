package com.example.realworld.controller;

import com.example.realworld.dto.ArticleDTO;
import com.example.realworld.dto.ArticleListResponseDTO;
import com.example.realworld.dto.CommentDTO;
import com.example.realworld.dto.CommentListResponseDTO;
import com.example.realworld.dto.UpdateArticleRequestDTO;
import com.example.realworld.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



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
            @RequestParam(defaultValue = "0") int offset, Authentication authentication) {
        Optional<String> tagOptional = Optional.ofNullable(tag);
        Optional<String> authorOptional = Optional.ofNullable(author);
        Optional<String> favoritedOptional = Optional.ofNullable(favorited);

        ArticleListResponseDTO response = articleService.getArticles(tagOptional, authorOptional,
                favoritedOptional, limit, offset, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/feed")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleListResponseDTO> feedArticles(
            @RequestParam(required = false) String tag,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String favorited,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int offset, Authentication authentication) {
        Optional<String> tagOptional = Optional.ofNullable(tag);
        Optional<String> authorOptional = Optional.ofNullable(author);
        Optional<String> favoritedOptional = Optional.ofNullable(favorited);

        ArticleListResponseDTO response = articleService.getFeedArticles(tagOptional,
                authorOptional, favoritedOptional, limit, offset, authentication);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleDTO> getArticle(@PathVariable String slug,
            Authentication authentication) {
        ArticleDTO article = articleService.getArticle(slug, authentication);

        return ResponseEntity.ok(article);
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleDTO> createArticle(@Valid @RequestBody ArticleDTO articleDTO,
            Authentication authentication) {
        ArticleDTO createdArticle = articleService.createArticle(articleDTO, authentication);
        return ResponseEntity.status(201).body(createdArticle);
    }

    @PutMapping("/{slug}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable String slug,
            @Valid @RequestBody UpdateArticleRequestDTO articleDTO, Authentication authentication) {
        ArticleDTO updatedArticle = articleService.updateArticle(slug, articleDTO, authentication);
        return ResponseEntity.ok(updatedArticle);
    }

    @DeleteMapping("/{slug}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteArticle(@PathVariable String slug,
            Authentication authentication) {
        articleService.deleteArticle(slug, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{slug}/comments")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CommentDTO> addComment(@PathVariable String slug,
            @Valid @RequestBody CommentDTO commentDTO, Authentication authentication) {
        CommentDTO comment = articleService.addComment(slug, commentDTO, authentication);
        return ResponseEntity.status(201).body(comment);
    }

    @GetMapping("/{slug}/comments")
    public ResponseEntity<CommentListResponseDTO> getComments(@PathVariable String slug,
            Authentication authentication) {
        CommentListResponseDTO comments = articleService.getComments(slug, authentication);
        return ResponseEntity.ok(comments);
    }

    @DeleteMapping("/{slug}/comments/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteComment(@PathVariable String slug, @PathVariable Long id,
            Authentication authentication) {
        articleService.deleteComment(slug, id, authentication);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("{slug}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleDTO> favoriteArticle(@PathVariable String slug,
            Authentication authentication) {
        ArticleDTO article = articleService.favoriteArticle(slug, authentication);
        return ResponseEntity.ok(article);
    }


    @DeleteMapping("{slug}/favorite")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ArticleDTO> unfavoriteArticle(@PathVariable String slug,
            Authentication authentication) {
        ArticleDTO article = articleService.unfavoriteArticle(slug, authentication);
        return ResponseEntity.ok(article);
    }
}
