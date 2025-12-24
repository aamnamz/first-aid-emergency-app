package com.example.madproject

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper // NEW IMPORT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MChecklistActivity : AppCompatActivity() {

    private lateinit var adapter: ChecklistAdapter
    private val items = mutableListOf<ChecklistItem>()

    // Constants for SharedPreferences storage
    private val PREFS_FILE_NAME = "MChecklistPrefs"
    private val ITEMS_KEY = "checklist_items_data"

    // ------------------- Persistence Logic -------------------

    /**
     * Saves the current list of ChecklistItem objects to SharedPreferences
     */
    private fun saveItems() {
        val jsonString = Gson().toJson(items)
        getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(ITEMS_KEY, jsonString)
            .apply()
    }

    /**
     * Loads the stored JSON string from SharedPreferences
     */
    private fun loadItems() {
        val sharedPrefs = getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)
        val jsonString = sharedPrefs.getString(ITEMS_KEY, null)

        if (jsonString != null) {
            val itemType = object : TypeToken<MutableList<ChecklistItem>>() {}.type
            val loadedList: MutableList<ChecklistItem> = Gson().fromJson(jsonString, itemType)

            items.clear()
            items.addAll(loadedList)
        }
    }

    // ------------------- Activity Lifecycle -------------------

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. LOAD DATA: Load existing data before setting up the UI
        loadItems()

        setContentView(R.layout.activity_mychecklist)

        val rvChecklist = findViewById<RecyclerView>(R.id.rvChecklist)
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)

        // --- TOOLBAR SETUP FIX ---
        // Find the search input EditText from the included toolbar
        val toolbarSearchInput = findViewById<EditText>(R.id.toolbar_search_input)

        // FIX: Pass the required EditText and the empty lambda callback to the helper
        ToolbarHelper.setupSearchToolbar(this, toolbarSearchInput) { isSearchActive ->
            // My Checklist Activity leaves this empty.
        }
        // --- END TOOLBAR FIX ---


        // 2. ADAPTER SETUP: Pass the 'saveItems' function as the data change callback
        adapter = ChecklistAdapter(items) {
            saveItems() // This is called whenever an item is ticked/unticked or removed
        }
        rvChecklist.layoutManager = LinearLayoutManager(this)
        rvChecklist.adapter = adapter

        // --- ItemTouchHelper for Swipe-to-Remove ---
        setupSwipeToDelete(rvChecklist)
        // --------------------------------------------------------

        // Add new item
        btnAddItem.setOnClickListener {
            val input = EditText(this)
            input.hint = "Enter new item"

            AlertDialog.Builder(this)
                .setTitle("Add Checklist Item")
                .setView(input)
                .setPositiveButton("Add") { _, _ ->
                    val text = input.text.toString().trim()
                    if (text.isNotEmpty()) {
                        adapter.addItem(text)
                        // 3. SAVE DATA: Save the list immediately after a new item is added
                        saveItems()
                    }
                }
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    /**
     * Helper function to attach swipe functionality to the RecyclerView.
     */
    private fun setupSwipeToDelete(recyclerView: RecyclerView) {
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            0, // No drag directions
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT // Enable swiping left and right
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false // We don't support moving items
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                // Call the adapter's remove function, which handles persistence
                adapter.removeItem(position)
            }
        }

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)

    }

    /**
     * Optional but good practice: Save the data when the user leaves the activity
     */
    override fun onPause() {
        super.onPause()
        saveItems()
    }
}