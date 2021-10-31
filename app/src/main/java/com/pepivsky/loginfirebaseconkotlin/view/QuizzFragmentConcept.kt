package com.pepivsky.loginfirebaseconkotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.databinding.FragmentQuizzConceptBinding
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

private const val ARG_CARD = "card"
private const val ARG_CARDSLIST = "cards"

class QuizzFragmentConcept : Fragment(R.layout.fragment_quizz_concept), View.OnClickListener {

    lateinit var binding: FragmentQuizzConceptBinding
    private lateinit var listCards: List<FlashCard>
    var card: FlashCard? = null
    private var callback: OnButtonListener? = null //instancia de la interface, este es asignadoa al boton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizzConceptBinding.bind(view)

        arguments?.let {
            listCards = it.getParcelableArrayList(ARG_CARDSLIST)!!
            card = it.getParcelable(ARG_CARD)
        }

        Log.i("answers", "")
        val answersList = getAnswers(card!!, listCards) // obtener las respuestas para setear en los botones

        //seteando valores
        binding.tvConceptQuizz.text = card?.concept
        //botones
        binding.btnDefinition1.text = answersList?.get(0)?.definition
        binding.btnDefinition2.text = answersList?.get(1)?.definition
        binding.btnDefinition3.text = answersList?.get(2)?.definition

        //comprobar si la respuesta es correcta
        binding.btnDefinition1.setOnClickListener {
            // TODO deshabilitar el boton para evitar el error al darle doble tap // it.isEnabled = false
            if (binding.btnDefinition1.text.toString()
                    .equals(card?.definition, true)
            ) { //aqui se compara el valor del texto del boton contra el del objeto card para saber si es la respuesta correcta
                Log.i("QuizzFragmentConcept", "DefinicionCorrecta ${card?.definition}")
                colorizeCorrect(binding.btnDefinition1)
            } else {
                Log.i("QuizzFragmentConcept", "Incorrecta")
                colorizeIncorrect(binding.btnDefinition1)
            }

            binding.btnDefinition1.postDelayed({
                binding.btnDefinition1.setBackgroundColor(resources.getColor(R.color.purple_500, null))
                callback?.onButtonClicked()
            }, 1000)
        }

        binding.btnDefinition2.setOnClickListener {
            if (binding.btnDefinition2.text.toString().equals(card?.definition, true)
            ) { //aqui se compara el valor del texto del boton contra el del objeto card para saber si es la respuesta correcta
                Log.i("QuizzFragmentConcept", "DefinicionCorrecta ${card?.definition}")
                //binding.btnDefinition1.setBackgroundColor(resources.getColor(R.color.green, null))
                colorizeCorrect(binding.btnDefinition2)
            } else {
                Log.i("QuizzFragmentConcept", "Incorrecta")
                colorizeIncorrect(binding.btnDefinition2)
            }

            binding.btnDefinition2.postDelayed({
                binding.btnDefinition2.setBackgroundColor(resources.getColor(R.color.purple_500, null))
                callback?.onButtonClicked()
            }, 1000)
        }

        binding.btnDefinition3.setOnClickListener {
            if (binding.btnDefinition3.text.toString()
                    .equals(card?.definition, true)
            ) { //aqui se compara el valor del texto del boton contra el del objeto card para saber si es la respuesta correcta
                Log.i("QuizzFragmentConcept", "DefinicionCorrecta ${card?.definition}")
                //binding.btnDefinition1.setBackgroundColor(resources.getColor(R.color.green, null))
                colorizeCorrect(binding.btnDefinition3)
            } else {
                Log.i("QuizzFragmentConcept", "Incorrecta")
                colorizeIncorrect(binding.btnDefinition3)
            }

            binding.btnDefinition3.postDelayed({
                binding.btnDefinition3.setBackgroundColor(resources.getColor(R.color.purple_500, null))
                callback?.onButtonClicked()
            }, 1000)
        }

    }

    private fun colorizeIncorrect(button: Button) {
        button.setBackgroundColor(resources.getColor(R.color.red, null))
    }

    private fun colorizeCorrect(button: Button) {
        button.setBackgroundColor(resources.getColor(R.color.green, null))
        Collections.counter++
    }

    companion object {
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
    interface OnButtonListener {
        fun onButtonClicked()
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}