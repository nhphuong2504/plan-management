package tz9.Calendar.profilepicture;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ProfileRepository extends JpaRepository<ProfilePicture, Long> {
    @Query(value = "SELECT * FROM profile_picture e WHERE e.title = ?1",
            nativeQuery = true)
    Optional<ProfilePicture> findByTitle(String title);
}
