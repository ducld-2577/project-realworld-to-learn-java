package com.example.realworld.model;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "_UserFavorites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFavorite {

    @EmbeddedId
    private UserFavoritesId id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private Article article;

    public UserFavorite(User user, Article article) {
        this.user = user;
        this.article = article;
        this.id = new UserFavoritesId(user.getId(), article.getId());
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserFavoritesId getId() {
        return id;
    }

    public void setId(UserFavoritesId id) {
        this.id = id;
    }
}
