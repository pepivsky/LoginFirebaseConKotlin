package com.pepivsky.loginfirebaseconkotlin

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    EMAil,
    GOOGLE
}
class HomeActivity : AppCompatActivity() {
    private lateinit var tvEmail: TextView
    private lateinit var tvProvider: TextView
    private lateinit var btnLogOut: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        tvEmail = findViewById(R.id.tvEmail)
        tvProvider = findViewById(R.id.tvProveedor)
        btnLogOut = findViewById(R.id.btnLogout)

        //obtener datos del bundle
        val bundle = intent.extras
        val email = bundle?.getString("email")
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

            FirebaseAuth.getInstance().signOut() //hacer logout de firebase
            onBackPressed() //volver a la pantalla anterior
        }

    }
}