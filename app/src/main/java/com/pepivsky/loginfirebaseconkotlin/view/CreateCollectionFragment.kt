package com.pepivsky.loginfirebaseconkotlin.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.adapter.FlashCardAdapter
import com.pepivsky.loginfirebaseconkotlin.databinding.FragmentCreateCollectionBinding
import com.pepivsky.loginfirebaseconkotlin.model.Collection
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard


class CreateCollectionFragment : Fragment(R.layout.fragment_create_collection) {

    lateinit var binding: FragmentCreateCollectionBinding
    private lateinit var adapter: FlashCardAdapter
    private val flashCards = mutableListOf<FlashCard>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var email: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCreateCollectionBinding.bind(view)

        flashCards.add(FlashCard(null, null))// primer item
        initRecycler()

        //obteniendo el email desde las shared preferences
        val sharedPref = activity?.getSharedPreferences(
            getString(R.string.prefs_file),
            Context.MODE_PRIVATE
        )
        email = sharedPref?.getString("email", "email vacio").toString()
        Log.i("CCFragment", "$email")


        //agregar otro item
        binding.fabAdd.setOnClickListener {
            flashCards.add(FlashCard(null, null))
            adapter.notifyItemInserted(flashCards.size - 1)
            Toast.makeText(context, "Agregado! Size ${flashCards.size}", Toast.LENGTH_SHORT).show()

        }

        //guardar los objetos
        binding.btnGuardar.setOnClickListener {
            Log.i("myTag", "$flashCards")
            Log.i("size", "${flashCards.size}")

            //guardando el nuevo objeto collection
            val newCollection = Collection(binding.edtTitle.text.toString(), flashCards)
            //Collections.collectionsList.add(newCollection) //TODO guardar el objeto en Firebase

            addCollectionToBD(newCollection, email)

            Toast.makeText(activity, "Coleccion guardada :)", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.action_createCollectionFragment_to_homeFragment) // navegar al home
            Log.i("colecciones", "${Collections.collectionsList}")

        }
    }

    private fun initRecycler() {
        binding.rviNewItems.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = FlashCardAdapter(flashCards)
        binding.rviNewItems.adapter = adapter

    }

    private fun addCollectionToBD(collection: Collection, email: String) {
        val refUser = db.collection("users").document(email)
        refUser.update("collections", FieldValue.arrayUnion(collection))

        if (Collections.collectionsList.isNotEmpty()) {
            Collections.collectionsList.add(collection)
        }
    }

    companion object {
        fun deleteCollectionToBD(collection: Collection, email: String) {
            val refUser = FirebaseFirestore.getInstance().collection("users").document(email)
            //refUser.update("collections", FieldValue.arrayUnion(collection))
            refUser.update("collections", FieldValue.arrayRemove(collection))

            //refUser.update("collections", FieldValue.delete())
        }
    }

    /*override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }*/
}