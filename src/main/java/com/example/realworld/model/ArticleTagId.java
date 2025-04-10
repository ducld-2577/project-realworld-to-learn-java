package com.example.realworld.model;

import lombok.*;
import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@Builder
public class ArticleTagId implements Serializable {

    private Long articleId;
    private Long tagId;

    public ArticleTagId(Long articleId, Long tagId) {
        this.articleId = articleId;
        this.tagId = tagId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ArticleTagId that = (ArticleTagId) o;
        return Objects.equals(articleId, that.articleId) && Objects.equals(tagId, that.tagId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, tagId);
    }
}
