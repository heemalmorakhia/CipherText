package com.example.group_6_csci_4176

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.GsonBuilder
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import java.io.File
import java.io.FileWriter

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val saveButton = findViewById<Button>(R.id.settingsSaveButton)

        saveButton.setOnClickListener {
            val numOfTokens = findViewById<EditText>(R.id.numberOfTokensField).text.toString().toInt()
            val numOfGuesses = findViewById<EditText>(R.id.numberOfGuessesField).text.toString().toInt()
            val colourBlindSwitch = findViewById<Switch>(R.id.colorBlindSwitch).isChecked
            val duplicates =    if(numOfTokens < 6) (findViewById<Switch>(R.id.duplicatesSwitch).isChecked)
                                else true

            val data = Settings(numOfTokens, numOfGuesses, colourBlindSwitch, duplicates)

            val gson = GsonBuilder()
                .registerTypeAdapter(Settings::class.java, object : TypeAdapter<Settings>() {
                    override fun write(out: JsonWriter, value: Settings?) {
                        if (value == null) {
                            out.nullValue()
                            return
                        }
                        out.beginObject()
                        out.name("numberOfTokens").value(value.numberOfTokens)
                        out.name("numberOfGuesses").value(value.numberOfGuesses)
                        out.name("colourBlind").value(value.colourBlind)
                        out.name("duplicates").value(value.duplicates)
                        out.endObject()
                    }

                    override fun read(input: JsonReader): Settings? {
                        // Implement this method if you need to read JSON into Settings object
                        return null
                    }

                })
                .create()

            val json = gson.toJson(data)

            try {
                val file = File(getAssetsDir(this), "settings.json")
                val writer = FileWriter(file)
                writer.write(json)
                writer.close()
                Toast.makeText(this, "Settings Saved!", Toast.LENGTH_LONG).show()
                goBack()
            } catch (e: Exception) {
                Log.e("SettingsActivity", "Error writing to file", e)
            }

        }
    }

    private fun getAssetsDir(context: Context): File {
        return context.externalCacheDir ?: context.cacheDir
    }

    override fun onBackPressed() {
        super.onBackPressed() // This line is important, as it ensures that the default back button behavior is still executed if your custom code does not consume the event
        goBack()
    }

    fun goBack(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}