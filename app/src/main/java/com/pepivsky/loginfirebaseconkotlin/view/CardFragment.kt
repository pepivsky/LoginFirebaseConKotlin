package com.pepivsky.loginfirebaseconkotlin.view

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.databinding.FragmentCardBinding
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

private const val ARG_CARD = "card"

class CardFragment : Fragment(R.layout.fragment_card), View.OnClickListener {

    lateinit var binding: FragmentCardBinding
    var card: FlashCard? = null
    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet
    var isFront = true
    //callback
    private var callback: OnButtonListener? = null //instancia de la interface, este es asignadoa al boton


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCardBinding.bind(view)

        arguments?.let {
            //string = it.getString(ARG_STRING)//obteniendo el parametro por su clave y guardandolo en la variable String

            card = it.getParcelable(ARG_CARD)

        }


        binding.btnSiguiente.setOnClickListener {
            callback?.onButtonClicked() //se llama el metodo del callback perviamente seteado con funcionalidad desde el main
        }

        //seteando
        binding.tvConceptCard.text = card?.concept

        binding.tvDefinitionCard.text = card?.definition

        prepareFlipCard()//nuevaLinea, necesaria para lanimacion al darle tap a un card

    }


    companion object {

        @JvmStatic //parametros que recibe, en este caso es un String
        fun newInstance(card: FlashCard?) =
            CardFragment().apply {
                //guardando el String dentro de un Bundle
                arguments = Bundle().apply {
                    //pasandole un String como parametro
                    putParcelable(ARG_CARD, card) //aqui se le puede pasar cualquier cosa

                }
            }
    }

    //fun setOnbuttonListener(callback: onButtonListener)

    //setear el listener
    fun setOnButtonListener(callback: OnButtonListener) { //setter
        this.callback = callback

    }

    //interface para que el activity controle el onClic y pueda crear fragments
    interface OnButtonListener{
        fun onButtonClicked()
    }

    override fun onClick(v: View?) {
        //callback
    }
    fun prepareFlipCard() {
        val scale = requireContext().resources.displayMetrics.density //obteniendo la escala
        binding.cardFront.cameraDistance = 8000 * scale // asignando distancia de la camara
        binding.cardBack.cameraDistance = 8000 * scale

        //settear los animator
        front_anim = AnimatorInflater.loadAnimator(context, R.animator.front_animator) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(context, R.animator.back_animator) as AnimatorSet

        //girar card al darle tap (hay que quitarle la elevacion para que se vea bien)
        //girar card al darle tap (hay que quitarle la elevacion para que se vea bien)
        binding.cardFront.setOnClickListener {
            if (isFront) {

                if (!front_anim.isRunning && !back_anim.isRunning ) {
                    //front_anim.
                    front_anim.setTarget(binding.cardFront)
                    back_anim.setTarget(binding.cardBack)

                    front_anim.start()
                    back_anim.start()
                    isFront = false
                }

            } else {

                if (!front_anim.isRunning && !back_anim.isRunning ) {
                    front_anim.setTarget(binding.cardBack)
                    back_anim.setTarget(binding.cardFront)


                    back_anim.start()
                    front_anim.start()
                    isFront = true
                }


            }
        }

        binding.cardBack.setOnClickListener {
            if (isFront) {


                if (!front_anim.isRunning && !back_anim.isRunning ) {
                    front_anim.setTarget(binding.cardFront)
                    back_anim.setTarget(binding.cardBack)

                    back_anim.start()
                    front_anim.start()
                    isFront = false
                }


            } else {

                if (!front_anim.isRunning && !back_anim.isRunning ) {
                    front_anim.setTarget(binding.cardBack)
                    back_anim.setTarget(binding.cardFront)

                    back_anim.start()
                    front_anim.start()
                    isFront = true
                }


            }
        }
    }
}