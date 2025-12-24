package com.example.madproject

import android.content.Intent
import android.os.Bundle
import android.widget.EditText // REQUIRED: Import for finding the search input
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class ChecklistsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)

        // --- 1. TOOLBAR SETUP FIX ---
        // Find the search input EditText from the included toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        // FIX: Pass the required EditText and the empty lambda callback to the helper
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // Index activities leave this empty, as they typically don't have a suggestions list
        }
        // --- END TOOLBAR FIX ---

        // Find each checklist LinearLayout
        val homeChecklist = findViewById<LinearLayout>(R.id.homeChecklist)
        val hikingChecklist = findViewById<LinearLayout>(R.id.hikingChecklist)
        val campingChecklist = findViewById<LinearLayout>(R.id.campingChecklist)
        val roadTripChecklist = findViewById<LinearLayout>(R.id.roadTripChecklist)
        val picnicChecklist = findViewById<LinearLayout>(R.id.picnicChecklist)
        val swimmingChecklist = findViewById<LinearLayout>(R.id.swimmingChecklist)
        val sportsChecklist = findViewById<LinearLayout>(R.id.sportsChecklist)
        val travelChecklist = findViewById<LinearLayout>(R.id.travelChecklist)

        // Set click listeners for each checklist
        homeChecklist.setOnClickListener {
            startActivity(Intent(this, HomefirstaidActivity::class.java))
        }

        hikingChecklist.setOnClickListener {
            startActivity(Intent(this, HikingActivity::class.java))
        }

        campingChecklist.setOnClickListener {
            startActivity(Intent(this, CampingActivity::class.java))
        }

        roadTripChecklist.setOnClickListener {
            startActivity(Intent(this, RoadtripActivity::class.java))
        }

        picnicChecklist.setOnClickListener {
            startActivity(Intent(this, PicnicActivity::class.java))
        }

        swimmingChecklist.setOnClickListener {
            startActivity(Intent(this, SwimmingActivity::class.java))
        }

        sportsChecklist.setOnClickListener {
            startActivity(Intent(this, OutdoorActivity::class.java))
        }

        travelChecklist.setOnClickListener {
            startActivity(Intent(this, TravelActivity::class.java))
        }
    }
}