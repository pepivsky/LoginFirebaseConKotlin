package com.pepivsky.loginfirebaseconkotlin.view

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.model.Collection
import com.pepivsky.loginfirebaseconkotlin.adapter.CollectionAdapter
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.model.User


class HomeFragment : Fragment() {

    private lateinit var rvCollections: RecyclerView
    private lateinit var edtBuscar: EditText

    private lateinit var email: String
    private val db = FirebaseFirestore.getInstance()


    /*val collections = mutableListOf(
        Collection("Verbos ingles"),
        Collection("Fundamentos de POO"),
        Collection("Patologias"),
        Collection("tipos de notas musicales"),
        Collection("Probando si el textview se puede ajustar y leerse bien cuando es largo")



    )*/

    //val lista = MutableListOf
    //val listaColecciones = mutableListOf<Collection>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvCollections = view.findViewById(R.id.rvCollections)
        //edtBuscar = view.findViewById(R.id.edtSearch)
        Collections.counter = 0



        /*edtBuscar.setFocusableInTouchMode(false);
        edtBuscar.setFocusable(false);
        edtBuscar.setFocusableInTouchMode(true);
        edtBuscar.setFocusable(true);*/
        //TODO recuperar los datos de Firestore
        //obteniendo el email desde las shared preferences
        val sharedPref = activity?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        email = sharedPref?.getString("email", "email vacio").toString()
        Log.i("HomeFragment", "$email")

        if (Collections.collectionsList.isEmpty()) { //si la lista esta vacia, consulta firebase
            getCollections(email)
        } else {
            initRecycler()
        }


    }

    private fun getCollections(email: String) {
        val docRef = db.collection("users").document(email)
        docRef.get().addOnSuccessListener { documentSnapshot ->
            val user = documentSnapshot.toObject<User>()
            Log.i("Home", "${user?.collections}")
            if (user?.collections?.isNotEmpty() == true) {
                user?.collections?.forEach { Collections.collectionsList.add(it) }
                initRecycler()
            } else { //lista vacia
                //mostrar un mensaje o boton para crear una coleccion
                Toast.makeText(activity, "Vaya parece que no tienes colecciones, crea una :)", Toast.LENGTH_LONG).show()
            }

        }
        //se llama si algo salio mal
        docRef.get().addOnFailureListener {
            Toast.makeText(activity,"Vaya, algo ha salido mal :(", Toast.LENGTH_LONG).show()
        }
    }

    private fun initRecycler() {
        rvCollections.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val adapter = CollectionAdapter(Collections.collectionsList, email) //se le pasa la lista
        rvCollections.adapter = adapter
        //adapter.notifyDataSetChanged()
    }

}



