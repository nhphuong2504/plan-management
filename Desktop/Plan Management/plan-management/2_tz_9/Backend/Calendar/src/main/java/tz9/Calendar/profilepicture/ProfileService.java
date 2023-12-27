package tz9.Calendar.profilepicture;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ProfileService {

    AppUserRepository appUserRepository;

    ProfileRepository profileRepository;

    @Autowired
    public ProfileService (AppUserRepository appUserRepository, ProfileRepository profileRepository) {
        this.appUserRepository = appUserRepository;
        this.profileRepository = profileRepository;
    }

    // Use same method as input/output stream

    /**
     * Streams picture to response servlet.
     *
     * @param userId - User to get picture of.
     * @param response - Response to stream to.
     */
    public void getPicture(long userId, HttpServletResponse response) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        AppUser user = appUserRepository.getReferenceById(userId);

        if (user.getProfilePicture() == null) {
            throw new IllegalArgumentException("Error: User has not posted a profile picture.");
        }

        ProfilePicture pfp = user.getProfilePicture();
        String filepath = "/pictures/" + pfp.getTitle();

        stream(filepath, response);
    }

    /**
     * Finds and streams picture by title in database.
     *
     * @param picTitle - Title to stream.
     * @param response - Response to stream to.
     */
    public void getPicture(String picTitle, HttpServletResponse response) {
        if (profileRepository.findByTitle(picTitle).isEmpty()) {
            throw new IllegalArgumentException("Error: A picture with title " + picTitle + " does not exist.");
        }

        String filepath = "/pictures/" + picTitle;

        stream(filepath, response);
    }

    // Use file in/output stream to get --> same as music file

    /**
     * Posts a picture and stores on server.
     *
     * @param userId - User posting picture.
     * @param file - File to post.
     */
    public void postPicture (long userId, MultipartFile file) {
        String fileBasePath = "/pictures/";
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (profileRepository.findByTitle(file.getOriginalFilename()).isPresent()) {
            throw new RuntimeException("Error: Picture with that title already exists.");
        }

        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        Path path = Paths.get(fileBasePath + fileName);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AppUser poster = appUserRepository.getReferenceById(userId);

        ProfilePicture pfp = new ProfilePicture(fileBasePath + fileName, fileName, poster);
        poster.setProfilePicture(pfp);
        pfp.addUser(poster);

        profileRepository.save(pfp);
        appUserRepository.save(poster);
    }

    /**
     * Deletes picture by database ID.
     *
     * @param pictureId - Picture to delete from Backend.
     */
    public void deletePicture (long pictureId) {
        if (!profileRepository.existsById(pictureId)) {
            throw new RuntimeException("Error: Picture with that Id does not exist.");
        }

        ProfilePicture pic = profileRepository.getReferenceById(pictureId);

        List<AppUser> usersUsingPic = pic.getUsersUsingPicture();

        for (AppUser u : usersUsingPic) {
            u.setProfilePicture(null);
            appUserRepository.save(u);
        }

        pic.clearUsers();
        profileRepository.save(pic);
        profileRepository.delete(pic);
    }

    /**
     * Changes a user's current picture.
     *
     * @param userId - User to change picture for.
     * @param pictureId - Picture to change to.
     */
    public void changePicture(long userId, long pictureId) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        if (!profileRepository.existsById(pictureId)) {
            throw new RuntimeException("Error: Picture with that Id does not exist.");
        }

        AppUser user = appUserRepository.getReferenceById(userId);
        ProfilePicture oldPicture = user.getProfilePicture();
        ProfilePicture newPic = profileRepository.getReferenceById(pictureId);

        if (oldPicture != null) {
            oldPicture.removeUser(user);
            profileRepository.save(oldPicture);
        }

        user.setProfilePicture(newPic);
        newPic.addUser(user);
        appUserRepository.save(user);
        profileRepository.save(newPic);
    }

    private void stream(String filepath, HttpServletResponse response) {
        try {
            InputStream inputStream = new FileInputStream(filepath);
            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
