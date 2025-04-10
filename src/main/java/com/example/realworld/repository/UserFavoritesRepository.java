package com.example.realworld.repository;

import com.example.realworld.model.UserFavorites;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFavoritesRepository extends JpaRepository<UserFavorites, Long> {

}
