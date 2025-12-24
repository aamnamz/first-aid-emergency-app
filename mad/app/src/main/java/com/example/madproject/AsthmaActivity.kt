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
import android.widget.EditText
import android.widget.Toast // REQUIRED: Import for Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

// Implement the TextToSpeech.OnInitListener interface
class AsthmaActivity : AppCompatActivity(), OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var btnSpeak: Button
    private lateinit var instructionContainer: LinearLayout

    // --- FIX 1: Flag to track TTS readiness ---
    private var isTtsReady = false
    // Constants for button text states
    private val SPEAK_TEXT = "▶️ Speak Instructions"
    private val STOP_TEXT = "⏹️ Stop"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asthma)

        // --- 1. TOOLBAR SETUP FIX ---
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // Toolbar setup complete
        }

        // --- 2. TTS & UI INITIALIZATION ---

        tts = TextToSpeech(this, this)
        setupTtsListener()

        btnSpeak = findViewById(R.id.btn_speak_instructions)
        instructionContainer = findViewById(R.id.instruction_container)

        btnSpeak.isEnabled = false // Disable initially
        btnSpeak.text = SPEAK_TEXT

        // --- FIX 2: Modified Click Listener to check isTtsReady ---
        btnSpeak.setOnClickListener {
            // Safety check: Don't proceed if the engine hasn't fully initialized
            if (!isTtsReady) {
                Toast.makeText(this, "Speech engine is still loading. Please wait.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (tts.isSpeaking) {
                // If currently speaking, STOP it
                tts.stop()
            } else {
                // If not speaking, START it
                val instructions = extractInstructionsFromLayout()
                speakOut(instructions)
            }
        }
        // --- END FIX 2 ---

        // Handle Back Button Press (stops TTS and finishes activity)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
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
            // NOTE: Changed UTTERANCE_ID to "asthmaInstructions" for better context
            private val UTTERANCE_ID = "asthmaInstructions"

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
                        Log.e("TTS", "Speech error in AsthmaActivity.")
                    }
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onStop(utteranceId: String?, interrupted: Boolean) {
                runOnUiThread {
                    btnSpeak.text = SPEAK_TEXT
                    Log.d("TTS", "Speech stopped by user interaction in AsthmaActivity.")
                }
            }
        })
    }

    /**
     * Extracts text from all TextViews within the instruction container.
     */
    private fun extractInstructionsFromLayout(): String {
        val stringBuilder = StringBuilder()

        for (i in 0 until instructionContainer.childCount) {
            val view: View = instructionContainer.getChildAt(i)

            if (view is TextView) {
                val text = view.text.toString().trim()
                if (text.isNotEmpty() && !text.contains("First Aid")) {
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
                Log.e("TTS", "Language is not supported or missing data in AsthmaActivity.")
                btnSpeak.text = "Language Error"
            } else {
                // --- FIX 1: Set flag to true and enable button ---
                isTtsReady = true
                runOnUiThread {
                    btnSpeak.isEnabled = true
                    btnSpeak.text = SPEAK_TEXT
                }
                Log.d("TTS", "TTS Initialization successful in AsthmaActivity.")
            }
        } else {
            Log.e("TTS", "Initialization failed in AsthmaActivity.")
            btnSpeak.text = "TTS Failed"
        }
    }

    // The actual speech function
    private fun speakOut(text: String) {
        // Safety check to avoid crash on empty text
        if (text.isNotEmpty()) {
            tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "asthmaInstructions")
        }
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