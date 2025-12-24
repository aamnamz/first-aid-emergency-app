package com.example.madproject

import android.os.Bundle
import android.widget.EditText // REQUIRED: Import for finding the search input
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class HikingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checklist_hikking)

        // --- 1. TOOLBAR SETUP FIX ---
        // Find the search input EditText from the included toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        // FIX: Pass the required EditText and the empty lambda callback to the helper
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // Checklist activities leave this empty, as they don't have a suggestion list
        }
        // --- END TOOLBAR FIX ---

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
}