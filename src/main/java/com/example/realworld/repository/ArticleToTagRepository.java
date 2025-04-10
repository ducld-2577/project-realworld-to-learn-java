package com.example.realworld.repository;

import com.example.realworld.model.ArticleToTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleToTagRepository extends JpaRepository<ArticleToTag, Long> {
}
