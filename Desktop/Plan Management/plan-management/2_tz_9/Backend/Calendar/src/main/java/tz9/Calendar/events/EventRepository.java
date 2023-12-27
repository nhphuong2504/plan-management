package tz9.Calendar.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tz9.Calendar.appUser.AppUser;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAll();

    @Query(value = "SELECT * FROM events e WHERE e.title = ?1",
        nativeQuery = true)
    Optional<Event> findByTitle(String title);


    List<Event> findAllByOwner(AppUser owner);      //find events by owner

    @Query(value = "SELECT * FROM events e WHERE e.title = ?1",
            nativeQuery = true)
    List<Event> findAllByTitle(String title);

    @Query("SELECT e FROM Event e WHERE e.date < :expiryDate OR (e.date = :expiryDate AND e.time <= :expiryTime)")
    List<Event> findExpiredEvents(@Param("expiryDate") LocalDate expiryDate, @Param("expiryTime") LocalTime expiryTime);
}
