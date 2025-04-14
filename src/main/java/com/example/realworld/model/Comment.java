package com.example.realworld.model;

import java.time.LocalDateTime;

import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "Comment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String body;

    @Column(name = "createdAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updatedAt", nullable = false, updatable = false)
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "articleId", nullable = false)
    private Article article;

    @ManyToOne
    @JoinColumn(name = "authorId", nullable = false)
    private User author;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
