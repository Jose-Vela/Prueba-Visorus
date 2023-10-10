package com.example.pruebavisorus

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
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
    private val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)

    /*private var categoriesMutableList: MutableList<CategoryDataResponse> = mutableListOf(
        CategoryDataResponse("A01",1569364107680, "SMARTPHONES"),
        CategoryDataResponse("A02",1569364107680, "COMPUTADORAS"),
        CategoryDataResponse("A03",1569364107680, "TABLETAS"),
        CategoryDataResponse("A04",1569364107680, "MONITORES"),
    )*/

    private var categoriesMutableList: MutableList<CategoryProvider> = mutableListOf()

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
        categoriesMutableList.add(CategoryProvider("",true))

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val myResponse: Response<CategoryEntity> =
                    retrofitInstance.create(ApiService::class.java).getCategories()
                if (myResponse.isSuccessful) {
                    val response : CategoryEntity = myResponse.body()!!
                    if (response.total != 0) {
                        //runOnUiThread { showCategoriesResults(response.data) }
                        /*for (i in 1..response.data.size){
                            categoriesMutableList.add(i, CategoryProvider(response.data[i].nombre))
                        }*/
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

        /*CategoryDataResponse("A01",1569364107680, "SMARTPHONES"),
        CategoryDataResponse("A02",1569364107680, "COMPUTADORAS"),
        CategoryDataResponse("A03",1569364107680, "TABLETAS"),
        CategoryDataResponse("A04",1569364107680, "MONITORES"),*/

        for (i in 1..4){
            categoriesMutableList.add(CategoryProvider("${i.toString()}"))
        }

        categoriesAdapter = CategoriesAdapter(
            categoriesMutableList,
            { position -> onItemSelected(position) },
            { onAddCategoryClick() }
        ) { onShowCategoryClick() }
        binding.rvCategories.setHasFixedSize(true)
        //binding.rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rvCategories.layoutManager = linearLayoutManager
        binding.rvCategories.adapter = categoriesAdapter

        categoriesMutableList.add(categoriesMutableList.size, CategoryProvider(""))
    }

    private fun onShowCategoryClick() {

    }

    private fun onAddCategoryClick() {

    }

    private fun onItemSelected(position: Int) {
        if(position < categoriesMutableList.size-1){    // Validamos para que los cambios no afecten a la última posición ("ADD")
            categoriesMutableList[position].isSelected = !categoriesMutableList[position].isSelected

            // Contabilizamos la cantidad de categorias seleccionadas, desde 1 hasta la última posición (no contando "TODAS" ni "ADD"
            var countSelected = 0
            for (i in 1 ..(categoriesMutableList.size-2)){
                if(categoriesMutableList[i].isSelected) {
                    countSelected += 1
                }
            }
            // ---------------------------------------------------------------------------------------

            // Deshabilita la posición 0 "TODAS" cuando se selecciona alguna otra
            if(position > 0 && categoriesMutableList[0].isSelected){
                categoriesMutableList[0].isSelected = false
                categoriesAdapter.notifyItemChanged(0)
            }
            // -------------------------------------------------------------

            // Deshabilita el resto cuando se selecciona "TODAS"
            if(position == 0){  // Verificamos que se haya seleccionado la opción "TODAS"
                categoriesMutableList[0].isSelected = true
                if(countSelected > 0){  // Con la variable contador, verificamos si hay opciones/categorias seleccionadas
                    for (i in 1..categoriesMutableList.size-2){
                        if(categoriesMutableList[i].isSelected){    // Verificamos cuales de las otras opciones se han seleccionado y lo notificamos
                            categoriesMutableList[i].isSelected = false
                            categoriesAdapter.notifyItemChanged(i)
                        }
                    }
                }
            }
            // ---------------------------------------------------

            //  Validamos si se seleccionan todas las categorias o ninguna, para regresar el foco a la opcion "TODAS"
            if(countSelected == categoriesMutableList.size-2 || countSelected == 0){
                categoriesMutableList[0].isSelected = true  // La posición 0 es donde se almacena la opción "TODAS"
                for (i in 1 ..(categoriesMutableList.size-2)){
                    categoriesMutableList[i].isSelected = false
                    if (countSelected != 0){    // Validamos para que solo notifique cuando hayamos seleccionado todas las categorias (menos "TODAS" y "ADD")
                        categoriesAdapter.notifyItemChanged(i)
                    }
                }
                categoriesAdapter.notifyItemChanged(0)  // Notificamos el cambio para la opción "TODAS", se activa nuevamente
                linearLayoutManager.scrollToPosition(0) // Le indicamos al LayoutManager, que haga un autoscroll a la posición 0 ("TODAS")
            }
        } else {
            showDialogAddCategory()
        }
        // ------------------------------------------------------------------------------------------------------------------

        categoriesAdapter.notifyItemChanged(position) // Notificamos cambios para la opción seleccionada por el usuario, la posición actual
    }

    private fun showDialogAddCategory() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_category)

        val edNameCategory: EditText = dialog.findViewById(R.id.edNameCategory)
        val edKeyCategory: EditText = dialog.findViewById(R.id.edKeyCategory)
        val edDateCategory: EditText = dialog.findViewById(R.id.edDateCategory)
        val btnAddCategory: Button = dialog.findViewById(R.id.btnAddCategory)

        btnAddCategory.setOnClickListener {
            val currentNameCategory = edNameCategory.text.toString()
            val currentKeyCategory = edKeyCategory.text.toString()
            val currentDateCategory = edDateCategory.text.toString()

            if(currentNameCategory.isNotEmpty() && currentKeyCategory.isNotEmpty() && currentDateCategory.isNotEmpty()){
                val currentCategory = CategoryDataResponse(currentKeyCategory, currentDateCategory.toLong(), currentNameCategory)

                categoriesMutableList.add(categoriesMutableList.size-1, CategoryProvider(currentNameCategory))
                categoriesAdapter.notifyItemInserted(categoriesMutableList.size-1)
                dialog.hide()
            }
        }

        dialog.show()
        Log.d("DEBUG", "Tamaño: ${categoriesMutableList.size},  ${categoriesMutableList.indices}, ${categoriesMutableList}")
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
            is CategoryDataResponse -> { categoriesAdapter.updateList(listOf(result as CategoryProvider)) }

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