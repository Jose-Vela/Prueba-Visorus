package com.example.pruebavisorus

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoriesAdapter(
    //var categoriesList: List<CategoryDataResponse> = emptyList(),
    var categoriesList: List<CategoryDataResponse>,
    private val onItemSelect: (Int) -> Unit,
    private val onAddCategoryClick: () -> Unit,
    private val onShowCategoryClick: () -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_SHOW_BUTTON = 1
        private const val VIEW_TYPE_CATEGORY = 2
        private const val VIEW_TYPE_ADD_BUTTON = 3
    }

    fun updateList(categoryList: List<CategoryDataResponse>) {
        this.categoriesList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        /*return CategoriesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false)
        )*/
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_CATEGORY) {
            CategoriesViewHolder(
                inflater.inflate(R.layout.item_category, parent, false)
            )
        } else if (viewType == VIEW_TYPE_SHOW_BUTTON){
            val showButtonView = inflater.inflate(R.layout.button_show_all_categories, parent, false)
            ButtonShowAllCategoriesViewHolder(showButtonView, onShowCategoryClick)
        } else {
            // Infla el diseño del botón de agregar
            val addButtonView = inflater.inflate(R.layout.button_add_category, parent, false)
            ButtonAddCategoryViewHolder(addButtonView, onAddCategoryClick)
        }
    }

    override fun getItemCount() = categoriesList.size + 2

    override fun onBindViewHolder(viewholder: RecyclerView.ViewHolder, position: Int) {
        //viewholder.render(categoriesList[position], onItemSelect)
        //if ((position > 0) && (position <= categoriesList.size)) {
        if ((position > 0) && (position <= categoriesList.size)) {
            // Vincula la vista de la categoría
            val category = categoriesList[position-1]
            (viewholder as CategoriesViewHolder).render(category, onItemSelect)
        } /*else {
            // Vincula la vista del botón de agregar
            // No es necesario realizar nada aquí, ya que la acción del botón se maneja en su propio ViewHolder
        }*/
    }

    /*override fun onBindViewHolder(viewholder: CategoriesViewHolder, position: Int) {
        //viewholder.render(categoriesList[position], onItemSelect)
        if (position < categoriesList.size) {
            // Vincula la vista de la categoría
            val category = categoriesList[position]
            (viewholder as CategoriesViewHolder).render(category, onItemSelect)
        } /*else {
            // Vincula la vista del botón de agregar
            // No es necesario realizar nada aquí, ya que la acción del botón se maneja en su propio ViewHolder
        }*/
    }*/

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            VIEW_TYPE_SHOW_BUTTON
        } else if (position + 1 < categoriesList.size + 2) {
            VIEW_TYPE_CATEGORY
        } else {
            VIEW_TYPE_ADD_BUTTON
        }
    }
}