package com.pepivsky.loginfirebaseconkotlin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth

enum class ProviderType {
    BASIC
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

        //setup
        setup(email ?: "vacio", provider ?: "vacio") //pasarle los datos
    }

    private fun setup(email: String, provider: String) {
        title = "Inicio"
        //seteando los valores
        tvEmail.text = email
        tvProvider.text = provider

        //logout
        btnLogOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut() //hacer logout de firebase
            onBackPressed() //volver a la pantalla anterior
        }

    }
}