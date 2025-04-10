package com.example.realworld.model;

import lombok.*;
import javax.persistence.*;

@Entity
@Table(name = "_ArticleToTag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArticleToTag {

    @EmbeddedId
    private ArticleTagId id;

    @ManyToOne
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private Tag tag;

    public ArticleToTag(Article article, Tag tag) {
        this.article = article;
        this.tag = tag;
        this.id = new ArticleTagId(article.getId(), tag.getId());
    }

    public Article getArticle() {
        return article;
    }

    public Tag getTag() {
        return tag;
    }

    public ArticleTagId getId() {
        return id;
    }

    public void setId(ArticleTagId id) {
        this.id = id;
    }
}
