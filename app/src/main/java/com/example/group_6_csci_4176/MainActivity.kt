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

        // Calls the _toGameClicked event.
        val toGameButton = findViewById<Button>(R.id.toGameButton)
        toGameButton.setOnClickListener(_toGameClicked)

        // Calls the _settingsClicked event.
        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener(_settingsClicked)

        // Calls the _quitClicked event.
        val quitButton = findViewById<Button>(R.id.quitButton)
        quitButton.setOnClickListener(_quitClicked)

        // Initializes mediaPlayer for background music.
        mediaPlayer = MediaPlayer.create(this, R.raw.background)
        mediaPlayer.setVolume(0.1F, 0.1F)
        mediaPlayer.isLooping = true
        mediaPlayer.start()
        soundOn = true
    }

    // Starts the gameActivity.
    private val _toGameClicked = View.OnClickListener {
        stopMediaPlayer()
        val gameActivity = Intent(this, GameActivity::class.java)
        startActivity(gameActivity)
    }

    // Starts the settingsActivity.
    private val _settingsClicked = View.OnClickListener {
        stopMediaPlayer()
        val settingsActivity = Intent(this, SettingsActivity::class.java)
        startActivity(settingsActivity)
    }

    // Closes the application.
    private val _quitClicked = View.OnClickListener {
        stopMediaPlayer()
        finishAffinity()
    }

    // Stops the mediaPlayer for background music.
    fun stopMediaPlayer(){
        if(soundOn){
            mediaPlayer.stop()
            mediaPlayer.release()
            soundOn = false
        }
    }
}