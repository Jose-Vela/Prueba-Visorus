package com.example.pruebavisorus

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ArticlesAdapter(
    var articlesList: List<ArticleDataResponse> = emptyList(),
    private val onItemSelect: (Int) -> Unit): RecyclerView.Adapter<ArticlesViewHolder>() {

    fun updateList(articleList: List<ArticleDataResponse>) {
        this.articlesList = articleList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticlesViewHolder {
        return ArticlesViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
        )
    }

    override fun getItemCount() = articlesList.size

    override fun onBindViewHolder(viewholder: ArticlesViewHolder, position: Int) {
        viewholder.render(articlesList[position], onItemSelect)
    }

}