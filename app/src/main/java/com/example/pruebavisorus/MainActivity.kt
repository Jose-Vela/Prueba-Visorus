package com.example.pruebavisorus

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.pruebavisorus.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.create

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var retrofitInstance: Retrofit
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        retrofitInstance = RetrofitInstance.getRetrofitInstance()

        CoroutineScope(Dispatchers.IO).launch {
            val myResponse: Response<ArticuloEntity> =
                retrofitInstance.create(ApiService::class.java).getArticulos()

            if (myResponse.isSuccessful) {
                val response : ArticuloEntity = myResponse.body()!!
                Log.d("DEBUG", response.toString())
                if (response.total != 0) {
                    runOnUiThread { showResults(response.data, false) }
                } else {
                    runOnUiThread { showResults(emptyList(), true) }
                }
            } else {
                runOnUiThread { showResults(emptyList(), true) }
            }
        }
    }

    private fun showResults(result: List<ArticuloDataResponse>, emptyResults: Boolean) {
        val textOut = if (!emptyResults)
            result.toString()
        else
            "No hay articulos"

        binding.tv.text = textOut
    }
}