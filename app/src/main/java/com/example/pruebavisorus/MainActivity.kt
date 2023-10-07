package com.example.pruebavisorus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pruebavisorus.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import java.net.SocketTimeoutException

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
            try {
                val myResponse: Response<ArticuleEntity> =
                    retrofitInstance.create(ApiService::class.java).getArticles()
                if (myResponse.isSuccessful) {
                    val response : ArticuleEntity = myResponse.body()!!
                    if (response.total != 0) {
                        runOnUiThread { showArticlesResults(response.data) }
                    } else {
                        runOnUiThread { showArticlesResults(emptyList()) }
                    }
                } else {
                    runOnUiThread { showArticlesResults(emptyList()) }
                }
            } catch (ste: SocketTimeoutException){
                Log.d("ERROR_CONECTION","Falló al conectarse con el servidor: ${ste.toString()}")
                runOnUiThread { showErrorConection() }
            }
        }

        articlesAdapter = ArticlesAdapter() { position -> onItemSelected(position) }
        binding.rvArticles.setHasFixedSize(true)
        binding.rvArticles.layoutManager = LinearLayoutManager(this)
        binding.rvArticles.adapter = articlesAdapter
    }

    private fun initCategories() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val myResponse: Response<CategoryEntity> =
                    retrofitInstance.create(ApiService::class.java).getCategories()
                if (myResponse.isSuccessful) {
                    val response : CategoryEntity = myResponse.body()!!
                    if (response.total != 0) {
                        runOnUiThread { showCategoriesResults(response.data) }
                    } else {
                        runOnUiThread { showCategoriesResults(emptyList()) }
                    }
                } else {
                    runOnUiThread { showCategoriesResults(emptyList()) }
                }
            } catch (ste: SocketTimeoutException){
                Log.d("ERROR_CONECTION","Falló al conectarse con el servidor: ${ste.toString()}")
                runOnUiThread { showErrorConection() }
            }
        }

        categoriesAdapter = CategoriesAdapter (emptyList(), { position -> onItemSelected(position) }, { onAddCategoryClick() })
        binding.rvCategories.setHasFixedSize(true)
        binding.rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun onAddCategoryClick() {

    }

    private fun onItemSelected(position: Int) {

    }

    private fun showCategoriesResults(result: List<CategoryDataResponse>) {
        categoriesAdapter.updateList(result)
    }

    private fun showArticlesResults(result: List<ArticleDataResponse>) {
        articlesAdapter.updateList(result)
    }

    private fun showErrorConection() {
        val toast = Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG)
        toast.show()
    }
}