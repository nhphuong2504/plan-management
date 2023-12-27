package tz9.Calendar.friends;
import javax.persistence.*;
@Entity
@Table(name = "friends")
@IdClass(FriendId.class)
public class Friend {

    @Id
    @Column(name = "user_id")
    private Long userId;
    @Id
    @Column(name = "friend_id")
    private Long friendId;

    // Getters and setters
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
}


