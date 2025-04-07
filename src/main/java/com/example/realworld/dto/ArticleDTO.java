package com.example.realworld.dto;

import java.util.List;

public class ArticleDTO {
    private String slug;
    private String title;
    private String description;
    private List<String> tagList;
    // private String createdAt;
    // private String updatedAt;
    private boolean favorited;
    private int favoritesCount;
    private AuthorDTO author;

    public ArticleDTO() {}

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getTagList() {
        return tagList;
    }

    public void setTagList(List<String> tagList) {
        this.tagList = tagList;
    }

    // public String getCreatedAt() {
    //     return createdAt;
    // }

    // public void setCreatedAt(String createdAt) {
    //     this.createdAt = createdAt;
    // }

    // public String getUpdatedAt() {
    //     return updatedAt;
    // }

    // public void setUpdatedAt(String updatedAt) {
    //     this.updatedAt = updatedAt;
    // }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }

    public int getFavoritesCount() {
        return favoritesCount;
    }

    public void setFavoritesCount(int favoritesCount) {
        this.favoritesCount = favoritesCount;
    }

    public AuthorDTO getAuthor() {
        return author;
    }

    public void setAuthor(AuthorDTO author) {
        this.author = author;
    }

    public static class AuthorDTO {
        private String username;
        private String bio;
        private String image;
        private boolean following;

        public AuthorDTO() {}

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getBio() {
            return bio;
        }

        public void setBio(String bio) {
            this.bio = bio;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public boolean isFollowing() {
            return following;
        }

        public void setFollowing(boolean following) {
            this.following = following;
        }
    }
}

