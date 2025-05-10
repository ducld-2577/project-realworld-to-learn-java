package com.example.realworld.dto;

public class UpdateUserResponseDTO {
    private UserDTO user;

    public UpdateUserResponseDTO(UserDTO user) {
        this.user = user;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }

    public static class UserDTO {
        private String email;
        private String username;
        private String bio;
        private String image;

        public UserDTO(String email, String username, String bio, String image) {
            this.email = email;
            this.username = username;
            this.bio = bio;
            this.image = image;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

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
    }
}
