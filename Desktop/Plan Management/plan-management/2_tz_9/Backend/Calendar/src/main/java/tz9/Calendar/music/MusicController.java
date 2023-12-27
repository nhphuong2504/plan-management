package tz9.Calendar.music;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tz9.Calendar.alert.Alert;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/music/")
public class MusicController {

    private MusicService musicService;

    public MusicController(MusicService musicService) {
        this.musicService = musicService;
    }

    /**
     * Get a file with String name.
     *
     * @param title - Title of file to get.
     * @param response - Servlet response for streaming to.
     */
    @GetMapping (value = "/streamTitle={musicTitle}")
    @Operation(description = "Get a file with String name.")
    public void streamByTitle(@PathVariable("musicTitle") String title, HttpServletResponse response) {
        musicService.streamByTitle(title, response);
    }

    /**
     * Streams music by ID.
     *
     * @param musicId - Music to stream.
     * @param response - Servlet response for streaming to.
     */
    @GetMapping (value = "/streamId={musicId}")
    @Operation(description = "Streams music by ID.")
    public void streamById (@PathVariable("musicId") long musicId, HttpServletResponse response) {
        musicService.streamById(musicId, response);
    }

    /**
     * Get a file with id.
     *
     * @param musicId - Music to get.
     * @return Music File.
     */
    @GetMapping (value = "/get/music={musicId}")
    @Operation(description = "Get a file with id.")
    public Music getFileById(@PathVariable("musicId") long musicId) {
        return musicService.getMusic(musicId);
    }

    /**
     * Posts a new mp3 file to the Database.
     *
     * @param file - File to post.
     * @param userId - User posting.
     * @return Confirmation Message.
     */
    @PostMapping (value = "/postSong/user={userId}")
    @Operation(description = "Posts a new mp3 file to the Database.")
    public Alert postSong(@RequestBody MultipartFile file, @PathVariable("userId") long userId) {
        musicService.postFile(file, userId);
        return new Alert("Posted Successfully");
    }

    /**
     * Delete file.
     *
     * @param musicId - Music to delete
     * @param userId - User attempting delete
     * @return Confirmation Message.
     */
    @DeleteMapping (value = "/deleteSong/music={musicId}/user={userId}")
    @Operation(description = "Delete file.")
    public Alert deleteSong(@PathVariable("musicId") long musicId, @PathVariable("userId") long userId) {
        musicService.deleteMusic(musicId);
        return new Alert("Delete successful");
    }

    /**
     * Unpreferences a user's music.
     *
     * @param userId - User to unpreference music
     * @return Confirmation Message.
     */
    @DeleteMapping (value = "/unPreference/user={userId}")
    @Operation(description = "Unpreferences a user's music.")
    public Alert unPreference(@PathVariable("userId") long userId) {
        musicService.deletePreference(userId);
        return new Alert("Unpreferenced successfully.");
    }

    /**
     * Changes a users preferred music.
     *
     * @param userId - User to change a preference.
     * @param musicId - Music to change to.
     * @return Confirmation message.
     */
    @PutMapping (value = "/changePreference/user={userId}/music={musicId}")
    @Operation(description = "Changes a users preferred music.")
    public Alert changePreference(@PathVariable("userId") long userId, @PathVariable("musicId") long musicId) {
        musicService.changePreference(userId, musicId);
        return new Alert ("Changed Preference successfully.");
    }

    /**
     * Changes a user's preferred music to null.
     *
     * @param userId - User to unpreference.
     * @param response - Servlet response for streaming to.
     */
    @GetMapping (value = "/streamPreference/user={userId}")
    @Operation(description = "Changes a user's preferred music to null.")
    public void streamPreference(@PathVariable("userId") long userId, HttpServletResponse response) {
        musicService.streamPreference(userId, response);
    }

    /**
     * Get all music in Repository.
     *
     * @return All SQL entries of music in Repository.
     */
    @GetMapping (value = "/getAllMusic")
    @Operation(description = "Get all music in Repository.")
    public List<Music> getAllMusic() {
        return musicService.getAllMusic();
    }

}
