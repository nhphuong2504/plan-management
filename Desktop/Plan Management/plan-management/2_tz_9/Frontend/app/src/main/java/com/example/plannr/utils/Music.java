package com.example.plannr.utils;

import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;

/**
 * This class manages the background music playing in the app
 */
public class Music {
    public static boolean isPlaying = false;

    public static MediaPlayer buttonAudio;

    public static MediaPlayer backgroundMusic;


    /**
     * Plays the button audio file. Currently unimplemented
     */
    public static void buttonClick()
    {
        buttonAudio.start();
    }

    /**
     * Creates a MediaPlayer to play background music if one doesn't already exist. It streams the music from an audio file stored on the server
     */
    public static void startMusic()
    {
        if(Music.isPlaying) {
            System.out.println("MUSIC IS PLAYING");
            return;
        }
        backgroundMusic = new MediaPlayer();
        backgroundMusic.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            backgroundMusic.setDataSource("http://coms-309-040.class.las.iastate.edu:8080/music/streamPreference/user=3");
            backgroundMusic.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } //Send a request with a specific url for user number and preference
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(1.0f, 1.0f);
        backgroundMusic.start();
        Music.isPlaying = true;
    }

    /**
     * This function creates a replacement MediaPlayer that plays the song from the updated user preference.
     */
    public static void changeMusic()
    {
        backgroundMusic.stop();
        backgroundMusic.release();
        backgroundMusic = new MediaPlayer();
        backgroundMusic.setAudioStreamType(AudioManager.STREAM_MUSIC);
        try {
            backgroundMusic.setDataSource("http://coms-309-040.class.las.iastate.edu:8080/music/streamPreference/user=3");
            backgroundMusic.prepare();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } //Send a request with a specific url for user number and preference
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(1.0f, 1.0f);
        backgroundMusic.start();
        Music.isPlaying = true;
    }

    /**
     * This function creates the MediaPlayer for button click sounds. Currently unimplemented
     */
    public static void buttonSoundInit()
    {
        Music.buttonAudio = new MediaPlayer();
        Music.buttonAudio.reset();
        try
        {
            Music.buttonAudio.setDataSource("raw/music");
            Music.buttonAudio.prepare();
        } catch (Exception e)
        {
            System.out.println("BUTTONCLICK ISSUE");
        }
        Music.buttonAudio.setLooping(false);
        Music.buttonAudio.setVolume(1.0f, 1.0f);
    }
}
