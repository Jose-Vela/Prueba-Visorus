package com.example.pruebavisorus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
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

    private var categoriesMutableList: MutableList<CategoryDataResponse> = mutableListOf(
        CategoryDataResponse("A01",1569364107680, "SMARTPHONES"),
        CategoryDataResponse("A02",1569364107680, "COMPUTADORAS"),
        CategoryDataResponse("A03",1569364107680, "TABLETAS"),
        CategoryDataResponse("A04",1569364107680, "MONITORES"),
    )
    companion object {
        val ERROR_CONECTION = 0
        val RESPONSE_NO_SUCCESSFUL = 1
        val EMPTY_RESPONSE = 2
    }

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
                        //runOnUiThread { showArticlesResults(response.data) }
                        runOnUiThread { showResults(response.data) }
                    } else {
                        //runOnUiThread { showArticlesResults(emptyList()) }
                        runOnUiThread { showResults(EMPTY_RESPONSE) }
                    }
                } else {
                    //runOnUiThread { showArticlesResults(emptyList()) }
                    runOnUiThread { showResults(RESPONSE_NO_SUCCESSFUL) }
                }
            } catch (ste: SocketTimeoutException){
                Log.d("ERROR_CONECTION","Falló al conectarse con el servidor: ${ste.toString()}")
                //runOnUiThread { showErrorConection() }
                runOnUiThread { showResults(ERROR_CONECTION) }
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
                        //runOnUiThread { showCategoriesResults(response.data) }
                        runOnUiThread { showResults(response.data) }
                    } else {
                        //runOnUiThread { showCategoriesResults(emptyList()) }
                        runOnUiThread { showResults(EMPTY_RESPONSE) }
                    }
                } else {
                    //runOnUiThread { showCategoriesResults(emptyList()) }
                    runOnUiThread { showResults(RESPONSE_NO_SUCCESSFUL) }
                }
            } catch (ste: SocketTimeoutException){
                Log.d("ERROR_CONECTION","Falló al conectarse con el servidor: ${ste.toString()}")
                //runOnUiThread { showErrorConection() }
                runOnUiThread { showResults(ERROR_CONECTION) }
            }
        }

        categoriesAdapter = CategoriesAdapter(
            categoriesMutableList,
            { position -> onItemSelected(position) },
            { onAddCategoryClick() },
            { onShowCategoryClick() }
        )
        binding.rvCategories.setHasFixedSize(true)
        binding.rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rvCategories.adapter = categoriesAdapter
    }

    private fun onShowCategoryClick() {

    }

    private fun onAddCategoryClick() {

    }

    private fun onItemSelected(position: Int) {

    }

    /*private fun showCategoriesResults(result: List<CategoryDataResponse>) {
        categoriesAdapter.updateList(result)
    }*/

    /*private fun showArticlesResults(result: List<ArticleDataResponse>) {
        articlesAdapter.updateList(result)
    }*/

    private fun <T> showResults(result: T) {
        var textToast: String = ""
        when(result){
            is CategoryDataResponse -> { categoriesAdapter.updateList(listOf(result)) }

            is ArticleDataResponse -> { articlesAdapter.updateList(listOf(result))}

            is Int -> when {
                result == ERROR_CONECTION -> {  textToast = "ERROR AL CONECTARSE CON EL SERVIDOR" }

                result == RESPONSE_NO_SUCCESSFUL -> { textToast = "RESPUESTA FALLIDA DEL SERVIDOR" }

                result == EMPTY_RESPONSE -> { textToast = "RESPUESTA CON DATOS VACIOS" }
            }
        }

        if(result is Int) {
            Log.d("RESULT IS INT", textToast)
            Toast.makeText(applicationContext, textToast, Toast.LENGTH_LONG).show()
        }
    }

    /*private fun showErrorConection() {
        val toast = Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG)
        toast.show()
    }*/
}