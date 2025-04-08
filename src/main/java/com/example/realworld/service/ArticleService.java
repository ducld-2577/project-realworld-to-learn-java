package com.example.realworld.service;

import com.example.realworld.dto.ArticleDTO;
import com.example.realworld.dto.ArticleListResponseDTO;
import com.example.realworld.model.Article;
import com.example.realworld.model.Tag;
import com.example.realworld.model.User;
import com.example.realworld.repository.ArticleRepository;
import com.example.realworld.repository.UserRepository;
import com.example.realworld.repository.UserFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.Authentication;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowRepository userFollowRepository;

    public ArticleListResponseDTO getArticles(Optional<String> tag, Optional<String> author,
            Optional<String> favorited, Integer limit, Integer offset,
            Authentication authentication) {
        List<Article> articles = articleRepository.findArticlesWithFilters(tag.orElse(null),
                author.orElse(null), favorited.orElse(null), offset, limit);

        final User currentUser;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        } else {
            currentUser = null;
        }

        List<ArticleDTO> articleDTOList = articles.stream()
                .map(article -> convertToDTO(article, currentUser)).collect(Collectors.toList());

        return new ArticleListResponseDTO(articleDTOList, articleDTOList.size());
    }

    public ArticleListResponseDTO getFeedArticles(Optional<String> tag, Optional<String> author,
            Optional<String> favorited, Integer limit, Integer offset,
            Authentication authentication) {
        final User currentUser;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        } else {
            return new ArticleListResponseDTO(List.of(), 0);
        }

        List<Article> articles = articleRepository.findArticlesOnFeedWithFilters(tag.orElse(null),
                author.orElse(null), favorited.orElse(null), offset, limit, currentUser);

        List<ArticleDTO> articleDTOList = articles.stream()
                .map(article -> convertToDTO(article, currentUser)).collect(Collectors.toList());

        return new ArticleListResponseDTO(articleDTOList, articleDTOList.size());
    }

    private ArticleDTO convertToDTO(Article article, User currentUser) {
        ArticleDTO dto = new ArticleDTO();
        dto.setSlug(article.getSlug());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setTagList(article.getTags().stream().map(Tag::getName).collect(Collectors.toList()));
        dto.setCreatedAt(article.getCreatedAt().toString());
        dto.setUpdatedAt(article.getUpdatedAt().toString());
        dto.setFavoritesCount(article.getFavoritedUsers().size());
        if (currentUser != null) {
            dto.setFavorited(article.getFavoritedUsers().contains(currentUser));
        } else {
            dto.setFavorited(false);
        }

        ArticleDTO.AuthorDTO authorDTO = new ArticleDTO.AuthorDTO();
        authorDTO.setUsername(article.getAuthor().getUsername());
        authorDTO.setBio(article.getAuthor().getBio());
        authorDTO.setImage(article.getAuthor().getImage());

        if (currentUser != null) {
            boolean isFollowing = userFollowRepository.existsByFollowerAndFollowing(currentUser,
                    article.getAuthor());
            authorDTO.setFollowing(isFollowing);
        } else {
            authorDTO.setFollowing(false);
        }

        dto.setAuthor(authorDTO);
        return dto;
    }
}
