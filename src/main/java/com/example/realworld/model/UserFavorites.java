package com.example.realworld.model;

import javax.persistence.*;

@Entity
@IdClass(UserFavoritesId.class)
public class UserFavorites {

    @Id
    @Column(name = "A")
    private Long articleId;

    @Id
    @Column(name = "B")
    private Long userId;
}
