package com.mygdx.game;

import com.badlogic.gdx.audio.Music;

/**
 * (To be deleted later)
 *
 * More info here:
 * https://libgdx.com/wiki/audio/streaming-music
 */
public class exampleMusic {

    Music music;

    public exampleMusic(Music music)
    {
        this.music = music;
        music.setVolume(0.2f);
        music.play();
    }

    public void pause()
    {
        if(music.isPlaying())
            music.pause();
        else
            music.play();
    }

    public void dispose()
    {
        music.dispose();
    }
}
