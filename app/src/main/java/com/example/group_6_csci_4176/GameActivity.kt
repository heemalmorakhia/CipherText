package com.example.group_6_csci_4176

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.PorterDuff
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
import org.json.JSONArray
import org.json.JSONObject
import kotlin.properties.Delegates

data class Settings(var numberOfTokens: Int = 0,
                    var numberOfGuesses: Int = 0,
                    var colourBlind : Boolean = false,
                    var duplicates : Boolean = true);
data class ColorOptions(var colorOption: Boolean = false,
                        var color1: String = "",
                        var color2: String = "",
                        var color3: String = "",
                        var color4: String = "",
                        var color5: String = "",
                        var color6: String = "");

class GameActivity : AppCompatActivity() {
    final lateinit var gameEngine: GameEngine
    var codeLength by Delegates.notNull<Int>()

    lateinit var guessedPattern: IntArray
    var guessedPatternIndex = 0
    final lateinit var buttonColors: ColorOptions

    //lateinit var guessesArray = Array<String>

    private val buttonsArray: ArrayList<Button> = ArrayList()
    val buttonWeight : Float = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameEngine = GameEngine
        gameEngine.CreateGame(readSettings())

        buttonColors = readColors()

        codeLength = gameEngine.GetCodeLength()
        guessedPattern = IntArray(codeLength)

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
        println(buttonColors.color1)
        addToGuess("1", buttonColors.color1)
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value2Clicked = View.OnClickListener {
        addToGuess("2", buttonColors.color2)
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value3Clicked = View.OnClickListener {
        addToGuess("3", buttonColors.color3)
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value4Clicked = View.OnClickListener {
        addToGuess("4", buttonColors.color4)
    }

    @SuppressLint("SuspiciousIndentation")
    private val _value5Clicked = View.OnClickListener {
        addToGuess("5", buttonColors.color5)

    }

    @SuppressLint("SuspiciousIndentation")
    private val _value6Clicked = View.OnClickListener {
        addToGuess("6", buttonColors.color6)
    }

    @SuppressLint("SuspiciousIndentation")
    private val _deleteButtonClicked = View.OnClickListener {
        if (guessedPatternIndex == 0) return@OnClickListener
        guessedPatternIndex -= 1
        guessedPattern[guessedPatternIndex] = 0
        buttonsArray[guessedPatternIndex].text = ""
        buttonsArray[guessedPatternIndex].setBackgroundColor(Color.TRANSPARENT)
    }


    @SuppressLint("SuspiciousIndentation")
    private val _submitClicked = View.OnClickListener {
        if (guessedPatternIndex != codeLength)
            //Add toast here
            return@OnClickListener
        // Test the results provided
        var results = gameEngine.TestResult(guessedPattern)

        if(results[0] == gameEngine.GetCodeLength())
            findViewById<TextView>(R.id.resultsText).text = "YOU WIN!!!"
        else if (gameEngine.GameLost()) {
            findViewById<TextView>(R.id.resultsText).text = "YOU LOSE!!!"
        }
        else
        // display the results to the user
        findViewById<TextView>(R.id.resultsText).text =
            "Fully Correct: ${results[0]}\nPartially Correct: ${results[1]}\nIncorrect: ${results[2]}"

        //guessesArray[attempts] = guessedPattern.joinToString(" ")
        //attempts += 1
        //var previousAttemptsText = guessesArray.joinToString("\n")

        guessedPattern = IntArray(codeLength)
        guessedPatternIndex = 0

        handlePreviousAttempt(results)

    }

    private fun resetUserInput() {
        for (i in 0 until codeLength) {
            buttonsArray[i].setBackgroundColor(Color.TRANSPARENT)
            buttonsArray[i].text = ""
        }
    }

    private fun addToGuess(buttonText: String, color: String) {
        if (guessedPatternIndex == codeLength) return
        guessedPattern[guessedPatternIndex] = buttonText.toInt()
        buttonsArray[guessedPatternIndex].text = buttonText
        buttonsArray[guessedPatternIndex].setBackgroundColor(Color.parseColor(color))
        guessedPatternIndex++
    }

    @SuppressLint("Range")
    private fun handlePreviousAttempt(results: IntArray) {
        val previousGuessesLayout = findViewById<LinearLayout>(R.id.previousGuesses)
        //Linear Layout that contains the guesses for one attempt
        val row = LinearLayout(this)
        row.orientation = LinearLayout.HORIZONTAL
        for (i in 0 until codeLength) {
            val newButton = Button(this)
            val colorCode: Int = buttonsArray[i].text.toString().toInt()
            newButton.text = buttonsArray[i].text
            when(colorCode) {
                1 -> newButton.setBackgroundColor(Color.parseColor(buttonColors.color1))
                2 -> newButton.setBackgroundColor(Color.parseColor(buttonColors.color2))
                3 -> newButton.setBackgroundColor(Color.parseColor(buttonColors.color3))
                4 -> newButton.setBackgroundColor(Color.parseColor(buttonColors.color4))
                5 -> newButton.setBackgroundColor(Color.parseColor(buttonColors.color5))
                6 -> newButton.setBackgroundColor(Color.parseColor(buttonColors.color6))
            }
            val layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                buttonWeight
            )
            row.addView(newButton, layoutParams)
        }
        val resultsExplained = TextView(this)
        resultsExplained.text = "Fully Correct: ${results[0]}\nPartially Correct: ${results[1]}\nIncorrect: ${results[2]}"
        previousGuessesLayout.addView(row)
        previousGuessesLayout.addView(resultsExplained)
        resetUserInput()
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

    private fun readColors(): ColorOptions {
        val colorOption = gameEngine.GetColorblingOption()

        val colorsJSON = JSONObject(
            applicationContext.assets.open("colors.json").bufferedReader().use {it.readText()}
        )
        var arrayName: String
        if(colorOption)
            arrayName = "regular"
        else
            arrayName = "colorblindness1"
        val jsonArray:JSONObject = colorsJSON.getJSONObject(arrayName)
        return ColorOptions(
            colorOption = colorOption,
            color1 = jsonArray.getString("1"),
            color2 = jsonArray.getString("2"),
            color3 = jsonArray.getString("3"),
            color4 = jsonArray.getString("4"),
            color5 = jsonArray.getString("5"),
            color6 = jsonArray.getString("6")
        )
    }
}
