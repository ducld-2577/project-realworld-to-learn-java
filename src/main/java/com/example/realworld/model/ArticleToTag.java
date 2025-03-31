package com.example.realworld.model;

import javax.persistence.*;

@Entity
@Table(name = "_ArticleToTag")
public class ArticleToTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "A", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "B", nullable = false)
    private Tag tag;

    public ArticleToTag() {}

    public ArticleToTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
