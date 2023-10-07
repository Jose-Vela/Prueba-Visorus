package com.example.pruebavisorus

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebavisorus.databinding.ItemCategoryBinding

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCategoryBinding.bind(view)
    fun render(categoryDataResponse: CategoryDataResponse, onItemSelect: (Int) -> Unit) {
        binding.tvCategoryName.text = categoryDataResponse.nombre    // Asignamos el nombre correspondiente al textView de cada item
        binding.root.setOnClickListener { onItemSelect(layoutPosition) }
    }
}