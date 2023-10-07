package com.example.pruebavisorus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pruebavisorus.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofitInstance: Retrofit

    private lateinit var categoriesAdapter : CategoriesAdapter
    private lateinit var articlesAdapter : ArticlesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofitInstance = RetrofitInstance.getRetrofitInstance("http://visorus.ddnsking.com:8091/")

        initUI()
    }

    private fun initUI() {

        initCategories()
        initArticles()

    }

    private fun initArticles() {
        CoroutineScope(Dispatchers.IO).launch {
            val myResponse: Response<ArticuleEntity> =
                retrofitInstance.create(ApiService::class.java).getArticles()
            if (myResponse.isSuccessful) {
                val response : ArticuleEntity = myResponse.body()!!
                if (response.total != 0) {
                    runOnUiThread { showArticlesResults(response.data, false) }
                } else {
                    runOnUiThread { showArticlesResults(emptyList(), true) }
                }
            } else {
                runOnUiThread { showArticlesResults(emptyList(), true) }
            }
        }

        articlesAdapter = ArticlesAdapter() { position -> onItemSelected(position) }
        binding.rvArticles.setHasFixedSize(true)
        binding.rvArticles.layoutManager = LinearLayoutManager(this)
        binding.rvArticles.adapter = articlesAdapter

    }

    private fun initCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            val myResponse: Response<CategoryEntity> =
                retrofitInstance.create(ApiService::class.java).getCategories()
            if (myResponse.isSuccessful) {
                val response : CategoryEntity = myResponse.body()!!
                if (response.total != 0) {
                    runOnUiThread { showCategoriesResults(response.data, false) }
                } else {
                    runOnUiThread { showCategoriesResults(emptyList(), true) }
                }
            } else {
                runOnUiThread { showCategoriesResults(emptyList(), true) }
            }
        }

        categoriesAdapter = CategoriesAdapter { position -> onItemSelected(position) }
        binding.rvCategories.setHasFixedSize(true)
        binding.rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun onItemSelected(position: Int) {

    }

    private fun showCategoriesResults(result: List<CategoryDataResponse>, emptyResults: Boolean) {
        categoriesAdapter.updateList(result)
    }

    private fun showArticlesResults(result: List<ArticleDataResponse>, b: Boolean) {
        articlesAdapter.updateList(result)
    }
}