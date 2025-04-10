package com.example.realworld.model;

import lombok.*;
import javax.persistence.*;
import lombok.*;

@Entity
@Table(name = "_UserFollows")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollow {

    @EmbeddedId
    private UserFollowId id;

    @ManyToOne
    @MapsId("followerId")
    @JoinColumn(name = "follower_id")
    private User follower;

    @ManyToOne
    @MapsId("followingId")
    @JoinColumn(name = "following_id")
    private User following;
}

