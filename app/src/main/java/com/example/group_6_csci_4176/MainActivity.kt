package com.example.group_6_csci_4176

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    final lateinit var gameEngine: GameEngine

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        gameEngine = GameEngine
        gameEngine.CreateGame()

        val submitButton = findViewById<Button>(R.id.submitGuessButton)
        submitButton.setOnClickListener(_submitClicked)
    }

    private val _submitClicked = View.OnClickListener {
        // Create the guessed pattern array
        var guessedPattern = IntArray(gameEngine.GetCodeLength())

        // Get the values from the guesses provided by the user
        guessedPattern[0] = findViewById<EditText>(R.id.value1).text.toString().toInt()
        guessedPattern[1] = findViewById<EditText>(R.id.value2).text.toString().toInt()
        guessedPattern[2] = findViewById<EditText>(R.id.value3).text.toString().toInt()
        guessedPattern[3] = findViewById<EditText>(R.id.value4).text.toString().toInt()
        guessedPattern[4] = findViewById<EditText>(R.id.value5).text.toString().toInt()
        guessedPattern[5] = findViewById<EditText>(R.id.value6).text.toString().toInt()
        guessedPattern[6] = findViewById<EditText>(R.id.value7).text.toString().toInt()
        guessedPattern[7] = findViewById<EditText>(R.id.value8).text.toString().toInt()

        // Test the results provided
        var results = gameEngine.TestResult(guessedPattern)

        if(results[0] == gameEngine.GetCodeLength())
            findViewById<TextView>(R.id.resultsText).text = "YOU WIN!!!"
        else
        // display the results to the user
        findViewById<TextView>(R.id.resultsText).text =
            "Fully Correct: ${results[0]}\nPartially Correct: ${results[1]}\nIncorrect: ${results[2]}"
    }
}
