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
    int volumeScale = 3;
    float volume;
    int volumePercentage = 100;
    public Sound()
    {
        soundURL[0] = getClass().getResource("/sound/sound_testing.wav");
        soundURL[1] = getClass().getResource("/sound/speak.wav");
        soundURL[2] = getClass().getResource("/sound/shoot.wav");
    }

    public void setFile(int index)  {
        try
        {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[index]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            System.out.println(fc);
            checkVolume(volumePercentage);
        } catch(Exception e)
        {
            e.printStackTrace();
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
        clip.stop();
    }
    public void checkVolume(int volumePercentage) {
        if (volumePercentage < 0 || volumePercentage > 100) {
            throw new IllegalArgumentException("Volume must be between 0 and 100");
        }

        // Chuyển đổi từ 0 - 100 thành -80 đến 6
        float minVolume = -80.0f; // Mức âm lượng nhỏ nhất trong FloatControl
        float maxVolume = 6.0f;   // Mức âm lượng lớn nhất trong FloatControl
        float volume = minVolume + ((float) volumePercentage / 100.0f) * (maxVolume - minVolume);

        if (fc != null) {
            fc.setValue(volume); // Cài đặt âm lượng cho FloatControl
        } else {
            System.out.println("FloatControl is not initialized.");
        }
    }
}
