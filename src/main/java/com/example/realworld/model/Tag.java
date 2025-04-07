package com.example.realworld.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Tag")
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "id.tag")
    private List<ArticleToTag> articleToTags; // Liên kết với bảng ArticleTag

    public Tag() {}

    public Tag(String name) {
        this.name = name;
    }

    // Getter and Setter methods
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ArticleToTag> getArticleTags() {
        return articleToTags;
    }

    public void setArticleTags(List<ArticleToTag> articleTags) {
        this.articleToTags = articleToTags;
    }
}
