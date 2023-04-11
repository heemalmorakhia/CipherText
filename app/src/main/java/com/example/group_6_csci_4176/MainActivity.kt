package com.example.group_6_csci_4176

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    final lateinit var mediaPlayer : MediaPlayer
    final var soundOn : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toGameButton = findViewById<Button>(R.id.toGameButton)
        toGameButton.setOnClickListener(_toGameClicked)

        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener(_settingsClicked)

        val quitButton = findViewById<Button>(R.id.quitButton)
        quitButton.setOnClickListener(_quitClicked)

        mediaPlayer = MediaPlayer.create(this, R.raw.background)
        mediaPlayer.setVolume(0.05F, 0.05F)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        soundOn = true
    }

    private val _toGameClicked = View.OnClickListener {
        stopMediaPlayer()
        val gameActivity = Intent(this, GameActivity::class.java)
        startActivity(gameActivity)
    }

    private val _settingsClicked = View.OnClickListener {
        stopMediaPlayer()
        val settingsActivity = Intent(this, SettingsActivity::class.java)
        startActivity(settingsActivity)
    }

    private val _quitClicked = View.OnClickListener {
        stopMediaPlayer()
        finishAffinity()
    }

    fun stopMediaPlayer(){
        if(soundOn){
            mediaPlayer.stop()
            mediaPlayer.release()
            soundOn = false
        }
    }
}