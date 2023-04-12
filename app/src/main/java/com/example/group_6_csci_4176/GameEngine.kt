package com.example.group_6_csci_4176

import kotlin.properties.Delegates
import kotlin.random.Random

// The code for the data class was adapted from [2].
// [2] “Data classes | Kotlin Documentation,” Kotlin, 11-Apr-2023. [Online]. 
// Available: https://kotlinlang.org/docs/data-classes.html#properties-declared-in-the-class-body. [Accessed: 11-Apr-2023]. 
data class Token(var value: Int = 0, var used: Boolean = false)

object GameEngine {
    private lateinit var masterCode : Array<Token>
    private var guesses by Delegates.notNull<Int>()
    private var maxGuesses by Delegates.notNull<Int>()
    private var colorblindOption: Boolean = false

    private fun GameEngine(){}

    // Create the game by selecting 4 total different numbers from 1-6.
    fun CreateGame(settings : Settings){
        guesses = 0
        maxGuesses = settings.numberOfGuesses
        colorblindOption = settings.colourBlind

        // Create an array of different tokens.
        GenerateCipher(settings.numberOfTokens, settings.duplicates)
    }

    private fun GenerateCipher(numberOfTokens : Int, duplicates : Boolean){
        masterCode = if(duplicates) Array(numberOfTokens){ Token(Random.nextInt(1, 6 + 1)) }
        else {
            val potentialValues : List<Int> =  (1..6).shuffled().take(numberOfTokens)
            Array(numberOfTokens){i -> Token(potentialValues[i])}
        }
    }

    fun TestResult(guessedCode : IntArray) : IntArray {
        /*
        *   The results array is to be read as follows:
        *
        *   results[0] = # of correct colours in the correct location
        *   results[1] = # of correct colours in the incorrect position
        *   results[2] = # of incorrect colours
        */
        guesses ++
        var results = IntArray(3)

        ResetMasterCode()

        // check for all the correct guesses in correct location. 
        // This must be done first to avoid the issue of positions being used for other results.
        for(i in guessedCode.indices){
            if(guessedCode[i] == masterCode[i].value) {
                masterCode[i].used = true
                results[0] += 1
            }
        }

        // Retrieve the guesses.
        for(i in guessedCode.indices){
            if(HasAvailablePosition(guessedCode[i])) results[1] += 1
        }

        // The remaining guesses are incorrect.
        results[2] = masterCode.size - (results[1] + results[0])

        return results
    }

    fun GetCodeLength() : Int {
        return masterCode.size
    }

    fun GetColorblingOption() : Boolean {
        return colorblindOption
    }

    fun GameLost() : Boolean {
        return guesses == maxGuesses
    }

    fun GetGuesses() : Int {
        return guesses
    }

    /*
    HELPER FUNCTIONS
    */

    // Check for the correct guesses in incorrect locations. 
    // This had to be done to avoid false positives/negatives.
    private fun HasAvailablePosition(value: Int) : Boolean{
        for(i in masterCode.indices)
            if(masterCode[i].value == value && !masterCode[i].used){
                masterCode[i].used = true
                return true
            }

        return false
    }

    private fun ResetMasterCode(){
        for(i in masterCode.indices)
            masterCode[i].used = false
    }
}