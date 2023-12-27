package tz9.Calendar.friends;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRepository extends JpaRepository<Friend, FriendId> {
    List<Friend> findByUserId(Long userId);
    List<Friend> findAllByUserIdOrFriendId(Long userId, Long friendId);

}

