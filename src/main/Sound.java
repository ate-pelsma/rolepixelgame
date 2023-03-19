package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;
    URL soundURL[] = new URL[30];

    public Sound() {
        this.soundURL[0] = getClass().getResource("/music/BlueBoyAdventure.wav");
        this.soundURL[1] = getClass().getResource("/music/coin.wav");
        this.soundURL[2] = getClass().getResource("/music/powerup.wav");
        this.soundURL[3] = getClass().getResource("/music/unlock.wav");
        this.soundURL[4] = getClass().getResource("/music/fanfare.wav");
    }

    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);

        } catch (Exception e) {
        }
    }
    public void play(){
        clip.start();
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
    }
}
