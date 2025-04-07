package com.example.realworld.model;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "Article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String slug;
    private String title;
    private String description;
    private String body;

    @Column(name = "createdAt", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private String createdAt;

    @Column(name = "updatedAt", columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String updatedAt;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    private User author;

    @OneToMany(mappedBy = "id.article")
    private List<ArticleToTag> articleToTags; // Quan hệ nhiều-nhiều thông qua ArticleTag

    @Transient
    private List<Tag> tags; // Lưu trữ các tags cho Article

    public Article() {}

    public Article(String slug, String title, String description, String body, User author) {
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.body = body;
        this.author = author;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<ArticleToTag> getArticleTags() {
        return articleToTags;
    }

    public void setArticleTags(List<ArticleToTag> articleToTags) {
        this.articleToTags = articleToTags;
    }

    public List<Tag> getTags() {
        if (this.articleToTags != null) {
            this.tags = articleToTags.stream()
                                    .map(articleTag -> articleTag.getId().getTag())
                                    .collect(Collectors.toList());
        }
        return tags;
    }
}
