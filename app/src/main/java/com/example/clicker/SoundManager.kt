package com.example.clicker
import android.content.Context
import android.media.MediaPlayer

class SoundManager(private val context: Context) {

    private var currentPlayer: MediaPlayer? = null
    fun playSound(context: Context, soundResourceId: Int){
//        stopSound()
        currentPlayer = MediaPlayer.create(context, soundResourceId)
        currentPlayer?.setOnCompletionListener {
            stopSound()
        }
        currentPlayer?.start()

    }

    fun stopSound(){
        currentPlayer?.let {
            player ->
            if (player.isPlaying){
                player.stop()
            }
            player.release()
        }
        currentPlayer = null
    }
}