package com.example.pruebavisorus

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebavisorus.databinding.ItemArticleBinding

class ArticlesViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemArticleBinding.bind(view)
    fun render(articleDataResponse: ArticleDataResponse, onItemSelect: (Int) -> Unit) {
        binding.tvArticleName.text = articleDataResponse.nombre    // Asignamos el nombre correspondiente al textView de cada item
        binding.root.setOnClickListener { onItemSelect(layoutPosition) }
    }
}