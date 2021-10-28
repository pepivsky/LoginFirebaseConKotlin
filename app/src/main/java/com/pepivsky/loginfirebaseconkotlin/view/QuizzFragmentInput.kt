package com.pepivsky.loginfirebaseconkotlin.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.databinding.FragmentQuizzInputBinding
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

private const val ARG_CARD = "card"

class QuizzFragmentInput : Fragment(R.layout.fragment_quizz_input), View.OnClickListener {

    lateinit var binding: FragmentQuizzInputBinding
    var card: FlashCard? = null
    //listener
    private var callback: OnButtonListener? = null //instancia de la interface, este es asignadoa al boton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentQuizzInputBinding.bind(view)

        arguments?.let {
            card = it.getParcelable(ARG_CARD)
        }

        binding.tvConceptInput.text = card?.concept //setear el valor del card

        //onclick del boton
        binding.btnContitnuarInput.setOnClickListener {
            if (binding.edtConceptInput.text?.isNotBlank() == true) {//check if is correct
                if (binding.edtConceptInput.text.toString()
                        .toLowerCase()
                        .contains(card?.definition.toString().toLowerCase())
                ) {
                    colorizeCorrect(binding.btnContitnuarInput)
                } else {
                    colorizeIncorrect(binding.btnContitnuarInput)
                }

                binding.btnContitnuarInput.postDelayed({
                    binding.btnContitnuarInput.setBackgroundColor(
                        resources.getColor(
                            R.color.purple_500,
                            null
                        )
                    )
                    callback?.onButtonClicked()
                }, 1000)

            } else {
                Snackbar.make(binding.btnContitnuarInput, "El campo no debe estar vacio", Snackbar.LENGTH_SHORT).show()
            }
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
        fun newInstance(card: FlashCard?) =
            QuizzFragmentInput().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_CARD, card) //aqui se le puede pasar cualquier cosa

                }
            }
    }

    fun setOnButtonListener(callback: OnButtonListener) { //setter
        this.callback = callback

    }

    //interface para que el activity controle el onClic y pueda crear fragments
    interface OnButtonListener {
        fun onButtonClicked()
    }

    override fun onClick(v: View?) {
        //callback
    }
}