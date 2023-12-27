package tz9.Calendar.profilepicture;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tz9.Calendar.alert.Alert;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping ("/profilePictures")
public class ProfileController {

    private ProfileService profileService;

    public ProfileController(ProfileService profileService) {
        this.profileService = profileService;
    }

    /**
     * Gets a user's picture and returns through response servlet.
     *
     * @param userId - User to get picture for.
     * @param response - Response to send file to.
     */
    @GetMapping ("/getPicture/user={userId}")
    @Operation(description = "Gets a user's picture and returns through response servlet.")
    public void getUserProfile (@PathVariable("userId") long userId, HttpServletResponse response) {
        profileService.getPicture(userId, response);
    }

    /**
     * Gets a picture by its title.
     *
     * @param picTitle - Title of picture to get.
     * @param response - Response to send file to.
     */
    @GetMapping ("/getPicture/title={picTitle}")
    @Operation(description = "Gets a picture by its title.")
    public void getUserProfile (@PathVariable("picTitle") String picTitle, HttpServletResponse response) {
        profileService.getPicture(picTitle, response);
    }

    /**
     * Posts picture and attaches to user.
     *
     * @param userId - User to attach picture to.
     * @param file - Picture to upload.
     * @return Confirmation Message.
     */
    @PostMapping ("/postPicture/user={userId}")
    @Operation(description = "Posts picture and attaches to user.")
    public Alert postPicture (@PathVariable("userId") long userId, @RequestBody MultipartFile file) {
        profileService.postPicture(userId, file);
        return new Alert("Post successful.");
    }

    /**
     * Deletes a database entry for an uploaded picture.
     *
     * @param pictureId - Picture to delete.
     * @return Confirmation Message.
     */
    @DeleteMapping ("/deletePicture/pictureId={pictureId}")
    @Operation(description = "Deletes a database entry for an uploaded picture.")
    public Alert deletePicture (@PathVariable("pictureId") long pictureId) {
        profileService.deletePicture(pictureId);
        return new Alert ("Delete successful.");
    }

    /**
     * Changes a user's picture to picture with ID 'pictureId'.
     *
     * @param userId - User to change picture for.
     * @param pictureId - Picture to change to.
     * @return Confirmation Message.
     */
    @PutMapping ("/changePicture/user={userId}/pictureId={pictureId}")
    @Operation(description = "Changes a user's picture to picture with ID 'pictureId'.")
    public Alert changePicture (@PathVariable("userId") long userId, @PathVariable("pictureId") long pictureId) {
        profileService.changePicture(userId, pictureId);
        return new Alert ("Change successful.");
    }

}
