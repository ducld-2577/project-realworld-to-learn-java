package com.example.realworld.repository;

import com.example.realworld.model.User;
import com.example.realworld.model.UserFollow;
import com.example.realworld.model.UserFollowId;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, UserFollowId> {
    Optional<UserFollow> findByFollowerAndFollowing(User follower, User following);
    boolean existsByFollowerAndFollowing(User follower, User following);
}

