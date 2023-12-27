package tz9.Calendar.music;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusicRepository extends JpaRepository<Music, Long> {

    @Query(value = "SELECT * FROM music e WHERE e.title = ?1",
            nativeQuery = true)
    Optional<Music> findByTitle(String title);

}
