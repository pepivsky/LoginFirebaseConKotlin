package com.pepivsky.loginfirebaseconkotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_CARD = "card"
private const val ARG_CARDSLIST = "cards"

/**
 * A simple [Fragment] subclass.
 * Use the [QuizzFragmentConcept.newInstance] factory method to
 * create an instance of this fragment.
 */
class QuizzFragmentConcept : Fragment(), View.OnClickListener {

    private lateinit var listCards: List<FlashCard>
    var card: FlashCard? = null

    private var callback: OnButtonListener? = null //instancia de la interface, este es asignadoa al boton

    private lateinit var btnDef1: Button
    private lateinit var btnDef2: Button
    private lateinit var btnDef3: Button
    private lateinit var tvConcept: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            listCards = it.getParcelableArrayList(ARG_CARDSLIST)!!
            card = it.getParcelable(ARG_CARD)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_quizz_concept, container, false)
        Log.i("answers", "")
        val answersList = getAnswers(card!!, listCards)//obtener las respuestas para setear en los botones

        btnDef1 = view.findViewById(R.id.btnDefinition1)
        btnDef2 = view.findViewById(R.id.btnDefinition2)
        btnDef3 = view.findViewById(R.id.btnDefinition3)
        tvConcept = view.findViewById(R.id.tvConceptQuizz)


        //seteando valores
        tvConcept.text = card?.concept

        //botones
        btnDef1.text = answersList?.get(0)?.definition
        btnDef2.text = answersList?.get(1)?.definition
        btnDef3.text = answersList?.get(2)?.definition

        //comprobar si la respuesta es correcta
        btnDef1.setOnClickListener {
            if (btnDef1.text.toString().equals(card?.definition, true)) { //aqui se compara el valor del texto del boton contra el del objeto card para saber si es la respuesta correcta
                Log.i("QuizzFragmentConcept", "DefinicionCorrecta")
            }
            callback?.onButtonClicked()
        }


        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment QuizzFragmentConcept.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(listCards: List<FlashCard>, card: FlashCard) =
            QuizzFragmentConcept().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_CARDSLIST, ArrayList(listCards))
                    putParcelable(ARG_CARD, card)

                }
            }
    }

    fun getAnswers(correctCard: FlashCard, cardsList: List<FlashCard>): List<FlashCard> {
        //val cardsList = mutableListOf<String>("uno", "dos", "tres", "cuatro", "cinco", "seis", "siete")
        Log.i("listaAnserws", "$cardsList")
        val cardsCopy = cardsList.toMutableList()

        val correctIndex = cardsCopy.indexOf(correctCard)
        val correctAnswer = cardsCopy[correctIndex]


        cardsCopy.removeAt(correctIndex)
        cardsCopy.shuffle()
        val wrongAnswer = cardsCopy[0]
        val wrongAnswer2 = cardsCopy[1]
        val answers = mutableListOf(correctAnswer, wrongAnswer, wrongAnswer2)
        answers.shuffle()
        return answers
    }

    fun setOnButtonListener(callback: OnButtonListener) { //setter
        this.callback = callback

    }

    //interface para que el activity controle el onClic y pueda crear fragments
    interface OnButtonListener{
        fun onButtonClicked()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}