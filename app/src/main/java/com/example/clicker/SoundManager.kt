package com.example.clicker
import android.content.Context
import android.media.MediaPlayer

class SoundManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var currentSoundResourceId: Int? = null
    fun playSound(soundResourceId: Int, ){
        stopSound()
        mediaPlayer = MediaPlayer.create(context, soundResourceId)
        mediaPlayer?.start()
    }

    fun stopSound(){
        mediaPlayer?.stop()
    }
}