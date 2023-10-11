package com.example.pruebavisorus

import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebavisorus.databinding.ItemCategoryBinding

class CategoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemCategoryBinding.bind(view)
    fun render(
        category: CategoryProvider,
        categoriesListSize: Int,
        onItemSelect: (Int) -> Unit
    ) {
        if(layoutPosition > 0 && layoutPosition < categoriesListSize - 1){
            binding.tvCategoryName.text = category.category.nombre    // Asignamos el nombre correspondiente al textView de cada item
        }
        binding.root.setOnClickListener { onItemSelect(layoutPosition) }

        val cardColor: Int
        val cardTextColor: Int

        if(category.isSelected){
            cardColor = R.color.app_accent_color
            cardTextColor = R.color.white
        } else{
            cardColor = R.color.white
            cardTextColor = R.color.app_text_disabled_color
        }

        binding.viewContainer.setCardBackgroundColor(ContextCompat.getColor(binding.viewContainer.context, cardColor))
        binding.tvCategoryName.setTextColor(ContextCompat.getColor(binding.tvCategoryName.context, cardTextColor))

    }

}