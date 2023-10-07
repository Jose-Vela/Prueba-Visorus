package com.example.pruebavisorus

import android.view.View
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class AddButtonViewHolder(itemView: View, onClick: () -> Unit) : RecyclerView.ViewHolder(itemView) {
    private val addButton: CardView = itemView.findViewById(R.id.viewContainer)

    init {
        addButton.setOnClickListener {
            onClick()
        }
    }
}
