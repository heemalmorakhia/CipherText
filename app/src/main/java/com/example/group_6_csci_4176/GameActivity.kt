package com.example.group_6_csci_4176

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import org.json.JSONObject

data class Settings(var numberOfTokens: Int = 0,
                    var numberOfGuesses: Int = 0,
                    var colourBlind : Boolean = false,
                    var duplicates : Boolean = true);

class GameActivity : AppCompatActivity() {
    final lateinit var gameEngine: GameEngine

    var guessedPattern = IntArray(4)
    var guessedPatternIndex = 0

    var guessesArray = Array(4) {""}
    var attempts = 0

    private val buttonsArray: ArrayList<Button> = ArrayList()
    val codeLength = 4 //Replace with settings info
    val buttonWeight : Float = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameEngine = GameEngine
        gameEngine.CreateGame(readSettings())

        println("Made it here.")

        // guessedPattern = IntArray(gameEngine.GetCodeLength())

        val value1Button = findViewById<Button>(R.id.value1Button)
        value1Button.setOnClickListener(_value1Clicked)
        val value2Button = findViewById<Button>(R.id.value2Button)
        value2Button.setOnClickListener(_value2Clicked)
        val value3Button = findViewById<Button>(R.id.value3Button)
        value3Button.setOnClickListener(_value3Clicked)
        val value4Button = findViewById<Button>(R.id.value4Button)
        value4Button.setOnClickListener(_value4Clicked)
        val value5Button = findViewById<Button>(R.id.value5Button)
        value5Button.setOnClickListener(_value5Clicked)
        val value6Button = findViewById<Button>(R.id.value6Button)
        value6Button.setOnClickListener(_value6Clicked)

        val deleteButton = findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener(_deleteButtonClicked)

        /*
        https://stackoverflow.com/questions/12227310/how-can-we-use-a-variable-in-r-id
        listOf(value1Button, value2Button, value3Button, value4Button,
            value4Button, value5Button, value6Button).forEach {
                it.setOnClickListener(::valueButtonClick)
        }
        */

        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener(_submitClicked)

        val containerLayout = findViewById<LinearLayout>(R.id.containerLayout)
        for (i in 0 until codeLength) {
            val button = Button(this)
            button.isEnabled = false
            button.setBackgroundColor(Color.TRANSPARENT)
            buttonsArray.add(button)
            val layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                buttonWeight
            )
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            containerLayout.addView(button, layoutParams)
        }
    }

    /*
    https://stackoverflow.com/questions/12227310/how-can-we-use-a-variable-in-r-id
    private fun valueButtonClick(view: View) {
        with (view as Button) {
            guessedPattern[guessedPatternIndex] = findViewById<Button>(R.id.).text.toString().toInt()
            guessedPatternIndex++
        }
    }
    */

    @SuppressLint("SuspiciousIndentation")
    private val _value1Clicked = View.OnClickListener {
        guessedPattern[guessedPatternIndex] = findViewById<Button>(R.id.value1Button).text.toString().toInt()
        userInput("1", "#ff0000", guessedPatternIndex)
        guessedPatternIndex++
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value2Clicked = View.OnClickListener {
        guessedPattern[guessedPatternIndex] = findViewById<Button>(R.id.value2Button).text.toString().toInt()
        userInput("2", "#ffa500", guessedPatternIndex)
        guessedPatternIndex++
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value3Clicked = View.OnClickListener {
        guessedPattern[guessedPatternIndex] = findViewById<Button>(R.id.value3Button).text.toString().toInt()
        userInput("3", "#ffff00", guessedPatternIndex)
        guessedPatternIndex++
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value4Clicked = View.OnClickListener {
        guessedPattern[guessedPatternIndex] = findViewById<Button>(R.id.value4Button).text.toString().toInt()
        userInput("4", "#008000", guessedPatternIndex)
        guessedPatternIndex++
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value5Clicked = View.OnClickListener {
        guessedPattern[guessedPatternIndex] = findViewById<Button>(R.id.value5Button).text.toString().toInt()
        userInput("5", "#0000ff", guessedPatternIndex)
        guessedPatternIndex++

    }

    @SuppressLint("SuspiciousIndentation")
    private val _value6Clicked = View.OnClickListener {
        guessedPattern[guessedPatternIndex] = findViewById<Button>(R.id.value6Button).text.toString().toInt()
        userInput("6", "#800080", guessedPatternIndex)
        guessedPatternIndex++
    }

    @SuppressLint("SuspiciousIndentation")
    private val _deleteButtonClicked = View.OnClickListener {
        guessedPatternIndex -= 1
        guessedPattern[guessedPatternIndex] = 0
        userInput("", "#00FFFFFF", guessedPatternIndex)
    }


    @SuppressLint("SuspiciousIndentation")
    private val _submitClicked = View.OnClickListener {
        // Create the guessed pattern array
        // var guessedPattern = IntArray(gameEngine.GetCodeLength())

        // Get the values from the guesses provided by the user
        // guessedPattern[0] = findViewById<Button>(R.id.value1).text.toString().toInt()
        // guessedPattern[1] = findViewById<Button>(R.id.value2).text.toString().toInt()
        // guessedPattern[2] = findViewById<Button>(R.id.value3).text.toString().toInt()
        // guessedPattern[3] = findViewById<Button>(R.id.value4).text.toString().toInt()
        // guessedPattern[4] = findViewById<Button>(R.id.value5).text.toString().toInt()
        // guessedPattern[5] = findViewById<Button>(R.id.value6).text.toString().toInt()
        // guessedPattern[6] = findViewById<Button>(R.id.value7).text.toString().toInt()
        // guessedPattern[7] = findViewById<Button>(R.id.value8).text.toString().toInt()

        if (attempts === 4) {
            findViewById<TextView>(R.id.resultsText).text = "YOU LOSE!!!"
        }
        // Test the results provided
        var results = gameEngine.TestResult(guessedPattern)

        if(results[0] == gameEngine.GetCodeLength())
            findViewById<TextView>(R.id.resultsText).text = "YOU WIN!!!"
        else
        // display the results to the user
        findViewById<TextView>(R.id.resultsText).text =
            "Fully Correct: ${results[0]}\nPartially Correct: ${results[1]}\nIncorrect: ${results[2]}"

        guessesArray[attempts] = guessedPattern.joinToString(" ")
        attempts += 1
        var previousAttemptsText = guessesArray.joinToString("\n")
        findViewById<TextView>(R.id.resultsText2).text =
            "${previousAttemptsText}"

        guessedPattern = IntArray(4)
        guessedPatternIndex = 0
    }

    private fun userInput(buttonText: String, color: String, index: Int) {
        buttonsArray[index].text = buttonText
        buttonsArray[index].setBackgroundColor(Color.parseColor(color))
    }

    private fun readSettings(): Settings {
        val settingsJSON = JSONObject(
            applicationContext.assets.open("settings.json").bufferedReader().use { it.readText() })

        return Settings(
            numberOfTokens = settingsJSON.getInt("numberOfTokens"),
            numberOfGuesses = settingsJSON.getInt("numberOfGuesses"),
            colourBlind = settingsJSON.getBoolean("colourBlind"),
            duplicates = settingsJSON.getBoolean("duplicates")
        )
    }
}
