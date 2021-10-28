package com.pepivsky.loginfirebaseconkotlin.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.databinding.ItemFrontBackCardBinding
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard

class FlashCardAdapter(private val flashcards: MutableList<FlashCard>): RecyclerView.Adapter<FlashCardAdapter.FlashcardHolder>()  {


    companion object {
        lateinit var flashcardsList: MutableList<FlashCard>
    }

    init {
        flashcardsList = flashcards
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FlashcardHolder {
        val binding = ItemFrontBackCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FlashcardHolder(binding)
    }

    override fun onBindViewHolder(holder: FlashcardHolder, position: Int) {
        holder.render()
        holder.binding.edtConcept.setText(flashcards[position].concept)
        holder.binding.edtDefinition.setText(flashcards[position].definition)
    }

    override fun getItemCount(): Int {
        return flashcards.size
    }

    class FlashcardHolder(val binding: ItemFrontBackCardBinding): RecyclerView.ViewHolder(binding.root) {

        fun render() {

            binding.edtConcept.addTextChangedListener(object : TextWatcher { //Listener para setear el texto cuando sea  escrito
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //before
                    binding.edtConcept.backgroundTintList = ColorStateList.valueOf(Color.RED) //rojo cuando esta vacio
                    binding.edtConcept.setHintTextColor(ColorStateList.valueOf(Color.RED))
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    flashcardsList.get(adapterPosition).concept = binding.edtConcept.text.toString()

                }

                override fun afterTextChanged(s: Editable?) {
                    //revisar si estan llenos los editText
                    if (binding.edtConcept.text.isNotEmpty()) {
                        binding.edtConcept.backgroundTintList = ColorStateList.valueOf(Color.GRAY) //gris cuando se ha ingresado texto
                        binding.edtConcept.setHintTextColor(ColorStateList.valueOf(Color.GRAY))
                    }
                }

            })

            //definicion
            binding.edtDefinition.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    //before
                    Log.i("tag", "Test")
                    //binding.edtDefinition.backgroundTintMode
                    binding.edtDefinition.backgroundTintList = ColorStateList.valueOf(Color.RED)
                    binding.edtDefinition.setHintTextColor(ColorStateList.valueOf(Color.RED))
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    flashcardsList.get(adapterPosition).definition = binding.edtDefinition.text.toString()
                }

                override fun afterTextChanged(s: Editable?) {
                    if (binding.edtDefinition.text.isNotEmpty()) {
                        binding.edtDefinition.backgroundTintList = ColorStateList.valueOf(Color.GRAY)
                        binding.edtDefinition.setHintTextColor(ColorStateList.valueOf(Color.GRAY))
                    }
                }

            })
        }



    }
}