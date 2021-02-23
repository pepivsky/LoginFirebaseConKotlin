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
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

private const val ARG_STRING = "string"
private const val ARG_CARD = "card"

class CardFragment : Fragment(), View.OnClickListener {

    private var string: String? = null

    var card: FlashCard? = null

    //views
    private lateinit var tvConcept: TextView
    private lateinit var tvDefiniton: TextView

    private lateinit var btnSiguiente: Button

    lateinit var front_anim: AnimatorSet
    lateinit var back_anim: AnimatorSet

    lateinit var cardFront: CardView
    lateinit var cardBack: CardView

    var isFront = true

    //callback
    private var callback: OnButtonListener? = null //instancia de la interface, este es asignadoa al boton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //arguments?.getString(ARG_STRING) //es lo mismo que usando let

        arguments?.let {
            //string = it.getString(ARG_STRING)//obteniendo el parametro por su clave y guardandolo en la variable String

            card = it.getParcelable(ARG_CARD)

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_card, container, false)

        tvConcept = view.findViewById(R.id.tvConceptCard)
        tvDefiniton = view.findViewById(R.id.tvDefinitionCard)
        btnSiguiente = view.findViewById(R.id.btnSiguiente)

        cardFront = view.findViewById(R.id.cardFront)
        cardBack = view.findViewById(R.id.cardBack)

        btnSiguiente.setOnClickListener {
            callback?.onButtonClicked() //se llama el metodo del callback perviamente seteado con funcionalidad desde el main
        }

        //seteando
        tvConcept.text = card?.concept

        tvDefiniton.text = card?.definition

        prepareFlipCard()//nuevaLinea, necesaria para lanimacion al darle tap a un card


        return view

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
        val scale = context?.resources?.displayMetrics?.density //obteniendo la escala
        cardFront.cameraDistance = 8000 * scale!! //asignando distancia de la camara
        cardBack.cameraDistance = 8000 * scale

        //settear los animator
        front_anim = AnimatorInflater.loadAnimator(context, R.animator.front_animator) as AnimatorSet
        back_anim = AnimatorInflater.loadAnimator(context, R.animator.back_animator) as AnimatorSet

        //girar card al darle tap (hay que quitarle la elevacion para que se vea bien)
        cardFront.setOnClickListener {
            if (isFront) {
                front_anim.setTarget(cardFront)
                back_anim.setTarget(cardBack)

                front_anim.start()
                back_anim.start()

                isFront = false
            } else {
                front_anim.setTarget(cardBack)
                back_anim.setTarget(cardFront)

                back_anim.start()
                front_anim.start()

                isFront = true
            }
        }

        cardBack.setOnClickListener {
            if (isFront) {
                front_anim.setTarget(cardFront)
                back_anim.setTarget(cardBack)

                front_anim.start()
                back_anim.start()

                isFront = false
            } else {
                front_anim.setTarget(cardBack)
                back_anim.setTarget(cardFront)

                back_anim.start()
                front_anim.start()

                isFront = true
            }
        }
    }
}