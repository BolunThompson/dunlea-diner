package com.mygdx.game;

import com.badlogic.gdx.audio.Sound;

/**
 * Created May 28, 2023
 *
 * (To be deleted later)
 *
 * HELLO PERSON. IT'S YOUR LUCKY DAY. YOU'VE BEEN SELECTED TO ADD SOUNDS TO THE GAME.
 * ADDING SOUNDS IS VERY SIMPLE. SEE THE STEPS BELOW:
 */
public class exampleSound {

    // FIRST CREATE A SOUND OBJECT
    Sound sound;

    public exampleSound(Sound sound)
    {
        this.sound = sound; // initialize sound object
    }

    // TO PLAY THE SOUND, SIMPLY USE THE METHOD sound.play()
    // THE PARAMETER (here it's 1.0f) CONTROLS THE VOLUME OF THE SOUND
    public void playSound()
    {
        sound.play(1.0f);
    }

    // MAKE SURE YOU REMEMBER TO DISPOSE EVERY SOUND OBJECT IN THE DISPOSE METHOD
    public void dispose()
    {
        sound.dispose();
    }

    /**
     * NOW YOU'RE READY TO ADD SOUNDS TO THE GAME.
     * BUT FIRST, TO MAKE THINGS EASIER FOR YOURSELF, RENAME ALL THE SOUND FILES TO APPROPRIATE NAMES WITH NO SPACES,
     * SUCH AS "Toaster-placeItemSound.wav"
     *
     * 1. CREATE SOUND OBJECTS FOR EACH SOUND EFFECT IN THE APPROPRIATE CLASSES
     *      (ex. toaster sound in Toaster.java class)
     * 2. USE sound.play() TO PLAY THE SOUND AT THE RIGHT PLACE
     * 3. PROFIT???
     *
     *
     * LEARN MORE ABOUT SOUND EFFECTS:
     * https://libgdx.com/wiki/audio/sound-effects
     *
     * TO ADD MUSIC, USE A MUSIC OBJECT:
     * https://libgdx.com/wiki/audio/streaming-music
     */
}
