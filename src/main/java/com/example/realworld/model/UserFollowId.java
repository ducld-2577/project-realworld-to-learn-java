package com.example.realworld.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserFollowId implements Serializable {

    private Long followerId;
    private Long followingId;

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserFollowId that = (UserFollowId) o;
        return Objects.equals(followerId, that.followerId)
                && Objects.equals(followingId, that.followingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(followerId, followingId);
    }
}
