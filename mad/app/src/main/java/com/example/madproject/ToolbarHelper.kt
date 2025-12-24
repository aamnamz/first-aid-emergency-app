package com.example.madproject

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.util.Locale
import kotlin.reflect.KClass

object ToolbarHelper {

    // --- 1. MAPPING LOGIC (Updated for better search and corrected Bleeding map) ---
    private val topicActivityMap: Map<String, KClass<out AppCompatActivity>> = mapOf(
        // First Aid Guides
        "burn" to BurnActivity::class,
        "scald" to BurnActivity::class,
        "choking" to ChokingActivity::class,
        "cpr" to CprActivity::class,
        "heart" to CprActivity::class,
        "fracture" to FractureActivity::class,
        "broken" to FractureActivity::class,
        "bleed" to WoundActivity::class,
        "laceration" to WoundActivity::class,
        "heat" to HeatStrokeActivity::class,
        "stroke" to HeatStrokeActivity::class,
        "wound" to WoundActivity::class,
        "cut" to WoundActivity::class,
        "scrape" to WoundActivity::class,
        "allergy" to AsthmaActivity::class,
        "asthma" to AsthmaActivity::class,
        "faint" to FaintingActivity::class,
        "dizzy" to FaintingActivity::class,

        // Checklist/Utility Activities
        "my checklist" to MChecklistActivity::class,
        "personal" to MChecklistActivity::class,
        "essential" to ChecklistsActivity::class,
        "guide" to ChecklistsActivity::class,
        "contacts" to EmergencyContactsActivity::class,
        "emergency phone" to EmergencyContactsActivity::class,

        // Specific Checklist Activities
        "home firstaid" to HomefirstaidActivity::class,
        "firstaid kit" to HomefirstaidActivity::class,
        "picnic" to PicnicActivity::class,
        "outdoor" to OutdoorActivity::class,
        "sports" to OutdoorActivity::class,
        "roadtrip" to RoadtripActivity::class,
        "car trip" to RoadtripActivity::class,
        "swimming" to SwimmingActivity::class,
        "pool" to SwimmingActivity::class,
        "travel" to TravelActivity::class,
        "trip" to TravelActivity::class,
        "hiking" to HikingActivity::class,
        "camping" to CampingActivity::class,
        "camp" to CampingActivity::class
    )

    // Helper function to handle the direct navigation logic
    private fun executeSearchNavigation(activity: AppCompatActivity, query: String) {
        val normalizedQuery = query.trim().lowercase(Locale.getDefault())
        var targetActivity: KClass<out AppCompatActivity>? = null

        // Loop through the keywords in the map
        for ((keyword, activityClass) in topicActivityMap) {
            // Use 'contains' for flexible searching of multi-word phrases (like "my checklist")
            if (normalizedQuery.contains(keyword)) {
                targetActivity = activityClass
                break
            }
        }

        if (targetActivity != null) {
            activity.startActivity(Intent(activity, targetActivity.java))
        } else {
            Toast.makeText(activity, "No matching guide or checklist found for \"$query\"", Toast.LENGTH_SHORT).show()
        }
    }

    fun setupSearchToolbar(
        activity: AppCompatActivity,
        searchInput: EditText?,
        onSearchModeToggled: (isSearchActive: Boolean) -> Unit
    ) {

        // Find the elements required for the search toggle and title swap
        val btnBack = activity.findViewById<ImageButton>(R.id.btnMenu) // Renamed internally for clarity
        val btnSearch = activity.findViewById<ImageButton>(R.id.btnSearch)
        val toolbarTitle = activity.findViewById<TextView>(R.id.toolbarTitle)

        // 1. Back Button Setup (Navigates to the previous screen)
        btnBack?.setOnClickListener {
            // FIX: Use finish() or onBackPressed() to go back to the previous activity in the stack.
            activity.finish()
        }

        // Initial state setup (Title visible, Search Input hidden)
        toolbarTitle?.visibility = View.VISIBLE
        searchInput?.visibility = View.GONE
        // Use activity.title if you want the app to handle setting the correct title based on the activity
        // If not, leave it to the calling activity to set the title:
        // toolbarTitle?.text = activity.title

        // 2. Search Button Click Listener (Toggles UI State & Calls Callback)
        btnSearch?.setOnClickListener {
            val isActive = toolbarTitle?.visibility == View.VISIBLE

            if (isActive) {
                // State: Title -> Search Input (SHOW Input)
                toolbarTitle?.visibility = View.GONE
                searchInput?.visibility = View.VISIBLE
                searchInput?.requestFocus()

                onSearchModeToggled(true) // Notify Activity to SHOW suggestions

                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.showSoftInput(searchInput, InputMethodManager.SHOW_IMPLICIT)

            } else {
                // State: Search Input -> Title (CLOSE Search)
                searchInput?.text?.clear()
                searchInput?.visibility = View.GONE
                toolbarTitle?.visibility = View.VISIBLE

                onSearchModeToggled(false) // Notify Activity to HIDE suggestions

                val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                imm?.hideSoftInputFromWindow(searchInput?.windowToken, 0)
            }
        }

        // 3. CRITICAL FIX: Handle Search Submission (Direct Navigation)
        searchInput?.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = v.text.toString()
                if (query.isNotEmpty()) {
                    // Call direct navigation logic
                    executeSearchNavigation(activity, query)

                    // Reset UI after search
                    searchInput.text.clear()
                    searchInput.visibility = View.GONE
                    toolbarTitle?.visibility = View.VISIBLE

                    onSearchModeToggled(false) // Hide suggestions list

                    val imm = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(searchInput.windowToken, 0)
                }
                true
            } else {
                false
            }
        }
    }
}