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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.adapter.FlashCardAdapter
import com.pepivsky.loginfirebaseconkotlin.model.Collection
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard


class CreateCollectionFragment : Fragment() {

    private lateinit var rvNewCard: RecyclerView
    private lateinit var fabAdd: FloatingActionButton
    private lateinit var btnGuardar: Button
    private lateinit var edtTitle: EditText
    private lateinit var edtDescription: EditText

    private lateinit var adapter: FlashCardAdapter

    private val flashCards = mutableListOf<FlashCard>()

    private val db = FirebaseFirestore.getInstance()

    private lateinit var email: String



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_collection, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //binding
        rvNewCard = view.findViewById(R.id.rviNewItems)
        fabAdd = view.findViewById(R.id.fabAdd)
        btnGuardar = view.findViewById(R.id.btnGuardar)
        edtTitle = view.findViewById(R.id.edtTitle)
        edtDescription = view.findViewById(R.id.edtDescription)


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
        fabAdd.setOnClickListener {
            flashCards.add(FlashCard(null, null))
            adapter.notifyItemInserted(flashCards.size - 1)
            Toast.makeText(context, "Agregado! Size ${flashCards.size}", Toast.LENGTH_SHORT).show()

        }

        //guardar los objetos
        btnGuardar.setOnClickListener {
            Log.i("myTag", "$flashCards")
            Log.i("size", "${flashCards.size}")

            //guardando el nuevo objeto collection
            val newCollection = Collection(edtTitle.text.toString(), flashCards)
            //Collections.collectionsList.add(newCollection) //TODO guardar el objeto en Firebase

            addCollectionToBD(newCollection, email)

            Toast.makeText(activity, "Coleccion guardada :)", Toast.LENGTH_SHORT).show()
            Log.i("colecciones", "${Collections.collectionsList}")

        }


    }

    private fun initRecycler() {
        rvNewCard.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        adapter = FlashCardAdapter(flashCards)
        rvNewCard.adapter = adapter

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