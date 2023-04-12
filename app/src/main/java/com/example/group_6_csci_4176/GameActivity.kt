package com.example.group_6_csci_4176

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.json.JSONObject
import java.io.File
import java.io.FileReader
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

    var gameOver by Delegates.notNull<Boolean>()

    private val buttonsArray: ArrayList<Button> = ArrayList()
    val buttonWeight : Float = 1.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        gameEngine = GameEngine
        gameEngine.CreateGame(readSettings())

        buttonColors = readColors()

        // Sets the guessed code's length dynamically following settings.
        codeLength = gameEngine.GetCodeLength()
        guessedPattern = IntArray(codeLength)

        gameOver = false

        // Creates a button connected to the gameActivity interface buttons for each guess element.

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

        val submitButton = findViewById<Button>(R.id.submitButton)
        submitButton.setOnClickListener(_submitClicked)

        val containerLayout = findViewById<LinearLayout>(R.id.containerLayout)

        // Creates the buttons so the user can see their guessed pattern.
        for (i in 0 until codeLength) {
            val button = Button(this)
            button.isEnabled = false
            button.setBackgroundColor(Color.TRANSPARENT)
            buttonsArray.add(button)

            // The code for the dynamic alteration of the layout of new buttons was adapted from [7] and [4].
            // [7] wael, “android - Dynamically add view with LayoutParams - Stack Overflow,” Stack Overflow, 27-Mar-2014. [Online]. Available: https://stackoverflow.com/questions/22700408/dynamically-add-view-with-layoutparams. [Accessed: 11-Apr-2023]. 
            // [4] MFP, “android - center button programmatically and dynamic layout - Stack Overflow,” Stack Overflow, 07-Jul-2014. [Online]. Available: https://stackoverflow.com/questions/24606263/center-button-programmatically-and-dynamic-layout. [Accessed: 11-Apr-2023]. 

            //Adds layout params to dynamically change the layout based on the number of buttons.
            val layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                buttonWeight
            )
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL
            //Adds buttons (with layout parameters) to the LinearLayout.
            containerLayout.addView(button, layoutParams)
        }
    }

    // Adds value 1 to the current guess.
    @SuppressLint("SuspiciousIndentation")
    private val _value1Clicked = View.OnClickListener {
        println(buttonColors.color1)
        addToGuess("1", buttonColors.color1)
    }

    // Adds value 2 to the current guess.
    @SuppressLint("SuspiciousIndentation")
    private val _value2Clicked = View.OnClickListener {
        addToGuess("2", buttonColors.color2)
    }

    // Adds value 3 to the current guess.
    @SuppressLint("SuspiciousIndentation")
    private val _value3Clicked = View.OnClickListener {
        addToGuess("3", buttonColors.color3)
    }

    // Adds value 4 to the current guess.
    @SuppressLint("SuspiciousIndentation")
    private val _value4Clicked = View.OnClickListener {
        addToGuess("4", buttonColors.color4)
    }

    // Adds value 5 to the current guess.
    @SuppressLint("SuspiciousIndentation")
    private val _value5Clicked = View.OnClickListener {
        addToGuess("5", buttonColors.color5)
    }

    // Adds value 6 to the current guess.
    @SuppressLint("SuspiciousIndentation")
    private val _value6Clicked = View.OnClickListener {
        addToGuess("6", buttonColors.color6)
    }

    // On delete, removes most recent pressed guess button.
    @SuppressLint("SuspiciousIndentation")
    private val _deleteButtonClicked = View.OnClickListener {
        if (guessedPatternIndex == 0) return@OnClickListener
        guessedPatternIndex -= 1
        guessedPattern[guessedPatternIndex] = 0
        buttonsArray[guessedPatternIndex].text = ""
        buttonsArray[guessedPatternIndex].setBackgroundColor(Color.TRANSPARENT)
    }

    // On submit, tests entered guess against correct pattern.
    @SuppressLint("SuspiciousIndentation")
    private val _submitClicked = View.OnClickListener {
        // If the entered the pattern is not the correct length, does not continue.
        if (guessedPatternIndex != codeLength)
            return@OnClickListener

        // If the game is over, does not continue.
        if(gameOver) return@OnClickListener

        // Test the results provided.
        var results = gameEngine.TestResult(guessedPattern)

        guessedPattern = IntArray(codeLength)
        guessedPatternIndex = 0

        handlePreviousAttempt(results)

    }

    // Resets the array of guess buttons pressed by the user.
    private fun resetUserInput() {
        for (i in 0 until codeLength) {
            buttonsArray[i].setBackgroundColor(Color.TRANSPARENT)
            buttonsArray[i].text = ""
        }
    }

    // Adds guess button pressed by user to current guess array.
    private fun addToGuess(buttonText: String, color: String) {
        if (guessedPatternIndex == codeLength) return
        guessedPattern[guessedPatternIndex] = buttonText.toInt()
        buttonsArray[guessedPatternIndex].text = buttonText
        buttonsArray[guessedPatternIndex].setBackgroundColor(Color.parseColor(color))
        buttonsArray[guessedPatternIndex].setTextColor(Color.parseColor("Black"))
        guessedPatternIndex++
    }

    @SuppressLint("Range")
    private fun handlePreviousAttempt(results: IntArray) {
        // The code for the dynamic alteration of the layout of new buttons was adapted from [7] and [4].
        // [7] wael, “android - Dynamically add view with LayoutParams - Stack Overflow,” Stack Overflow, 27-Mar-2014. [Online]. Available: https://stackoverflow.com/questions/22700408/dynamically-add-view-with-layoutparams. [Accessed: 11-Apr-2023]. 
        // [4] MFP, “android - center button programmatically and dynamic layout - Stack Overflow,” Stack Overflow, 07-Jul-2014. [Online]. Available: https://stackoverflow.com/questions/24606263/center-button-programmatically-and-dynamic-layout. [Accessed: 11-Apr-2023].
        
        // Uses nested LinearLayouts to display the previous attempts.
        val previousGuessesLayout = findViewById<LinearLayout>(R.id.previousGuesses)
        
        // LinearLayout that contains the guesses for one attempt.
        val row = LinearLayout(this)
        
        row.orientation = LinearLayout.HORIZONTAL
        for (i in 0 until codeLength) {
            val newButton = Button(this)
            val colorCode: Int = buttonsArray[i].text.toString().toInt()
            newButton.text = buttonsArray[i].text
            newButton.setTextColor(Color.parseColor("Black"))
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

        // Checks and outputs the explanation of results for previous guesses on the gameActivity interface.
        val resultsExplained = TextView(this)
        resultsExplained.setTypeface(Typeface.DEFAULT_BOLD)

        if(results[0] == gameEngine.GetCodeLength()) {
            Toast.makeText(applicationContext,"YOU WIN!!!", Toast.LENGTH_LONG).show()
            gameOver = true
            resultsExplained.text = "YOU WIN!!!"
        }
        else if (gameEngine.GameLost()) {
            Toast.makeText(applicationContext,"You Lose! :(", Toast.LENGTH_SHORT).show()
            gameOver = true
            resultsExplained.text = "You Lose! :("
        } else {
            resultsExplained.text =
                "Fully Correct: ${results[0]}\nPartially Correct: ${results[1]}\nIncorrect: ${results[2]}"
        }
        previousGuessesLayout.addView(row)
        previousGuessesLayout.addView(resultsExplained)
        resetUserInput()
    }

    private fun getAssetsDir(context: Context): File {
        return context.externalCacheDir ?: context.cacheDir
    }

    // Reads the necessary settings values from the settings file.
    private fun readSettings(): Settings {
        try{
            val file = File(getAssetsDir(this), "settings.json")

            val gson = Gson()

            val reader = FileReader(file)

            return  gson.fromJson(reader, Settings::class.java)
        } catch (e: Exception){
            // If the user has never created a settings file, then this will serve as the default.
            return Settings(numberOfTokens = 4,
                numberOfGuesses = 10,
                duplicates = true,
                colourBlind = false)
        }
    }

    // Determines if colors present on gameActivity interface should be regular or colorblind enabled based on settings 
    // and alters accordingly.
    private fun readColors(): ColorOptions {
        val colorOption = gameEngine.GetColorblingOption()

        val colorsJSON = JSONObject(
            applicationContext.assets.open("colors.json").bufferedReader().use {it.readText()}
        )
        var arrayName: String
        if(colorOption)
            arrayName = "regular"
        else
            arrayName = "colorblindness"
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

    // Returns to main menu mainActivity.
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
