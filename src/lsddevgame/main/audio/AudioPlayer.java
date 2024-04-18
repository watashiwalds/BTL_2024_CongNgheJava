package lsddevgame.main.audio;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class AudioPlayer {
    public static final int BUTTON_CLICKED = 0;
    public static final int JUMP = 1;
    public static final int ITEM_PICKUP = 2;
    public static final int CLIMBING = 3;
    public static final int DOOROPEN = 4;
    public static final int INWATER = 5;
    public static final int HURT = 6;

    public static final int GAME_OVER = 7;
    public static final int DEMO_FINISHED = 8;

    private Clip[] sfx;
    private Clip bgmusic;
    private float masterVolume, musicVolume, sfxVolume;

    public AudioPlayer() {
        loadDefaultVolume();
        loadSound();
    }

    private void loadDefaultVolume() {
        masterVolume = musicVolume = sfxVolume = 0.5f;
    }

    private void loadSound() {
        String bgmusiclinks = "background.wav";
        bgmusic = loadClip(bgmusiclinks);

        String[] sfxlinks = {"ui/button_clicked.wav", "player/jump.wav", "player/itempickup.wav", "player/climb.wav", "player/dooropen.wav", "player/inwater.wav", "player/hurt.wav", "gamestate/failed.wav", "gamestate/finished.wav"};
        sfx = new Clip[sfxlinks.length];
        for (int i=0; i<sfxlinks.length; i++) {
            sfx[i] = loadClip(sfxlinks[i]);
        }

        updateBgMusicVolume();
        updateSFXVolume();
    }

    private Clip loadClip(String audioLink) {
        URL url = getClass().getResource("/audio/" + audioLink);
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(url);
            Clip c = AudioSystem.getClip();
            c.open(ais);
            return c;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }

    public void playBgMusic(int musicID) {
        if (musicID == 1) {
            if (!bgmusic.isActive()) {
                bgmusic.setMicrosecondPosition(0);
                bgmusic.loop(Clip.LOOP_CONTINUOUSLY);
            }
        } else {
            stopBgMusic();
        }
    }
    public void playSFX(int sfxID) {
        if (!sfx[sfxID].isActive()) {
            sfx[sfxID].setMicrosecondPosition(0);
            sfx[sfxID].start();
        }
    }

    public void playGameOver() {
        stopBgMusic();
        sfx[GAME_OVER].setMicrosecondPosition(0);
        sfx[GAME_OVER].start();
    }
    public void playFinished() {
        stopBgMusic();
        sfx[DEMO_FINISHED].setMicrosecondPosition(0);
        sfx[DEMO_FINISHED].start();
    }

    public void stopBgMusic() {
        if (bgmusic.isActive()) bgmusic.stop();
    }

    public void setMasterVolume(float value) {
        this.masterVolume = value;
        updateBgMusicVolume();
        updateSFXVolume();
    }
    public void setBgMusicVolume(float value) {
        this.musicVolume = value;
        updateBgMusicVolume();
    }
    public void setSFXVolume(float value) {
        this.sfxVolume = value;
        updateSFXVolume();
    }

    private void updateBgMusicVolume() {
        FloatControl gainCtrl = (FloatControl) bgmusic.getControl(FloatControl.Type.MASTER_GAIN);
        float range = gainCtrl.getMaximum()-gainCtrl.getMinimum();
        float gain = (range*musicVolume)*masterVolume + gainCtrl.getMinimum();
        gainCtrl.setValue(gain);
    }
    private void updateSFXVolume() {
        for (Clip c : sfx) {
            FloatControl gainCtrl = (FloatControl) c.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainCtrl.getMaximum()-gainCtrl.getMinimum();
            float gain = (range*sfxVolume)*masterVolume + gainCtrl.getMinimum();
            gainCtrl.setValue(gain);
        }
    }
}
