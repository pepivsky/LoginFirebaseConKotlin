package com.pepivsky.loginfirebaseconkotlin.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

class FlashCardAdapter(val flashcards: MutableList<FlashCard>): RecyclerView.Adapter<FlashCardAdapter.FlashcardHolder>()  {


    companion object {
        lateinit var flashcardsList: MutableList<FlashCard>
    }

    init {
        flashcardsList = flashcards
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return FlashcardHolder(layoutInflater.inflate(R.layout.item_front_back_card , parent, false))
    }

    override fun onBindViewHolder(holder: FlashcardHolder, position: Int) {
        holder.render(flashcards[position])

        holder.edtConcept.setText(flashcards[position].concept)
        holder.edtDefinition.setText(flashcards[position].definition)


    }

    override fun getItemCount(): Int {
        return flashcards.size
    }


    class FlashcardHolder(val view: View): RecyclerView.ViewHolder(view) {
        lateinit var edtConcept: EditText
        lateinit var edtDefinition: EditText




        //lateinit var flashCard: FlashCard

        fun render(flashCard: FlashCard) {
            edtConcept = view.findViewById(R.id.edtConcept)
            edtDefinition = view.findViewById(R.id.edtDefinition)



            edtConcept.addTextChangedListener(object : TextWatcher { //Listener para setear el texto cuando sea  escrito
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //before
                    edtConcept.backgroundTintList = ColorStateList.valueOf(Color.RED) //rojo cuando esta vacio
                    edtConcept.setHintTextColor(ColorStateList.valueOf(Color.RED))
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    flashcardsList.get(adapterPosition).concept = edtConcept.text.toString()

                }

                override fun afterTextChanged(s: Editable?) {
                    //revisar si estan llenos los editText
                    if (edtConcept.text.isNotEmpty()) {
                        edtConcept.backgroundTintList = ColorStateList.valueOf(Color.GRAY) //gris cuando se ha ingresado texto
                        edtConcept.setHintTextColor(ColorStateList.valueOf(Color.GRAY))
                    }
                }

            })

            //definicion
            edtDefinition.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //before
                    Log.i("tag", "Test")
                    //edtDefinition.backgroundTintMode
                    edtDefinition.backgroundTintList = ColorStateList.valueOf(Color.RED)
                    edtDefinition.setHintTextColor(ColorStateList.valueOf(Color.RED))
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    flashcardsList.get(adapterPosition).definition = edtDefinition.text.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    if (edtDefinition.text.isNotEmpty()) {
                        edtDefinition.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                        edtDefinition.setHintTextColor(ColorStateList.valueOf(Color.GRAY))
                    }
                }

            })



            //seteando atributos
            //flashCard.concept = edtConcept.text.toString()
            //flashCard.definition = edtConcept.text.toString()
        }



    }
}