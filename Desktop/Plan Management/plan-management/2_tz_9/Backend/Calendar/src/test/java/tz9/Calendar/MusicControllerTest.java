//package tz9.Calendar;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//import tz9.Calendar.appUser.AppUser;
//import tz9.Calendar.appUser.AppUserRole;
//import tz9.Calendar.appUser.AppUserService;
//import tz9.Calendar.music.Music;
//import tz9.Calendar.music.MusicController;
//import tz9.Calendar.music.MusicService;
//
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.List;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(SpringExtension.class)
//@WebMvcTest(MusicController.class)
//public class MusicControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private MusicService musicService;
//
//    @MockBean
//    private AppUserService appUserService;
//
//    private Music testMusic;
//    private AppUser testUser;
//
//    @BeforeEach
//    public void setUp() {
//        testUser = new AppUser("Phuong", "Nguyen", "Test email", "123", AppUserRole.USER);
//        when(appUserService.findUserById(testUser.getId())).thenReturn(testUser);
//        testMusic = new Music("testFilePath", "testTitle", testUser);
//    }
//
//    @Test
//    public void testStreamByTitle() throws Exception {
//        mockMvc.perform(get("/music/streamTitle={musicTitle}", testMusic.getTitle()))
//                .andExpect(status().isOk());
//        verify(musicService, times(1)).streamByTitle(eq(testMusic.getTitle()), any(HttpServletResponse.class));
//    }
//
//    @Test
//    public void testStreamById() throws Exception {
//        mockMvc.perform(get("/music/streamId={musicId}", testMusic.getId()))
//                .andExpect(status().isOk());
//        verify(musicService, times(1)).streamById(eq(testMusic.getId()), any(HttpServletResponse.class));
//    }
//
//    @Test
//    public void testGetFileById() throws Exception {
//        when(musicService.getMusic(testMusic.getId())).thenReturn(testMusic);
//        mockMvc.perform(get("/music/get/music={musicId}", testMusic.getId()))
//                .andExpect(status().isOk());
//        verify(musicService, times(1)).getMusic(testMusic.getId());
//    }
//
//
//    @Test
//    public void testGetAllMusic() throws Exception {
//        List<Music> musicList = new ArrayList<>();
//        musicList.add(testMusic);
//        when(musicService.getAllMusic()).thenReturn(musicList);
//
//        mockMvc.perform(get("/music/getAllMusic"))
//                .andExpect(status().isOk());
//        verify(musicService, times(1)).getAllMusic();
//    }
//}
