package tz9.Calendar.profilepicture;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tz9.Calendar.appUser.AppUser;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties("hibernateLazyInitializer")
public class ProfilePicture {

    @SequenceGenerator(name = "picture_sequence", sequenceName = "picture_sequence", allocationSize = 1)    //Generate sequence for this table
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "picture_sequence")
    private long id;

    private String filepath;

    private String title;

    @OneToMany (mappedBy = "profilePicture")
    @JsonIgnore
    List<AppUser> usersUsingPicture;

    public ProfilePicture() {
        this.filepath = "";
        this.title = "";
        usersUsingPicture = new ArrayList<AppUser>();
    }

    public ProfilePicture(String filepath, String title, AppUser postingUser) {
        this.filepath = filepath;
        this.title = title;
        if (usersUsingPicture == null) {
            usersUsingPicture = new ArrayList<>();
        }
        usersUsingPicture.add(postingUser);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<AppUser> getUsersUsingPicture() {
        return usersUsingPicture;
    }

    public void setUsersUsingPicture(List<AppUser> usersUsingPicture) {
        this.usersUsingPicture = usersUsingPicture;
    }

    public void removeUser(AppUser appUser) {
        if (!usersUsingPicture.contains(appUser)) {
            throw new IllegalArgumentException("Error: This user is not using this picture");
        }
        usersUsingPicture.remove(appUser);
    }

    public void addUser(AppUser appUser) {
        if (!usersUsingPicture.contains(appUser)) {
            usersUsingPicture.add(appUser);
        }
    }

    public void clearUsers() {
        usersUsingPicture.clear();
    }
}
