package com.pepivsky.loginfirebaseconkotlin.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard
import com.pepivsky.loginfirebaseconkotlin.R

class QuizzActivity : AppCompatActivity(),  CardFragment.OnButtonListener, QuizzFragmentConcept.OnButtonListener, QuizzFragmentDefinition.OnButtonListener, QuizzFragmentInput.OnButtonListener {

    lateinit var progressBar: RoundCornerProgressBar
    lateinit var btnExitQuizz: ImageButton

    //private val cardsList = MutableList<FlashCard>()
    private lateinit var list: List<FlashCard> //lista de flashCards

    var cardsList = mutableListOf<FlashCard>() //lista para crear los fragmentCard
    var listForQuizzFragmentConcept = mutableListOf<FlashCard>() //lista para crear los fragmentQuizzConcept
    var listoForQuizzFragmentDefinition = mutableListOf<FlashCard>()//lista para crear los fragmentQuizzDefinition
    var listoForQuizzFragmentInput = mutableListOf<FlashCard>()//lista para crear los fragmentQuizzDefinition



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_quizz)

        supportActionBar?.hide(); // ocultar el toolbar

        //obtener el extra con el index
        var bundle: Bundle? = intent.extras
        var index = bundle!!.getInt("pos") // 1
        Log.i("quizzActivity", "$index     ${Collections.collectionsList[index]}")

        //obteniendo el arrayList
        list = bundle.getParcelableArrayList<FlashCard>("lsitaTarjetas")!!

        cardsList = list.toMutableList()
        listForQuizzFragmentConcept = list.toMutableList()
        listoForQuizzFragmentDefinition = list.toMutableList()
        listoForQuizzFragmentInput = list.toMutableList()

        Log.i("lista recibida", "$cardsList")

        //Collections.collectionsList[message]

        /*val collection = Collections.collectionsList[index]
        cardsList = collection.listCard
        Log.i("testeandoLista", "${cardsList}")*/

        //private val cardsList = mutableListOf<Card>

        newFragmentCard() //agregar el primer fragment
        initUI()


    }

    private fun initUI() {
        initProgressBar()

        btnExitQuizz = findViewById(R.id.cancelButton)
        btnExitQuizz.setOnClickListener {
            onBackPressed()
        }
    }

    private fun initProgressBar() {
        progressBar = findViewById(R.id.pbQuizz)
        progressBar.max = ((list.size * 4) - 1).toFloat() //establecer el maximo valor del progressBar
    }

    fun sacarTarjeta(): FlashCard {
        val rand =
            (0 until cardsList.size).random() //obtiendo un numero entre 0 y el tamano de la lista
        val card = cardsList[rand] //extrayendo un valor de la lista
        //eliminar el item
        cardsList.removeAt(rand) //quitando la palabra de la lista

        return card
    }

    fun newFragmentCard() { //funcion que crea fragments
        val card = sacarTarjeta()
        val fragment = CardFragment.newInstance(card) //pasandole el string que necesita el fragment

        //seteando el onButtonListener
        fragment.setOnButtonListener(this)

        //fragment.setOnbuttonListener(this)
        supportFragmentManager //TODO implemetar animaciones
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right)
            .replace(R.id.container, fragment)//conteneror y fragment por el cual se reemplaza
            .commit()

    }

    //funciones para crear fragmentConcept
    fun newFragmentConcept() {
        val card = sacarCard()

        val fragmentConcept = QuizzFragmentConcept.newInstance(list, card)

        fragmentConcept.setOnButtonListener(this)

        supportFragmentManager //TODO implemetar animaciones
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right)
            .replace(
                R.id.container,
                fragmentConcept
            )//conteneror y fragment por el cual se reemplaza
            .commit()
    }

    fun sacarCard(): FlashCard {
        val rand =
            (0 until listForQuizzFragmentConcept.size).random() //obtiendo un numero entre 0 y el tamano de la lista
        val card = listForQuizzFragmentConcept[rand] //extrayendo un valor de la lista
        //eliminar el item
        listForQuizzFragmentConcept.removeAt(rand) //quitando la palabra de la lista

        return card
    }

    //funciones para crear fragmentDefinition
    fun newFragmentDefinition() {
        val card = sacarCardDef()

        val fragmentConcept = QuizzFragmentDefinition.newInstance(list, card)

        fragmentConcept.setOnButtonListener(this)

        supportFragmentManager //TODO implemetar animaciones
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right)
            .replace(
                R.id.container,
                fragmentConcept
            )//conteneror y fragment por el cual se reemplaza
            .commit()
    }

    fun sacarCardDef(): FlashCard {
        val rand =
            (0 until listoForQuizzFragmentDefinition.size).random() //obtiendo un numero entre 0 y el tamano de la lista
        val card = listoForQuizzFragmentDefinition[rand] //extrayendo un valor de la lista
        //eliminar el item
        listoForQuizzFragmentDefinition.removeAt(rand) //quitando la palabra de la lista

        return card
    }

    //frgament input
    fun sacarTarjeta1(): FlashCard {
        val rand =
            (0 until listoForQuizzFragmentInput.size).random() //obtiendo un numero entre 0 y el tamano de la lista
        val card = listoForQuizzFragmentInput[rand] //extrayendo un valor de la lista
        //eliminar el item
        listoForQuizzFragmentInput.removeAt(rand) //quitando la palabra de la lista

        return card
    }

    fun newFragmentInput() { //funcion que crea fragments
        val card = sacarTarjeta1()
        val fragment = QuizzFragmentInput.newInstance(card) //pasandole el string que necesita el fragment

        //seteando el onButtonListener
        fragment.setOnButtonListener(this)

        //fragment.setOnbuttonListener(this)
        supportFragmentManager //TODO implemetar animaciones
            .beginTransaction()
            .setCustomAnimations(R.anim.slide_out_left, R.anim.slide_in_right)
            .replace(R.id.container, fragment)//conteneror y fragment por el cual se reemplaza
            .commit()

    }

    //Al darle clic al boton, se lanza este metodo que crea nuevos Fragments
    override fun onButtonClicked() { //funcion sobreescrita que viene en la interfaz
        if (cardsList.isNotEmpty()) { //si todavia hay elementos en mi lista puedo crear mas fragemnts
            newFragmentCard()
            progressBar.progress++
        } else {
            if (listForQuizzFragmentConcept.isNotEmpty()) {
                newFragmentConcept()
                progressBar.progress++
            } else {
                if (listoForQuizzFragmentDefinition.isNotEmpty()) {
                    newFragmentDefinition()
                    progressBar.progress++
                } else {
                    if (listoForQuizzFragmentInput.isNotEmpty()) {
                        newFragmentInput()
                        progressBar.progress++
                    } else {
                        Toast.makeText(this, "No hay mas tarjetas", Toast.LENGTH_LONG).show()
                        Log.i("lista final", "$list")
                        Log.i("lista normal", "$cardsList")
                        //Log.i("lista test", "$testFragmentList")
                    }

                }
            }
        }
    }

    override fun onBackPressed() {
        //show dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("¿Estás seguro que deseas salir?")
        builder.setMessage("Perderás el progreso de tu sesión de estudio")

        builder.setPositiveButton(R.string.dialog_exit) { dialog, which ->
            super.onBackPressed()
        }

        builder.setNegativeButton(R.string.dialog_cancel) { dialog, which ->
            dialog.dismiss()
        }
        builder.show()
    }
}
