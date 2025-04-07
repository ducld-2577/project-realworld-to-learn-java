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

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFollowRepository userFollowRepository;

    public ArticleListResponseDTO getArticles(
        Optional<String> tag,
        Optional<String> author,
        Optional<String> favorited,
        Integer limit,
        Integer offset,
        Authentication authentication
    ) {
        // Lấy danh sách bài viết từ repository với các filter
        List<Article> articles = articleRepository.findArticlesWithFilters(
                tag.orElse(null),
                author.orElse(null),
                favorited.orElse(null),
                offset,
                limit
        );

        // Kiểm tra người dùng đã đăng nhập hay chưa
        final User currentUser;
        if (authentication != null && authentication.isAuthenticated()) {
            currentUser = userRepository.findByUsername(authentication.getName()).orElse(null);
        } else {
            currentUser = null; // Nếu không có người dùng đăng nhập, gán null
        }

        // Tạo danh sách ArticleDTO
        List<ArticleDTO> articleDTOList = articles.stream()
                .map(article -> convertToDTO(article, currentUser))
                .collect(Collectors.toList());

        // Trả về response với danh sách bài viết và số lượng bài viết
        return new ArticleListResponseDTO(articleDTOList, articleDTOList.size());
    }

    private ArticleDTO convertToDTO(Article article, User currentUser) {
        ArticleDTO dto = new ArticleDTO();
        dto.setSlug(article.getSlug());
        dto.setTitle(article.getTitle());
        dto.setDescription(article.getDescription());
        dto.setTagList(article.getTags().stream().map(Tag::getName).collect(Collectors.toList()));

        // Chỉ set favorited và favoritesCount khi người dùng đã đăng nhập
        // if (currentUser != null) {
        //     dto.setFavorited(article.getFavoritedUsers().contains(currentUser));
        //     dto.setFavoritesCount(article.getFavoritedUsers().size());
        // } else {
        //     dto.setFavorited(false); // Nếu chưa đăng nhập, đặt là false
        //     dto.setFavoritesCount(0); // Đặt số lượng favorites là 0
        // }

        // Tạo đối tượng AuthorDTO
        ArticleDTO.AuthorDTO authorDTO = new ArticleDTO.AuthorDTO();
        authorDTO.setUsername(article.getAuthor().getUsername());
        authorDTO.setBio(article.getAuthor().getBio());
        authorDTO.setImage(article.getAuthor().getImage());

        // Nếu có người dùng đăng nhập, kiểm tra xem có đang theo dõi tác giả không
        if (currentUser != null) {
            boolean isFollowing = userFollowRepository.existsByFollowerAndFollowing(currentUser, article.getAuthor());
            authorDTO.setFollowing(isFollowing);
        } else {
            authorDTO.setFollowing(false); // Nếu chưa đăng nhập, không theo dõi
        }

        dto.setAuthor(authorDTO);
        return dto;
    }
}
