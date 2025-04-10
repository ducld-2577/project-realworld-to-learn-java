package com.example.realworld.repository.interfaces;

import java.util.List;

import com.example.realworld.model.Article;
import com.example.realworld.model.User;

public interface ArticleRepositoryCustom {
    List<Article> findArticlesWithFilters(String tag, String author, String favorited, int limit,
            int offset);

    List<Article> findArticlesOnFeedWithFilters(String tag, String author, String favorited,
            int offset, int limit, User currentUser);
}
