package com.example.pruebavisorus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter(
    var categoriesList: List<CategoryDataResponse> = emptyList(),
    private val onItemSelect: (Int) -> Unit): RecyclerView.Adapter<CategoriesViewHolder>() {

    fun updateList(categoryList: List<CategoryDataResponse>) {
        this.categoriesList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )
    }

    override fun getItemCount() = categoriesList.size

    override fun onBindViewHolder(viewholder: CategoriesViewHolder, position: Int) {
        viewholder.render(categoriesList[position], onItemSelect)
    }

}