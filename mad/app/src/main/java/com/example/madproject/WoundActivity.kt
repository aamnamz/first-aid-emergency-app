package com.example.madproject

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale

class WoundActivity : AppCompatActivity(), TextToSpeech.OnInitListener {

    private lateinit var tts: TextToSpeech
    private lateinit var btnSpeak: Button
    private lateinit var instructionContainer: LinearLayout

    private val SPEAK_TEXT = "▶️ Speak Instructions"
    private val STOP_TEXT = "⏹️ Stop"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firstaid)

        // Toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { }

        // ✅ Initialize views FIRST
        btnSpeak = findViewById(R.id.btn_speak_instructions)
        instructionContainer = findViewById(R.id.instruction_container)

        btnSpeak.isEnabled = false
        btnSpeak.text = SPEAK_TEXT

        // ✅ Initialize TTS AFTER views
        tts = TextToSpeech(this, this)
        setupTtsListener()

        btnSpeak.setOnClickListener {
            if (tts.isSpeaking) {
                tts.stop()
            } else {
                val instructions = extractInstructionsFromLayout()
                speakOut(instructions)
            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (tts.isSpeaking) tts.stop()
                finish()
            }
        })
    }

    // ✅ Proper TTS listener
    private fun setupTtsListener() {
        tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {

            override fun onStart(utteranceId: String?) {
                runOnUiThread {
                    btnSpeak.text = STOP_TEXT
                }
            }

            override fun onDone(utteranceId: String?) {
                runOnUiThread {
                    btnSpeak.text = SPEAK_TEXT
                }
            }

            override fun onError(utteranceId: String?) {
                runOnUiThread {
                    btnSpeak.text = SPEAK_TEXT
                    Log.e("TTS", "Speech error")
                }
            }

            @Deprecated("Deprecated in Java")
            override fun onStop(utteranceId: String?, interrupted: Boolean) {
                runOnUiThread {
                    btnSpeak.text = SPEAK_TEXT
                }
            }
        })
    }

    private fun extractInstructionsFromLayout(): String {
        val builder = StringBuilder()

        for (i in 0 until instructionContainer.childCount) {
            val view: View = instructionContainer.getChildAt(i)

            if (view is TextView) {
                val text = view.text.toString().trim()
                if (i > 0 && text.isNotEmpty()
                    && !text.contains("First Aid")
                    && !text.contains("Quick First Aid")
                ) {
                    builder.append(text).append(". ")
                }
            }
        }
        return builder.toString()
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts.setLanguage(Locale.US)

            if (result == TextToSpeech.LANG_MISSING_DATA ||
                result == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                btnSpeak.text = "Language Error"
                Log.e("TTS", "Language not supported")
            } else {
                runOnUiThread {
                    btnSpeak.isEnabled = true
                    btnSpeak.text = SPEAK_TEXT
                }
            }
        } else {
            btnSpeak.text = "TTS Failed"
            Log.e("TTS", "Initialization failed")
        }
    }

    private fun speakOut(text: String) {
        if (text.isBlank()) {
            Log.e("TTS", "No text to speak")
            return
        }
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, "woundInstructions")
    }

    override fun onDestroy() {
        if (::tts.isInitialized) {
            tts.stop()
            tts.shutdown()
        }
        super.onDestroy()
    }
}
