package com.example.realworld.model;

import lombok.*;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Tag")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "tag")
    private List<ArticleToTag> articleToTags;

    public Tag(String name) {
        this.name = name;
    }

    public List<ArticleToTag> getArticleTags() {
        return articleToTags;
    }

    public void setArticleTags(List<ArticleToTag> articleTags) {
        this.articleToTags = articleToTags;
    }
}
