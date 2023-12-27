package tz9.Calendar.appUser;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface AppUserRepository
        extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);


    @Transactional
    @Modifying
    @Query("UPDATE AppUser a " +
            "SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);

    @Query("SELECT a.enabled FROM AppUser a WHERE a.email = ?1")
    Boolean findEnabledByEmail(String email);
    @Query("SELECT a.password FROM AppUser a WHERE a.email = ?1")
    String findPassword(String email);

    @Query("SELECT a.id FROM AppUser a WHERE a.email = ?1")
    Long findUserIdByEmail(String email);
}
