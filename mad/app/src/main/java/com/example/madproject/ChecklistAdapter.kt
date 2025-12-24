package com.example.madproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// MODIFIED: Added onDataChanged lambda to the constructor
class ChecklistAdapter(
    private val items: MutableList<ChecklistItem>,
    private val onDataChanged: () -> Unit // Function provided by the Activity to trigger save
) : RecyclerView.Adapter<ChecklistAdapter.ChecklistViewHolder>() {

    inner class ChecklistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtItem: TextView = itemView.findViewById(R.id.txtItem)
        val imgTick: ImageView = itemView.findViewById(R.id.imgTick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChecklistViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checklist, parent, false)
        return ChecklistViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChecklistViewHolder, position: Int) {
        val item = items[position]
        holder.txtItem.text = item.text
        holder.imgTick.setImageResource(
            if (item.isDone) R.drawable.ic_tick_done else R.drawable.ic_tick_undone
        )

        // Toggle done/undone on tick click
        holder.imgTick.setOnClickListener {
            item.isDone = !item.isDone
            notifyItemChanged(position)

            // CRUCIAL: Call the callback function to signal data has changed and needs saving
            onDataChanged()
        }
    }

    override fun getItemCount(): Int = items.size

    // Add new item (called from Activity)
    fun addItem(text: String) {
        items.add(ChecklistItem(text))
        notifyItemInserted(items.size - 1)
        // Save is called in the Activity after the dialog closes
    }

    // NEW FUNCTION: Removes the item at a specific position (called by ItemTouchHelper)
    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)

        // CRUCIAL: Immediately trigger the save function after removal
        onDataChanged()
    }
}