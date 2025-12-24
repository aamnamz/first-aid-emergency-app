package com.example.madproject

import android.Manifest
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Quick Action Cards
    private lateinit var cardMyChecklist: LinearLayout
    private lateinit var cardFirstAid: LinearLayout
    private lateinit var cardEmergencyContacts: LinearLayout

    // Category Cards
    private lateinit var cardBurn: LinearLayout
    private lateinit var cardChoking: LinearLayout
    private lateinit var cardCPR: LinearLayout
    private lateinit var btnViewMore: TextView

    private lateinit var fabEmergency: ExtendedFloatingActionButton

    // Search elements
    private lateinit var searchBar: EditText
    private lateinit var imgVoiceInput: ImageView
    private lateinit var imgSearchIcon: ImageView

    // --- 1. PERMISSION LAUNCHER ---
    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            startSpeech() // Permission granted, start speech input
        } else {
            Toast.makeText(this, "Microphone permission is required for voice search.", Toast.LENGTH_LONG).show()
        }
    }

    // --- 2. SPEECH RESULT LAUNCHER ---
    private val speechResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText =
                result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.get(0)

            // Pass the spoken text to the handler function
            handleVoiceCommand(spokenText)
        } else if (result.resultCode == Activity.RESULT_CANCELED) {
            Toast.makeText(this, "Voice input cancelled.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        // Initialize all views
        fabEmergency = findViewById(R.id.fabEmergency)
        cardMyChecklist = findViewById(R.id.cardChecklist)
        cardFirstAid = findViewById(R.id.cardFirstAid)
        cardEmergencyContacts = findViewById(R.id.cardHospital)
        cardBurn = findViewById(R.id.cardBurn)
        cardChoking = findViewById(R.id.cardChoking)
        cardCPR = findViewById(R.id.cardCPR)
        btnViewMore = findViewById(R.id.btnViewMore)

        // Search Initialization
        searchBar = findViewById(R.id.searchBar)
        // Note: Using img_voice_input and img_search_icon as fixed IDs per final XML structure
        imgVoiceInput = findViewById(R.id.img_voice_input)
        imgSearchIcon = findViewById(R.id.img_search_icon)

        // --- View Click Listeners ---
        fabEmergency.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:1122")
            startActivity(intent)
        }
        cardMyChecklist.setOnClickListener { startActivity(Intent(this, MChecklistActivity::class.java)) }
        cardFirstAid.setOnClickListener { startActivity(Intent(this, ChecklistsActivity::class.java)) }
        cardEmergencyContacts.setOnClickListener { startActivity(Intent(this, EmergencyContactsActivity::class.java)) }
        cardBurn.setOnClickListener { startActivity(Intent(this, BurnActivity::class.java)) }
        cardChoking.setOnClickListener { startActivity(Intent(this, ChokingActivity::class.java)) }
        cardCPR.setOnClickListener { startActivity(Intent(this, CprActivity::class.java)) }
        btnViewMore.setOnClickListener { startActivity(Intent(this, CategoriesActivity::class.java)) }

        // 1. Voice icon click listener (Starts the permission/speech flow)
        imgVoiceInput.setOnClickListener {
            checkPermissionAndStart()
        }

        // 2. Search icon click listener (Manual search button)
        imgSearchIcon.setOnClickListener {
            handleTextCommand(searchBar.text.toString())
        }

        // 3. Search bar enter listener (Keyboard search logic)
        searchBar.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                handleTextCommand(searchBar.text.toString())
                true
            } else false
        }
    }

    // --- 3. PERMISSION CHECK FUNCTION ---
    private fun checkPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            startSpeech()
        } else {
            // Request permission using the launcher
            requestPermission.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    // --- 4. SPEECH START FUNCTION (Offline Priority) ---
    private fun startSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            // CRITICAL FOR OFFLINE USE
            //putExtra(
              //  RecognizerIntent.EXTRA_PREFER_OFFLINE,
                //true
            //)
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            // Use current system locale for best results
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak your first aid topic")
        }

        try {
            speechResult.launch(intent)
        } catch (e: ActivityNotFoundException) {
            // This catches phones that don't have Google Speech services (rare)
            Toast.makeText(this, "Voice search is not supported on this device.", Toast.LENGTH_LONG).show()
        }
    }

    // --- 5. VOICE COMMAND HANDLER (Expanded with ALL Activities) ---
    private fun handleVoiceCommand(command: String?) {
        if (command == null) return

        val voice = command.lowercase(Locale.getDefault())

        // Display the command in the search bar
        searchBar.setText(command)

        when {
            // ===================================
            // FIRST AID GUIDES
            // ===================================

            // BURNS: Includes 'scald', 'fire', 'hot', 'sunburn'
            "burn" in voice || "scald" in voice || "fire" in voice || "hot" in voice || "sunburn" in voice || "jal" in voice -> {
                startActivity(Intent(this, BurnActivity::class.java))
            }

            // CHOKING: FIX: Prioritize 'choking' and exclude 'joke' from 'choke'
            "choking" in voice || "choke" in voice || "gagging" in voice || "strangle" in voice || "suffocate" in voice || "joke" in voice || "joking" in voice || "Dam Ghut" in voice -> {
                startActivity(Intent(this, ChokingActivity::class.java)) // Line 289: Improved logic to always guide to Choking if voice command is related to choke/joke misrecognition
            }

            // CPR: Includes 'resuscitation', 'heart attack', 'compression'
            "cpr" in voice || "resuscitation" in voice || "heart attack" in voice || "collapse" in voice || "compression" in voice -> {
                startActivity(Intent(this, CprActivity::class.java))
            }

            // FRACTURE: Includes 'broken', 'bone', 'sprain', 'twist'
            "fracture" in voice || "broken" in voice || "bone" in voice || "sprain" in voice || "twist" in voice || "tut" in voice-> {
                startActivity(Intent(this, FractureActivity::class.java))
            }

            // BLEEDING: Includes 'cut', 'laceration', 'severe'
            "bleed" in voice || "laceration" in voice || "severe cut" in voice || "blood loss" in voice || "khoon" in voice || "phone" in voice -> {
                startActivity(Intent(this, WoundActivity::class.java))
            }
            // MINOR WOUNDS: Includes 'scrape', 'abrasion', 'graze', 'cut'
            "wound" in voice || "cut" in voice || "scrape" in voice || "abrasion" in voice || "graze" in voice|| "chot" in voice -> {
                startActivity(Intent(this, WoundActivity::class.java))
            }

            // HEAT STROKE: Includes 'sunstroke', 'fever', 'overheat'
            "heat" in voice || "stroke" in voice || "sunstroke" in voice || "fever" in voice || "overheat" in voice || "lu lagna" in voice -> {
                startActivity(Intent(this, HeatStrokeActivity::class.java))
            }

            "asthma" in voice || "asthma attack" in voice || "dama" in voice || "sans ki bimari" in voice || "Asthmatic" in voice || "Breathing problem" in voice || "Breathing difficulty" in voice || "breathe" in voice  -> {
                startActivity(Intent(this, AsthmaActivity::class.java)) // Line 312: CHANGED from AsthmaActivity
            }

            // ASTHMA: Specific activity for Asthma
            "asthma" in voice || "breathing difficulty" in voice || "inhaler" in voice -> {
                startActivity(Intent(this, AsthmaActivity::class.java))
            }
            // FAINTING: Includes 'dizzy', 'passed out'
            "faint" in voice || "dizzy" in voice || "unconscious" in voice || "passed out" in voice|| "behosh" in voice-> {
                startActivity(Intent(this, FaintingActivity::class.java))
            }

            // ===================================
            // CHECKLISTS / UTILITY
            // ===================================

            // HOME FIRST AID KIT CHECKLIST
            "home kit" in voice || "house first aid" in voice || "home supplies" in voice -> {
                startActivity(Intent(this, HomefirstaidActivity::class.java))
            }

            // HIKING CHECKLIST
            "hike" in voice || "trail" in voice || "mountain" in voice || "backpacking" in voice|| "pahad" in voice || "pahadon" in voice-> {
                startActivity(Intent(this, HikingActivity::class.java))
            }

            // OUTDOOR CHECKLIST
            "outdoor" in voice || "outside" in voice || "nature" in voice || "woods" in voice || "survival gear" in voice || "khelne" in voice-> {
                startActivity(Intent(this, OutdoorActivity::class.java))
            }

            // ROAD TRIP CHECKLIST
            "road trip" in voice || "car" in voice || "driving" in voice || "vehicle" in voice || "safar" in voice-> {
                startActivity(Intent(this, RoadtripActivity::class.java))
            }

            // SWIMMING/WATER SAFETY CHECKLIST
            "swim" in voice || "pool" in voice || "water safety" in voice || "drowning" in voice || "beach" in voice|| "tairne" in voice -> {
                startActivity(Intent(this, SwimmingActivity::class.java))
            }

            // TRAVEL CHECKLIST
            "travel" in voice || "trip" in voice || "vacation" in voice || "flight" in voice || "luggage" in voice || "ghumne" in voice-> {
                startActivity(Intent(this, TravelActivity::class.java))
            }

            // PICNIC CHECKLIST
            "picnic" in voice || "park lunch" in voice || "food basket" in voice -> {
                startActivity(Intent(this, PicnicActivity::class.java))
            }

            // MY CHECKLIST (The user's customizable list) - Specific check first
            "my checklist" in voice || "my list" in voice || "my supplies" in voice || "personal kit" in voice -> {
                startActivity(Intent(this, MChecklistActivity::class.java))
            }

            // GENERAL CHECKLISTS (View all lists/topics)
            "checklist" in voice || "all lists" in voice || "guides" in voice || "topics" in voice || "view more" in voice || "all guides" in voice || "meri" in voice-> {
                startActivity(Intent(this, ChecklistsActivity::class.java))
            }

            // EMERGENCY CONTACTS
            "contacts" in voice || "emergency phone" in voice || "call" in voice || "hospital" in voice || "police" in voice || "ambulance" in voice|| "hungami" in voice|| "emergency" in voice -> {
                startActivity(Intent(this, EmergencyContactsActivity::class.java))
            }

            else -> {
                Toast.makeText(this, "No matching guide found for \"$command\"", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // --- 6. TEXT COMMAND HANDLER (Replicated logic for text input) ---
    private fun handleTextCommand(query: String) {
        val textQuery = query.trim().lowercase(Locale.getDefault())

        when {
            // FIRST AID
            textQuery.contains("burn") || textQuery.contains("scald") || textQuery.contains("fire") || textQuery.contains("sunburn") -> startActivity(Intent(this, BurnActivity::class.java))
            textQuery.contains("choking") -> startActivity(Intent(this, ChokingActivity::class.java))
            textQuery.contains("choking") || textQuery.contains("choke") || textQuery.contains("joke") || textQuery.contains("joking") -> {
                // If any of these are present, we assume the user wanted the Choking guide
                startActivity(Intent(this, ChokingActivity::class.java))
            }textQuery.contains("cpr") || textQuery.contains("resuscitation") || textQuery.contains("heart attack") -> startActivity(Intent(this, CprActivity::class.java))
            textQuery.contains("fracture") || textQuery.contains("broken") || textQuery.contains("sprain") -> startActivity(Intent(this, FractureActivity::class.java))
            textQuery.contains("bleed") || textQuery.contains("laceration") || textQuery.contains("severe cut") -> startActivity(Intent(this, WoundActivity::class.java)) // Line 387: CHANGED from WoundActivity
            textQuery.contains("heat") || textQuery.contains("stroke") || textQuery.contains("sunstroke") -> startActivity(Intent(this, HeatStrokeActivity::class.java))
            textQuery.contains("allergy") || textQuery.contains("anaphylaxis") || textQuery.contains("epipen") -> startActivity(Intent(this, AsthmaActivity::class.java)) // Line 393: CHANGED from AsthmaActivity
            textQuery.contains("asthma") || textQuery.contains("inhaler") -> startActivity(Intent(this, AsthmaActivity::class.java))
            textQuery.contains("faint") || textQuery.contains("dizzy") || textQuery.contains("unconscious") -> startActivity(Intent(this, FaintingActivity::class.java))
            textQuery.contains("wound") || textQuery.contains("cut") || textQuery.contains("scrape") -> startActivity(Intent(this, WoundActivity::class.java))

            // CHECKLISTS / UTILITY
            textQuery.contains("home kit") || textQuery.contains("house first aid") -> startActivity(Intent(this, HomefirstaidActivity::class.java))
            textQuery.contains("hike") || textQuery.contains("trail") || textQuery.contains("trek") || textQuery.contains("backpacking") || textQuery.contains("mountain") || textQuery.contains("climb") || textQuery.contains("hiking") || textQuery.contains("trekking") || textQuery.contains("climbing") -> startActivity(Intent(this, HikingActivity::class.java))
            textQuery.contains("outdoor") || textQuery.contains("survival") || textQuery.contains("camp") || textQuery.contains("tent") || textQuery.contains("bushcraft") || textQuery.contains("wild") || textQuery.contains("camping") || textQuery.contains("surviving") -> startActivity(Intent(this, OutdoorActivity::class.java))
            textQuery.contains("road trip") || textQuery.contains("car") || textQuery.contains("drive") || textQuery.contains("vehicle") || textQuery.contains("auto") || textQuery.contains("motor") || textQuery.contains("driving") -> startActivity(Intent(this, RoadtripActivity::class.java))
            textQuery.contains("swim") || textQuery.contains("water safety") || textQuery.contains("pool") || textQuery.contains("beach") || textQuery.contains("ocean") || textQuery.contains("river") || textQuery.contains("sea") || textQuery.contains("swimming") || textQuery.contains("diving") -> startActivity(Intent(this, SwimmingActivity::class.java))
            textQuery.contains("travel") || textQuery.contains("vacation") || textQuery.contains("trip") || textQuery.contains("holiday") || textQuery.contains("airport") || textQuery.contains("flight") || textQuery.contains("journey") || textQuery.contains("traveling") -> startActivity(Intent(this, TravelActivity::class.java))
            textQuery.contains("picnic") || textQuery.contains("park lunch") || textQuery.contains("basket") || textQuery.contains("food") || textQuery.contains("meal") || textQuery.contains("outside eat") -> startActivity(Intent(this, PicnicActivity::class.java))

            textQuery.contains("my checklist") || textQuery.contains("my list") -> startActivity(Intent(this, MChecklistActivity::class.java))
            textQuery.contains("checklist") || textQuery.contains("all guides") || textQuery.contains("topics")|| textQuery.contains("essential") -> startActivity(Intent(this, ChecklistsActivity::class.java))
            textQuery.contains("contact") || textQuery.contains("emergency phone") || textQuery.contains("ambulance") -> startActivity(Intent(this, EmergencyContactsActivity::class.java))

            else -> Toast.makeText(this, "No guide found for \"$query\"", Toast.LENGTH_SHORT).show()
        }
    }
}