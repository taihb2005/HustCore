package main;

import java.net.URL;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];
    FloatControl fc;
    int volumeScale = 6;
    float volume;
    int volumePercentage = 40;
    long savedPos = 0;
    public Sound()
    {
        soundURL[0] = getClass().getResource("/sound/TitleScreen2.wav");
        soundURL[1] = getClass().getResource("/sound/speak.wav");
        soundURL[2] = getClass().getResource("/sound/shoot.wav");
        soundURL[3] = getClass().getResource("/sound/levelup.wav");
        soundURL[4] = getClass().getResource("/sound/Thua.wav");
        soundURL[5] = getClass().getResource("/sound/Boss.wav");
        soundURL[6] = getClass().getResource("/sound/NhacLevel.wav");
        soundURL[7] = getClass().getResource("/sound/Quizz.wav");
        soundURL[8] = getClass().getResource("/sound/creditMusic.wav");
        soundURL[9] = getClass().getResource("/sound/fanfare.wav");
        soundURL[10] = getClass().getResource("/sound/minigame.wav");
        soundURL[11] = getClass().getResource("/sound/gong.wav");
    }

    public void setFile(int index)  {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[index]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume(volumePercentage);
        } catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    public void pause(){
        if (clip != null && clip.isRunning()) {
            long currentFramePosition = clip.getFramePosition();
            clip.stop();
            savedPos = currentFramePosition;
        }
    }
    public void resume(){
        if (clip != null && !clip.isRunning()) {
            clip.setFramePosition((int)savedPos);
            clip.start();
            savedPos = 0;
        }
    }
    public void play()
    {
        clip.start();
    }
    public void loop()
    {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop()
    {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }
    public void checkVolume(int volumePercentage) {
        if (volumePercentage < 0 || volumePercentage > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }

        switch (volumePercentage)
        {
            case 0 : volume = -80f; break;
            case 10 : volume = -40f; break;
            case 20 : volume = -30f; break;
            case 30 : volume = -20f; break;
            case 40 : volume = -12f; break;
            case 50 : volume = -6f; break;
            case 60 : volume = 0f; break;
            case 70 : volume = 3f; break;
            case 80 : volume = 4.5f; break;
            case 90 : volume = 5.5f; break;
            case 100: volume = 6f; break;
            default: volume = -80f; break;
        }


        if (fc != null) {
            fc.setValue(volume); // Cài đặt âm lượng cho FloatControl
        } else {
            System.out.println("FloatControl is not initialized.");
        }
    }
}
