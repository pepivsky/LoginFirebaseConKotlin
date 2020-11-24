package com.pepivsky.loginfirebaseconkotlin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth

class AuthActivity : AppCompatActivity() {
    private lateinit var btnRegistro: Button
    private lateinit var btnLogin: Button
    private lateinit var edtEmal: EditText
    private lateinit var edtPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        btnRegistro = findViewById(R.id.btnRegistro)
        btnLogin = findViewById(R.id.btnLogin)
        edtEmal = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)


        //setup login
        setup()
    }

    private fun setup() {
        //cambiar el titulo del activity
        title = "Autenticacion"

        //registrar usuario
        btnRegistro.setOnClickListener {
            //comprobar que se han introducido email y password
            if (edtEmal.text.isNotEmpty() && edtPassword.text.isNotEmpty()) {
                //crear el usuario con email y password en Firebase  y le pasamos el email y password
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    edtEmal.text.toString(),
                    edtPassword.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) {//comprobar que la operacion fue exitosa, si es asi vamos al home
                        showHome(it.result?.user?.email ?: "no hay correo", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }


            }
        }


        //Loguear usuario
        btnLogin.setOnClickListener {
            //comprobar que se han introducido email y password
            if (edtEmal.text.isNotEmpty() && edtPassword.text.isNotEmpty()) {
                //logear el usuario con email y password en Firebase  y le pasamos el email y password
                FirebaseAuth.getInstance().signInWithEmailAndPassword( //sign in
                    edtEmal.text.toString(),
                    edtPassword.text.toString()
                ).addOnCompleteListener {

                    if (it.isSuccessful) {//comprobar que la operacion fue exitosa, si es asi vamos al home
                        showHome(it.result?.user?.email ?: "no hay correo", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }


            }
        }

    }


    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email) //pasando datos con el intent
            putExtra("provider", provider)
        }
        startActivity(homeIntent)
    }

    private fun showAlert() {
        //muestra un alert
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }
}