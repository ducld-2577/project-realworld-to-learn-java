package com.example.realworld.model;

import javax.persistence.*;

@Entity
@Table(name = "_ArticleToTag")
public class ArticleToTag {

    @EmbeddedId
    private ArticleTagId id;

    public ArticleToTag() {}

    public ArticleToTag(Article article, Tag tag) {
        this.id = new ArticleTagId(article, tag);
    }

    public ArticleTagId getId() {
        return id;
    }

    public void setId(ArticleTagId id) {
        this.id = id;
    }
}
