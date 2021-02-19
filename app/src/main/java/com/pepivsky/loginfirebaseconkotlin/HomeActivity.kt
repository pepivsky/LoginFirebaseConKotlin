package com.pepivsky.loginfirebaseconkotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.pepivsky.loginfirebaseconkotlin.model.City
import com.pepivsky.loginfirebaseconkotlin.model.Collection
import com.pepivsky.loginfirebaseconkotlin.model.FlashCard
import com.pepivsky.loginfirebaseconkotlin.model.User

enum class ProviderType {
    EMAil,
    GOOGLE,
    FACEBOOK
}
class HomeActivity : AppCompatActivity() {
    private lateinit var tvEmail: TextView
    private lateinit var tvProvider: TextView
    private lateinit var btnLogOut: Button


    private lateinit var btnGuardar: Button


    private val db = FirebaseFirestore.getInstance()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvEmail = findViewById(R.id.tvEmail)
        tvProvider = findViewById(R.id.tvProveedor)
        btnLogOut = findViewById(R.id.btnLogout)
        btnGuardar = findViewById(R.id.btnGuardar)

        //obtener datos del bundle
        val bundle = intent.extras
        val email = bundle!!.getString("email")
        val provider = bundle?.getString("provider")



        Log.i("bundle", "email $email provider $provider")

        //setup
        setup(email ?: "vacio", provider ?: "vacio") //pasarle los datos

        /*
        guardar la sesion del usuario para que una vez que se logee no vuelva a pedir los datos de autenticacion
         */
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit() //aceso al fichero de modo privado
        //colocando datos en el sharedPreferences
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()
    }

    private fun setup(email: String, provider: String) {
        title = "Inicio"
        //seteando los valores
        tvEmail.text = email
        tvProvider.text = provider

        //logout
        btnLogOut.setOnClickListener {
            //borrado de datos
            val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit() //aceso al fichero de modo privado
            prefs.clear()
            prefs.apply()

            //deslogueo de FaceBook
            if (provider == ProviderType.FACEBOOK.name) {
                LoginManager.getInstance().logOut() //cerrando sesion
            }

            FirebaseAuth.getInstance().signOut() //hacer logout de firebase
            onBackPressed() //volver a la pantalla anterior
        }

        btnGuardar.setOnClickListener {
            //crear objeto dummy
            val collection1 = Collection("palabras nuevas",
                mutableListOf(
                    FlashCard("car", "car"),
                    FlashCard("cat", "gato"),
                    FlashCard("dog", "perro")
                ))

            val collection2 = Collection("test",
                mutableListOf(
                    FlashCard("road", "calle"),
                    FlashCard("rat", "rata"),
                    FlashCard("green", "verde")
                ))
            //TODO comprobar primero si el usuario existe en firestore antes de crearlo
            createUserInFirestore(email, provider) //crea el usuario en firestore, si existe, reemplaza el contenido
            addCollectionToBD(collection1, email) //agrega la coleccion a la base de datos

            //agrega una nueva collecion a la lista de colecciones del usuario





        }

    }

    private fun createUserInFirestore(email: String, provider: String) {
        val user = User(provider,mutableListOf<Collection>())
        db.collection("users").document(email).set(user)
    }

    private fun addCollectionToBD(collection: Collection, email: String) {
        val refUser = db.collection("users").document(email)
        refUser.update("collections", FieldValue.arrayUnion(collection))
    }


}