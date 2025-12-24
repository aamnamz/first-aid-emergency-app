package com.example.madproject

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.EditText // REQUIRED: Import for finding the search input
import androidx.appcompat.app.AppCompatActivity

class EmergencyContactsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contact)

        // --- 1. TOOLBAR SETUP FIX ---
        // Find the search input EditText from the included toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        // FIX: Pass the required EditText and the empty lambda callback to the helper
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // Utility activities leave this empty, as they typically don't have a suggestions list
        }
        // --- END TOOLBAR FIX ---

        // Map each row to its phone number
        val contactsMap= mapOf(
            R.id.ndmaRow to "0519205037",
            R.id.policeRow to "15",
            R.id.fireRow to "16",
            R.id.edhiRow to "115",
            R.id.chhipaRow to "1020",
            R.id.burnsRow to "02199215740",
            R.id.poisonRow to "02199215752"
        )

        // Set click listener for each contact
        for ((id, phone) in contactsMap) {
            // Finding the clickable LinearLayout by its ID
            findViewById<LinearLayout>(id).setOnClickListener {
                // Creates an intent to launch the dialer
                val intent = Intent(Intent.ACTION_DIAL)
                // Sets the phone number data using the "tel:" scheme
                intent.data = Uri.parse("tel:$phone")
                // Starts the dialer activity
                startActivity(intent)
            }
        }
    }
}