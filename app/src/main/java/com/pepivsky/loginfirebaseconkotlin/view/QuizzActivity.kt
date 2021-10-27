package com.pepivsky.loginfirebaseconkotlin.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.commit
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
        intent.extras.let { bundle ->
            val index = bundle?.getInt("pos") // 1
            Log.i("quizzActivity", "$index     ${Collections.collectionsList[index!!]}")

            //obteniendo el arrayList
            list = bundle.getParcelableArrayList<FlashCard>("lsitaTarjetas") as List<FlashCard>

            cardsList = list.toMutableList()
            listForQuizzFragmentConcept = list.toMutableList()
            listoForQuizzFragmentDefinition = list.toMutableList()
            listoForQuizzFragmentInput = list.toMutableList()
            Log.i("lista recibida", "$cardsList")

            newFragmentCard() //agregar el primer fragment
            initUI()
        }
        //Collections.collectionsList[message]

        /*val collection = Collections.collectionsList[index]
        cardsList = collection.listCard
        Log.i("testeandoLista", "${cardsList}")*/

        //private val cardsList = mutableListOf<Card>
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
        Collections.total = list.size * 3
    }

    private fun removeRandomCard(cardList: MutableList<FlashCard>): FlashCard {
        //val rand = (0 until cardsList.size).random() //obtiendo un numero entre 0 y el tamano de la lista
        val randomCard = cardList.random() //extrayendo un valor de la lista
        //eliminar el item
        cardList.remove(randomCard) //quitando la palabra de la lista
        return randomCard
    }

    private fun newFragmentCard() { //funcion que crea fragments
        val card = removeRandomCard(cardsList)
        val fragment = CardFragment.newInstance(card) //pasandole el string que necesita el fragment

        //seteando el onButtonListener
        fragment.setOnButtonListener(this)

        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace(R.id.container, fragment)
        }
    }

    //funciones para crear fragmentConcept
    private fun newFragmentConcept() {
        val card = removeRandomCard(listForQuizzFragmentConcept)

        val fragmentConcept = QuizzFragmentConcept.newInstance(list, card)

        fragmentConcept.setOnButtonListener(this)

        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace(R.id.container, fragmentConcept)
        }
    }


    //funciones para crear fragmentDefinition
    private fun newFragmentDefinition() {
        val card = removeRandomCard(listoForQuizzFragmentDefinition)

        val fragmentDefinition = QuizzFragmentDefinition.newInstance(list, card)

        fragmentDefinition.setOnButtonListener(this)

        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace(R.id.container, fragmentDefinition)
        }
    }

    private fun newFragmentInput() { //funcion que crea fragments
        val card = removeRandomCard(listoForQuizzFragmentInput)
        val fragmentInput = QuizzFragmentInput.newInstance(card) //pasandole el string que necesita el fragment

        //seteando el onButtonListener
        fragmentInput.setOnButtonListener(this)

        supportFragmentManager.commit {
            setCustomAnimations(
                R.anim.slide_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.slide_out
            )
            replace(R.id.container, fragmentInput)
        }
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
                        //Toast.makeText(this, "No hay mas tarjetas", Toast.LENGTH_LONG).show()
                        Log.i("lista final", "$list")
                        Log.i("lista normal", "$cardsList")
                        val intent = Intent(this, ResultActivity::class.java)
                        startActivity(intent)
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
