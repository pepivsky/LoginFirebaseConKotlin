package com.pepivsky.loginfirebaseconkotlin

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthActivity : AppCompatActivity() {
    private lateinit var btnRegistro: Button
    private lateinit var btnLogin: Button
    private lateinit var btnGooogleSign: Button
    private lateinit var edtEmal: EditText
    private lateinit var edtPassword: EditText
    private lateinit var authLayout: ConstraintLayout

    private val  GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        btnRegistro = findViewById(R.id.btnRegistro)
        btnLogin = findViewById(R.id.btnLogin)
        edtEmal = findViewById(R.id.edtEmail)
        edtPassword = findViewById(R.id.edtPassword)
        authLayout = findViewById(R.id.authLayout)
        btnGooogleSign = findViewById(R.id.btnGoogleSign)


        //setup login
        setup()
        //comprobar si existe una sesion activa
        session()
    }

    //hace visible la pantalla cada que se muestra nuevamente
    override fun onStart() {
        super.onStart()
        authLayout.visibility = View.VISIBLE
    }

    private fun session() { //recuperar la sesion del usuario si es que ya se ha logueado anteriormente
        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE) //aceso al fichero para recuperar la sesion del ususario
        val email = prefs.getString("email", null) //si no hay email es null
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null) {
            //ocultar el layout si existe una sesion
            authLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }


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
                        showHome(it.result?.user?.email ?: "no hay correo", ProviderType.EMAil)
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
                        showHome(it.result?.user?.email ?: "no hay correo", ProviderType.EMAil)
                    } else {
                        showAlert()
                    }
                }


            }
        }

        //logueo con cuenta de google
        btnGooogleSign.setOnClickListener {
            //configuracion de la autenticacion
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail() //solicita el email desde su cuenta
                .build()

            //cliente de autenticacion
            val googleClient = GoogleSignIn.getClient(this, googleConf) //le pasamos la configuracion
            googleClient.signOut() //cada vez que se presiona realiza antes el kogout de la cuenta que esta autenticada en ese momento (util si hay mas de una cuenta asociada al dispositivo)
            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN) //identificador para recoger la respuesta de la autenticacion

        }

    }


    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email) //pasando datos con el intent
            putExtra("provider", provider.name)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            //recuperar la respuesta
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                //si esto sale bioen ya tendremos el email
                if (account != null) {//si la cuenta no es nula recuperamos su credencial
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { //enviar la credencial a firebase y se agrega un addCompleteListener para manejar cuando la operacion se complete

                        if (it.isSuccessful) {//comprobar que la operacion fue exitosa, si es asi vamos al home
                            showHome(account.email ?: "email vacio", ProviderType.GOOGLE)
                        } else {
                            showAlert()
                        }
                    }
                }
            } catch (e: ApiException) {
                showAlert() //si hay un error se muestra el alert
            }



        }
    }
}