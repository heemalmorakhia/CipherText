package com.example.group_6_csci_4176

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toGameButton = findViewById<Button>(R.id.toGameButton)
        toGameButton.setOnClickListener(_toGameClicked)

        val settingsButton = findViewById<Button>(R.id.settingsButton)
        settingsButton.setOnClickListener(_settingsClicked)
    }

    private val _toGameClicked = View.OnClickListener {
        val gameActivity = Intent(this, GameActivity::class.java)
        startActivity(gameActivity)
    }

    private val _settingsClicked = View.OnClickListener {
        val settingsActivity = Intent(this, SettingsActivity::class.java)
        startActivity(settingsActivity)
    }

    private val _quitClicked = View.OnClickListener {
    }
}