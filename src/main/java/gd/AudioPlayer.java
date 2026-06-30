package gd;

import javax.sound.sampled.*;
import java.io.File;

public class AudioPlayer {
    private Clip clip;
    private final String path;
    private long pausedPosition;

    public AudioPlayer(String path){
        this.path = path;
    }

    public void play(boolean loop) {
        try {
            AudioInputStream audio = AudioSystem.getAudioInputStream(new File(path));
            clip = AudioSystem.getClip();
            clip.open(audio);

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            clip.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            pausedPosition = clip.getMicrosecondPosition();
            clip.stop();
        }
    }

    public void resume() {
        if (clip != null) {
            clip.setMicrosecondPosition(pausedPosition);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.setMicrosecondPosition(0);
        }
    }
}
