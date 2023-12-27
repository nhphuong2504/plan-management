package tz9.Calendar.friends;
import java.io.Serializable;
import java.util.Objects;

public class FriendId implements Serializable {
    private Long userId;
    private Long friendId;

    public FriendId() {
    }
    // Constructors, getters, and setters
    public FriendId(Long userId, Long friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public Long getFriendId() {
        return friendId;
    }
    public void setFriendId(Long friendId) {
        this.friendId = friendId;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendId friendId = (FriendId) o;
        return Objects.equals(userId, friendId.userId) && Objects.equals(friendId, friendId.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}

