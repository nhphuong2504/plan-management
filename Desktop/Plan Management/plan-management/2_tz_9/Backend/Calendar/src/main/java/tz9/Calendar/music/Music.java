package tz9.Calendar.music;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tz9.Calendar.appUser.AppUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")
public class Music {

    @SequenceGenerator(name = "music_sequence", sequenceName = "music_sequence", allocationSize = 1)    //Generate sequence for this table
    @Id
    @GeneratedValue (strategy = GenerationType.SEQUENCE, generator = "music_sequence")
    private long id;

    private String filePath;

    private String title;

    @OneToMany (mappedBy = "preferencedMusic")
    @JsonIgnore
    private List<AppUser> users;

    public Music() {
        this.filePath = "";
        this.title = "";
        this.users = new ArrayList<AppUser>();
    }

    public Music(String filePath, String title, AppUser preferencedUser) {
        this.filePath = filePath;
        this.title = title;
        if (users == null) {
            users = new ArrayList<AppUser>();
        }
        this.users.add(preferencedUser);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AppUser> getUsers() {
        return users;
    }

    public void setUsers(List<AppUser> users) {
        this.users = users;
    }

    public void removeUser(AppUser user) {
        if (!users.contains(user)) {
            throw new RuntimeException("Error: User with id " + user.getId() + " has not preferenced this song.");
        }

        users.remove(user);
    }

    public void addUser(AppUser user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }
}
