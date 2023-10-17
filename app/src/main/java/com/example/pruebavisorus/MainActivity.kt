package com.example.pruebavisorus

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
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

    private lateinit var lyPrices: LinearLayout

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

    private var articlesMutableList: MutableList<ArticleDataResponse> = mutableListOf()

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
        binding.fabAddArticle.setOnClickListener { showDialogAddArticle() }
        initCategories()
        initArticles()

    }

    private fun showDialogAddArticle() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_add_article)

        // ---------------------- DIALOGO DE ALERTA ---------------------------------
        val okSpannable = SpannableString("OK")
        okSpannable.setSpan(ForegroundColorSpan(getColor(R.color.app_accent_color)),
            0, okSpannable.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setPositiveButton(okSpannable, null)
        // ----------------------------------------- ---------------------------------

        lyPrices = dialog.findViewById(R.id.layoutPrices)

        val listEdPrices: MutableList<EditText> = mutableListOf()
        val prices: MutableList<Int> = mutableListOf()

        val edNameArticle: EditText = dialog.findViewById(R.id.edNameArticle)
        val edKeyArticle: EditText = dialog.findViewById(R.id.edKeyArticle)
        val edCatArticle: Spinner = dialog.findViewById(R.id.spCatArticle)
        val btnAddPriceArticle: Button = dialog.findViewById(R.id.btnAddPrice)
        val btnAddArticle: Button = dialog.findViewById(R.id.btnAddArticle)

        btnAddPriceArticle.setOnClickListener {
            var count = 0

            if(listEdPrices.isEmpty()){
                listEdPrices.add(addFieldPrice())
            } else {
                for(edPrices in listEdPrices){
                    if(edPrices.text.isEmpty()){
                        count += 1
                    }
                }

                if(count > 0) {
                    alertDialog.setTitle("CAMPO DE PRECIO VACIO")
                    alertDialog.setMessage("Agrega un valor al/a los campo(s) de precio anterior(es) para poder crear uno más.")
                    alertDialog.show()
                } else {
                    listEdPrices.add(addFieldPrice())
                }
            }
        }

        btnAddArticle.setOnClickListener {
            var priceFieldCompleted = false

            for(edPrices in listEdPrices){
                if(edPrices.text.isEmpty()){
                    priceFieldCompleted = false
                    break
                } else {
                    priceFieldCompleted = true
                    prices.add(edPrices.text.toString().toInt())
                }
            }

            if(priceFieldCompleted && edNameArticle.text.isNotEmpty() && edKeyArticle.text.isNotEmpty()){
                Log.d("BTN_ADD_SUCCES", prices.toString())
                dialog.hide()
            } else {
                prices.clear()
                alertDialog.setTitle("Campo(s) vacio(s)")
                alertDialog.setMessage("Ningun campo debe estar vacío")
                alertDialog.show()
            }
        }

        dialog.show()

    }

    private fun addFieldPrice() : EditText {
        val newEditText = EditText(this)

        newEditText.backgroundTintList = ColorStateList.valueOf(getColor(R.color.white))
        newEditText.hint = getString(R.string.app_dialog_add_price_article_new_edittext)
        newEditText.setHintTextColor(getColor(R.color.app_text_disabled_color))
        newEditText.inputType = InputType.TYPE_CLASS_NUMBER
        newEditText.setTextColor(getColor(R.color.app_text_color))
        newEditText.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        lyPrices.addView(newEditText)
        return  newEditText
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
                        runOnUiThread { showArticlesResults(response.data) }
                    } else {
                        //runOnUiThread { showArticlesResults(emptyList()) }
                        runOnUiThread { showConectionResults(EMPTY_RESPONSE) }
                    }
                } else {
                    //runOnUiThread { showArticlesResults(emptyList()) }
                    runOnUiThread { showConectionResults(RESPONSE_NO_SUCCESSFUL) }
                }
            } catch (ste: SocketTimeoutException){
                Log.d("ERROR_CONECTION","Falló al conectarse con el servidor: ${ste.toString()}")
                //runOnUiThread { showErrorConection() }
                runOnUiThread { showConectionResults(ERROR_CONECTION) }
            }
        }

        /*private var categoriesMutableList: MutableList<CategoryDataResponse> = mutableListOf(
        CategoryDataResponse("A01",1569364107680, "COMPUTADORAS"),
        CategoryDataResponse("A02",1569364107680, "SMARTPHONES"),
        CategoryDataResponse("A03",1569364107680, "TABLETAS"),
        CategoryDataResponse("A04",1569364107680, "MONITORES"),
    )*/

        /*articlesMutableList.add(ArticleDataResponse("L01",1,"Lenovo",
            listOf(Precio(12000F), Precio(12500F))))

        articlesMutableList.add(ArticleDataResponse("T01",1,"Toshiba",
            listOf(Precio(10000F), Precio(11500F))))

        articlesMutableList.add(ArticleDataResponse("S01",2,"Samnsung",
            listOf(Precio(6500F))))

        articlesMutableList.add(ArticleDataResponse("M01",2,"Motorola",
            listOf(Precio(8000F), Precio(4500F))))

        articlesMutableList.add(ArticleDataResponse("I01",3,"iPad",
            listOf(Precio(15000F), Precio(13000F))))

        articlesMutableList.add(ArticleDataResponse("G01",3,"Galaxy",
            listOf(Precio(14000F))))

        articlesMutableList.add(ArticleDataResponse("LG01",4,"LG",
            listOf(Precio(6500F))))

        articlesMutableList.add(ArticleDataResponse("BQ01",4,"BenQ",
            listOf(Precio(8200F))))
        */


        articlesAdapter = ArticlesAdapter(
            //articlesMutableList,
            emptyList(),
            { position -> onItemArticleSelected(position) }
        )
        binding.rvArticles.setHasFixedSize(true)
        binding.rvArticles.layoutManager = GridLayoutManager(this, 2)
        binding.rvArticles.adapter = articlesAdapter
    }

    private fun onItemArticleSelected(position: Int) {

    }

    private fun initCategories() {
        val todasCategory = CategoryDataResponse(0,"",0,"")
        val add = CategoryDataResponse(-1,"",0,"")
        categoriesMutableList.add(CategoryProvider(todasCategory, true))   //(CategoryProvider(0,"",true))
        categoriesMutableList.add(CategoryProvider(add))   //(CategoryProvider(0,"",true))

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val myResponse: Response<CategoryEntity> =
                    retrofitInstance.create(ApiService::class.java).getCategories()
                if (myResponse.isSuccessful) {
                    val response : CategoryEntity = myResponse.body()!!
                    if (response.total != 0) {
                        val index = categoriesMutableList.size-1
                        //var newsCategories: MutableList<CategoryProvider> = mutableListOf()//= CategoryProvider(response.data)
                        //runOnUiThread { showCategoriesResults(response.data) }
                        for (resp in response.data){
                            Log.d("comprobar", "entrando en el for ${resp}")
                            //categoriesMutableList.add(CategoryProvider(i,response.data[i].nombre))
                            categoriesMutableList.add(index, CategoryProvider(
                                CategoryDataResponse(
                                    id = resp.id,
                                    clave = resp.clave,
                                    fechaCreado = resp.fechaCreado,
                                    nombre = resp.nombre)
                                )
                            )
                        }
                        Log.d("comprobar", categoriesMutableList.toString())
                        //runOnUiThread { showResults(response.data) }
                        runOnUiThread { showCategoriesResults(categoriesMutableList) }
                    } else {
                        //runOnUiThread { showCategoriesResults(emptyList()) }
                        runOnUiThread { showConectionResults(EMPTY_RESPONSE) }
                    }
                } else {
                    //runOnUiThread { showCategoriesResults(emptyList()) }
                    runOnUiThread { showConectionResults(RESPONSE_NO_SUCCESSFUL) }
                }
            } catch (ste: SocketTimeoutException){
                Log.d("ERROR_CONECTION","Falló al conectarse con el servidor: ${ste.toString()}")
                //runOnUiThread { showErrorConection() }
                runOnUiThread { showConectionResults(ERROR_CONECTION) }
            }
        }

        /*categoriesMutableList.add(CategoryProvider(1,"COMPUTADORAS"))
        categoriesMutableList.add(CategoryProvider(2,"SMARTPHONES"))
        categoriesMutableList.add(CategoryProvider(3,"TABLETAS"))
        categoriesMutableList.add(CategoryProvider(4,"MONITORES"))*/


        categoriesAdapter = CategoriesAdapter(
            //categoriesMutableList,
            emptyList(),
            { position -> onItemSelected(position) },
            { onAddCategoryClick() },
            { onShowCategoryClick() }
        )
        binding.rvCategories.setHasFixedSize(true)
        //binding.rvCategories.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
        binding.rvCategories.layoutManager = linearLayoutManager
        binding.rvCategories.adapter = categoriesAdapter

        //categoriesMutableList.add(categoriesMutableList.size, CategoryProvider(-1,""))
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
        updateArticles()
    }

    private fun updateArticles() {
        /*var categoriesSelected= categoriesMutableList.filter { it.isSelected }
        var newArticles= categoriesMutableList

        for(category in categoriesSelected){
            if(category.id == 0){
                break
                //newArticles
            }
        }

        //position -> onItemArticleSelected(position)

        /*if(categoriesMutableList[0].isSelected){
            newArticles = articlesMutableList
        }*/

        Log.d("UpdateList",newArticles.toString())
        articlesAdapter.articlesList = listOf(newArticles as ArticleDataResponse)
        articlesAdapter.notifyDataSetChanged()*/
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
                //val currentCategory = CategoryDataResponse(currentKeyCategory, currentDateCategory.toLong(), currentNameCategory)

                val currentCategory = Category(
                    clave = currentKeyCategory,
                    nombre = currentNameCategory,
                    fechaCreado = currentDateCategory.toLong()
                )

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val myResponse: Response<CategoryEntityPost> =
                            retrofitInstance.create(ApiService::class.java).postCategories(currentCategory)
                        Log.d("Add-Category", myResponse.toString())
                        if (myResponse.isSuccessful) {
                            val response : CategoryEntityPost = myResponse.body()!!
                            if (response.message != "") {
                                val index = categoriesMutableList.size-1
                                //for (resp in response.data){
                                    Log.d("comprobar", "entrando en el for ${response}")
                                    //categoriesMutableList.add(CategoryProvider(i,response.data[i].nombre))
                                    categoriesMutableList.add(index, CategoryProvider(
                                        CategoryDataResponse(
                                            id = response.data.id,
                                            clave = response.data.clave,
                                            fechaCreado = response.data.fechaCreado,
                                            nombre = response.data.nombre)
                                    )
                                    )
                               // }
                                Log.d("Add-Category", categoriesMutableList.toString())
                                runOnUiThread { showCategoriesResults(categoriesMutableList) }
                            } else {
                                runOnUiThread { showConectionResults(EMPTY_RESPONSE) }
                            }
                        } else {
                            runOnUiThread { showConectionResults(RESPONSE_NO_SUCCESSFUL) }
                        }
                    } catch (ste: SocketTimeoutException){
                        runOnUiThread { showConectionResults(ERROR_CONECTION) }
                    }
                }

                //categoriesMutableList.add(categoriesMutableList.size-1, CategoryProvider(categoriesMutableList.size-1, currentNameCategory))
                //categoriesAdapter.notifyItemInserted(categoriesMutableList.size-1)
                dialog.hide()
            }
        }

        dialog.show()
        //Log.d("DEBUG", "Tamaño: ${categoriesMutableList.size},  ${categoriesMutableList.indices}, ${categoriesMutableList}")
    }

    private fun showCategoriesResults(result: List<CategoryProvider>) {
        categoriesAdapter.updateList(result)
    }

    private fun showArticlesResults(result: List<ArticleDataResponse>) {
        articlesAdapter.updateList(result)
    }

    private fun showConectionResults(value: Int) {
        var textToast: String = ""

        when {
            value == ERROR_CONECTION -> {  textToast = "ERROR AL CONECTARSE CON EL SERVIDOR" }

            value == RESPONSE_NO_SUCCESSFUL -> { textToast = "RESPUESTA FALLIDA DEL SERVIDOR" }

            value == EMPTY_RESPONSE -> { textToast = "RESPUESTA CON DATOS VACIOS" }
        }

        val toast = Toast.makeText(this, "Error de conexión", Toast.LENGTH_LONG)
        toast.show()
    }

    /*private fun <T> showResults(result: List<T>) {
        Log.d("TEST_SHOWRESULTS", result.toString())
        var textToast: String = ""
        when(result){
            is CategoryDataResponse -> {
                Log.d("TEST_SHOWRESULTS", "Dentro: ${result}")
                categoriesAdapter.updateList(listOf(result)) }

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
    }*/
}