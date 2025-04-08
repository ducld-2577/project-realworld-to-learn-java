package com.example.realworld.model;

import java.io.Serializable;
import java.util.Objects;
import lombok.*;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@Builder
public class UserFavoritesId implements Serializable {

    private Long userId;
    private Long articleId;

    public UserFavoritesId(Long userId, Long articleId) {
        this.userId = userId;
        this.articleId = articleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserFavoritesId that = (UserFavoritesId) o;
        return Objects.equals(userId, that.userId) && Objects.equals(articleId, that.articleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, articleId);
    }
}
