package tz9.Calendar.music;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import tz9.Calendar.appUser.AppUser;
import tz9.Calendar.appUser.AppUserRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class MusicService {

    MusicRepository musicRepository;

    AppUserRepository appUserRepository;

    @Autowired
    public MusicService(MusicRepository musicRepository, AppUserRepository appUserRepository) {
        this.musicRepository = musicRepository;
        this.appUserRepository = appUserRepository;
    }

    /**
     * Gets a music entry in the database.
     *
     * @param musicId - ID to get from database.
     * @return Music entry in database.
     */
    public Music getMusic(long musicId) {
        if (!musicRepository.existsById(musicId)) {
            throw new RuntimeException("Error: Music with id " + musicId + " does not exist.");
        }

        return musicRepository.getReferenceById(musicId);
    }

    /**
     * Streams music by database entry.
     *
     * @param musicId - Music ID to stream.
     * @param response - Response to stream to.
     */
    public void streamById(long musicId, HttpServletResponse response) {
        if (!musicRepository.existsById(musicId)) {
            throw new RuntimeException("Error: Music with id " + musicId + " does not exist.");
        }

        Music music = musicRepository.getReferenceById(musicId);
        String filepath = "/music/" + music.getTitle();

        stream(filepath, response);
    }

    /**
     * Streams music by database entry.
     *
     * @param title - Music title to stream.
     * @param response - Response to stream to.
     */
    public void streamByTitle(String title, HttpServletResponse response) {
        if (musicRepository.findByTitle(title).isEmpty()) {
            throw new RuntimeException("Error: ");
        }

        stream("/music/" + musicRepository.findByTitle(title).get().getTitle(),
                response);
    }

    /**
     * Posts a new music file to database.
     *
     * @param file - File to post.
     * @param posterId - User posting the file.
     */
    public void postFile(MultipartFile file, long posterId) {
        String fileBasePath = "/music/";
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        if (musicRepository.findByTitle(file.getOriginalFilename()).isPresent()) {
            throw new RuntimeException("Error: Music with that title already exists.");
        }

        if (!appUserRepository.existsById(posterId)) {
            throw new RuntimeException("Error: User with id " + posterId + " does not exist.");
        }

        Path path = Paths.get(fileBasePath + fileName);

        try {
            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        AppUser poster = appUserRepository.getReferenceById(posterId);

        System.out.println(poster.getFirstName());
        Music newSong = new Music(fileBasePath + fileName, fileName, poster);
        poster.setPreferencedMusic(newSong);

        musicRepository.save(newSong);
        appUserRepository.save(poster);
    }

    /**
     * Deletes a song my ID.
     *
     * @param musicId - Music to Delete.
     */
    public void deleteMusic(long musicId) {
        if (!musicRepository.existsById(musicId)) {
            throw new RuntimeException("Error: Music with id " + musicId + " does not exist.");
        }

        // Get music Reference and filepath
        Music music = musicRepository.getReferenceById(musicId);
        String filePath = music.getFilePath();

        // Clear users who've preferenced this music
        for (AppUser user : music.getUsers()) {
            user.setPreferencedMusic(null);
            appUserRepository.save(user);
        }
        // Clear on music's end
        music.getUsers().clear();

        // Get file to delete and delete
        File fileToDelete = new File(music.getFilePath());
        fileToDelete.delete();

        // Update and delete reference in SQL database
        musicRepository.save(music);
        musicRepository.deleteById(musicId);
    }

    /**
     * Attempts to remove a user's preferenced music.
     *
     * @param userId - User to delete preference.
     */
    public void deletePreference(long userId) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        AppUser user = appUserRepository.getReferenceById(userId);

        if (user.getPreferencedMusic() == null) {
            throw new RuntimeException("Error: User has no music to unpreference");
        }

        Music music = user.getPreferencedMusic();

        music.removeUser(user);
        user.setPreferencedMusic(null);

        appUserRepository.save(user);
        musicRepository.save(music);
    }

    /**
     * Attempts to change a user with id 'userId's preferred music to different music with id 'musicId'.
     *
     * @param userId - User to change preference of.
     * @param musicId - Music to change preference to.
     */
    @Transactional
    public void changePreference(long userId, long musicId) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        if (!musicRepository.existsById(musicId)) {
            throw new RuntimeException("Error: Music with id " + musicId + " does not exist.");
        }

        // Get references
        AppUser user = appUserRepository.getReferenceById(userId);
        Music oldMusic = user.getPreferencedMusic();
        Music newMusic = musicRepository.getReferenceById(musicId);

        // Update Relations
        if (oldMusic != null) {
            oldMusic.removeUser(user);
            musicRepository.save(oldMusic);
        }
        user.setPreferencedMusic(newMusic);
        newMusic.addUser(user);

        // Save Relations
        appUserRepository.save(user);
        musicRepository.save(newMusic);
    }

    /**
     * Attempts to stream a User's preferred music.
     *
     * @param userId - User to stream preference.
     * @param response - Response to stream to.
     */
    public void streamPreference(long userId, HttpServletResponse response) {
        if (!appUserRepository.existsById(userId)) {
            throw new RuntimeException("Error: User with id " + userId + " does not exist.");
        }

        AppUser user = appUserRepository.getReferenceById(userId);

        if (user.getPreferencedMusic() == null) {
            throw new RuntimeException("Error: User has no music to stream");
        }

        stream(user.getPreferencedMusic().getFilePath(), response);
    }

    /**
     * Gets all music in repository.
     *
     * @return All database entries.
     */
    public List<Music> getAllMusic() {
        return musicRepository.findAll();
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
