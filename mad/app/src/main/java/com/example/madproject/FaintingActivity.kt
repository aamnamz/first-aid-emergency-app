package com.example.madproject

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.EditText // REQUIRED for finding the search input
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

// 1. Implement the TextToSpeech.OnInitListener interface
class FaintingActivity : AppCompatActivity(), OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var btnSpeak: Button
    private lateinit var instructionContainer: LinearLayout

    // Constants for button text states
    private val SPEAK_TEXT = "▶️ Speak Instructions"
    private val STOP_TEXT = "⏹️ Stop"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fainting) // Assuming your layout is named activity_bleeding

        // --- FIX STARTS HERE ---
        // 1. Find the search input EditText from the included toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        // 2. Fix the ToolbarHelper call: Pass the input and the required lambda callback
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // We leave this empty because guide activities usually don't have suggestions lists
        }
        // --- FIX ENDS HERE ---

        // Initialize TTS engine
        tts = TextToSpeech(this, this)
        setupTtsListener() // Set up listener to toggle button text

        btnSpeak = findViewById(R.id.btn_speak_instructions)
        instructionContainer = findViewById(R.id.instruction_container)

        // Set initial state
        btnSpeak.isEnabled = false
        btnSpeak.text = SPEAK_TEXT

        btnSpeak.setOnClickListener {
            if (tts.isSpeaking) {
                // If currently speaking, STOP it
                tts.stop()
            } else {
                // If not speaking, START it
                val instructions = extractInstructionsFromLayout()
                speakOut(instructions)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Stop TTS before leaving the activity
                if (::tts.isInitialized && tts.isSpeaking) {
                    tts.stop()
                }
                finish()
            }
        })
    }

    // Function to set up the UtteranceProgressListener for button state changes
    private fun setupTtsListener() {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            private val UTTERANCE_ID = "bleedingInstructions"

            override fun onStart(utteranceId: String?) {
                if (utteranceId == UTTERANCE_ID) {
                    runOnUiThread {
                        btnSpeak.text = STOP_TEXT
                    }
                }
            }

            override fun onDone(utteranceId: String?) {
                if (utteranceId == UTTERANCE_ID) {
                    runOnUiThread {
                        btnSpeak.text = SPEAK_TEXT
                    }
                }
            }

            override fun onError(utteranceId: String?) {
                if (utteranceId == UTTERANCE_ID) {
                    runOnUiThread {
                        btnSpeak.text = SPEAK_TEXT
                        Log.e("TTS", "Speech error.")
                    }
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onStop(utteranceId: String?, interrupted: Boolean) {
                runOnUiThread {
                    btnSpeak.text = SPEAK_TEXT
                    Log.d("TTS", "Speech stopped by user interaction.")
                }
            }
        })
    }

    /**
     * Extracts text from all TextViews within the instruction container.
     */
    private fun extractInstructionsFromLayout(): String {
        val stringBuilder = StringBuilder()

        // Iterate through all child views in the container
        for (i in 0 until instructionContainer.childCount) {
            val view: View = instructionContainer.getChildAt(i)

            // If the view is a TextView, append its text
            if (view is TextView) {
                val text = view.text.toString().trim()
                // Check to skip the main title and only append instructions/steps
                if (i > 0 && text.isNotEmpty() && !text.contains("First Aid")) {
                    stringBuilder.append(text).append(". ")
                }
            }
        }
        return stringBuilder.toString()
    }

    // TTS Initialization Callback
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "Language is not supported or missing data.")
                btnSpeak.text = "Language Error"
            } else {
                // Enable the button once TTS is ready
                runOnUiThread {
                    btnSpeak.isEnabled = true
                    btnSpeak.text = SPEAK_TEXT
                }
                Log.d("TTS", "TTS Initialization successful.")
            }
        } else {
            Log.e("TTS", "Initialization failed.")
            btnSpeak.text = "TTS Failed"
        }
    }

    // The actual speech function
    private fun speakOut(text: String) {
        // We use an utterance ID so the listener can track this specific speech request
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "bleedingInstructions")
    }

    // Clean up resources when the activity is destroyed
    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}