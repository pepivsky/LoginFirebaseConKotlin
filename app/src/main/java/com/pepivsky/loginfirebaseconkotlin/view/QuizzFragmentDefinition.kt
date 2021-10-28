package com.pepivsky.loginfirebaseconkotlin.view

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.databinding.FragmentQuizzDefinitionBinding
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

private const val ARG_CARD = "card"
private const val ARG_CARDSLIST = "cards"

class QuizzFragmentDefinition : Fragment(R.layout.fragment_quizz_definition), View.OnClickListener {
    
    lateinit var binding: FragmentQuizzDefinitionBinding
    private lateinit var listCards: List<FlashCard>
    var card: FlashCard? = null
    private var callback: OnButtonListener? = null //instancia de la interface, este es asignadoa al boton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizzDefinitionBinding.bind(view)

        arguments?.let {
            listCards = it.getParcelableArrayList(ARG_CARDSLIST)!!
            card = it.getParcelable(ARG_CARD)
        }

        val answersList = getAnswers(card!!, listCards)//obtener las respuestas para setear en los botones

        //seteando valores
        binding.tvDefinitionfq.text = card?.definition

        //botones
        binding.btnConcept1.text = answersList?.get(0)?.concept
        binding.btnConcept2.text = answersList?.get(1)?.concept
        binding.btnConcept3.text = answersList?.get(2)?.concept

        binding.btnConcept1.setOnClickListener {
            if (binding.btnConcept1.text.toString().equals(card?.concept, true)
            ) { //aqui se compara el valor del texto del boton contra el del objeto card para saber si es la respuesta correcta
                Log.i("QuizzFragmentConcept", "DefinicionCorrecta ${card?.concept}")
                //btnDef1.setBackgroundColor(resources.getColor(R.color.green, null))
                colorizeCorrect(binding.btnConcept1)
            } else {
                Log.i("QuizzFragmentConcept", "Incorrecta")
                colorizeIncorrect(binding.btnConcept1)
            }
            binding.btnConcept1.postDelayed({
                binding.btnConcept1.setBackgroundColor(resources.getColor(R.color.purple_500, null))
                callback?.onButtonClicked()
            }, 1000)
        }
        binding.btnConcept2.setOnClickListener {
            if (binding.btnConcept2.text.toString().equals(card?.concept, true)
            ) { //aqui se compara el valor del texto del boton contra el del objeto card para saber si es la respuesta correcta
                Log.i("QuizzFragmentConcept", "DefinicionCorrecta ${card?.concept}")
                //btnDef1.setBackgroundColor(resources.getColor(R.color.green, null))
                colorizeCorrect(binding.btnConcept2)
            } else {
                Log.i("QuizzFragmentConcept", "Incorrecta")
                colorizeIncorrect(binding.btnConcept2)
            }
            binding.btnConcept2.postDelayed({
                binding.btnConcept2.setBackgroundColor(resources.getColor(R.color.purple_500, null))
                callback?.onButtonClicked()
            }, 1000)
        }
        binding.btnConcept3.setOnClickListener {
            if (binding.btnConcept3.text.toString()
                    .equals(card?.concept, true)
            ) { //aqui se compara el valor del texto del boton contra el del objeto card para saber si es la respuesta correcta
                Log.i("QuizzFragmentConcept", "DefinicionCorrecta ${card?.concept}")
                //btnDef1.setBackgroundColor(resources.getColor(R.color.green, null))
                colorizeCorrect(binding.btnConcept3)
            } else {
                Log.i("QuizzFragmentConcept", "Incorrecta")
                colorizeIncorrect(binding.btnConcept3)
            }
            binding.btnConcept3.postDelayed({
                binding.btnConcept3.setBackgroundColor(resources.getColor(R.color.purple_500, null))
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
            QuizzFragmentDefinition().apply {
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