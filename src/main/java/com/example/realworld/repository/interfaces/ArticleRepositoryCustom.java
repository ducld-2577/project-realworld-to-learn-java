package com.example.realworld.repository.interfaces;

import java.util.List;

import com.example.realworld.model.Article;

public interface ArticleRepositoryCustom {
    List<Article> findArticlesWithFilters(String tag, String author, String favorited, int limit, int offset);
}
