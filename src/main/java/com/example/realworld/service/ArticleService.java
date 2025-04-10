package com.example.realworld.service;

import com.example.realworld.dto.ArticleDTO;
import com.example.realworld.dto.ArticleListResponseDTO;
import com.example.realworld.dto.UpdateArticleRequestDTO;
import com.example.realworld.model.Article;
import com.example.realworld.model.ArticleToTag;
import com.example.realworld.model.Tag;
import com.example.realworld.model.User;
import com.example.realworld.repository.ArticleRepository;
import com.example.realworld.repository.TagRepository;
import com.example.realworld.repository.UserRepository;
import com.example.realworld.repository.UserFollowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.core.Authentication;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowRepository userFollowRepository;

    @PersistenceContext
    private EntityManager entityManager;

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

    public ArticleDTO getArticle(String slug, Authentication authentication) {
        Article article = articleRepository.findBySlug(slug);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article is not found");
        }

        final User currentUser;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        } else {
            currentUser = null;
        }

        return convertToDTO(article, currentUser);
    }

    @Transactional
    public ArticleDTO createArticle(ArticleDTO articleDTO, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Article article = new Article();
        article.setSlug(articleDTO.getSlug());
        article.setTitle(articleDTO.getTitle());
        article.setDescription(articleDTO.getDescription());
        article.setBody(articleDTO.getBody());
        article.setSlug(toSlug(articleDTO.getTitle()));
        List<String> tagList =
                articleDTO.getTagList() != null ? articleDTO.getTagList() : new ArrayList<>();
        List<ArticleToTag> articleToTags = tagList.stream().map(tagName -> {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> tagRepository.save(new Tag(tagName)));
            return new ArticleToTag(article, tag);
        }).collect(Collectors.toList());
        article.setArticleTags(articleToTags);
        article.setAuthor(currentUser);
        articleRepository.save(article);

        return convertToDTO(article, currentUser);
    }

    @Transactional
    public ArticleDTO updateArticle(String slug, UpdateArticleRequestDTO updateArticleRequestDTO,
            Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Article article = articleRepository.findBySlug(slug);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article is not found");
        }

        if (!article.getAuthor().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the author of this article");
        }

        if (updateArticleRequestDTO.getTitle() != null) {
            article.setTitle(updateArticleRequestDTO.getTitle());
            article.setSlug(toSlug(updateArticleRequestDTO.getTitle()));
        }

        if (updateArticleRequestDTO.getDescription() != null) {
            article.setDescription(updateArticleRequestDTO.getDescription());
        }

        if (updateArticleRequestDTO.getBody() != null) {
            article.setBody(updateArticleRequestDTO.getBody());
        }

        articleRepository.save(article);

        return convertToDTO(article, currentUser);
    }

    public void deleteArticle(String slug, Authentication authentication) {
        User currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        if (currentUser == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User is not authenticated");
        }

        Article article = articleRepository.findBySlug(slug);
        if (article == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article is not found");
        }

        if (!article.getAuthor().equals(currentUser)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "You are not the author of this article");
        }

        articleRepository.delete(article);
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
        dto.setBody(article.getBody());
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

    private String toSlug(String input) {
        final Pattern NONLATIN = Pattern.compile("[^\\w-]");
        final Pattern WHITESPACE = Pattern.compile("[\\s]");
        String nowhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(nowhitespace, Normalizer.Form.NFD);
        String slug = NONLATIN.matcher(normalized).replaceAll("");
        return slug.toLowerCase(Locale.ENGLISH);
    }
}
