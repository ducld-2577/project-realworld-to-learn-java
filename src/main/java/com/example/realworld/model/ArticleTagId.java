package com.example.realworld.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Embeddable
public class ArticleTagId implements Serializable {

    @ManyToOne
    @JoinColumn(name = "articleId", referencedColumnName = "id", insertable = false, updatable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "tagId", referencedColumnName = "id", insertable = false, updatable = false)
    private Tag tag;

    public ArticleTagId() {}

    public ArticleTagId(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
    }

    // Getter and Setter
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleTagId that = (ArticleTagId) o;
        return Objects.equals(article, that.article) &&
               Objects.equals(tag, that.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(article, tag);
    }
}
