package com.example.pruebavisorus

import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class ButtonAddCategoryViewHolder(itemView: View, onClick: () -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val addButton: CardView = itemView.findViewById(R.id.viewContainer)

    init {
        addButton.setOnClickListener {
            onClick().apply { Log.d("BOTÓN ADD", "Precionando botón Add") }
        }
    }
}
