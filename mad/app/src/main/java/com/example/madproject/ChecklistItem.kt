package com.example.madproject

data class ChecklistItem(
    var text: String,      // The task text
    var isDone: Boolean = false  // Tick/untick state
)
