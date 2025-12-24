package com.example.madproject

import android.os.Bundle
import android.widget.EditText // REQUIRED: Import for finding the search input
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

class CampingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.checklist_camping)

        // --- 1. TOOLBAR SETUP FIX ---
        // Find the search input EditText from the included toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        // Pass the required EditText and the lambda callback to the helper
        // We use an empty lambda {} since this activity doesn't need a suggestion list.
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // This callback is intentionally left empty in simple utility activities.
        }
        // --- END TOOLBAR FIX ---

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
    }
}