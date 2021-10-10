package com.pepivsky.loginfirebaseconkotlin.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.facebook.login.LoginManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.databinding.ActivityHomeBinding
import com.pepivsky.loginfirebaseconkotlin.model.Collection
import com.pepivsky.loginfirebaseconkotlin.model.Collections

enum class ProviderType {
    EMAil,
    GOOGLE,
    FACEBOOK
}
class HomeActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding

    private var provider: String? = ""
    private var email: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()

        //obtener datos del bundle
        intent.extras.let { bundle ->
            email = bundle?.getString("email")
            provider = bundle?.getString("provider")
        }

        Log.i("bundle", "email $email provider $provider")
        saveUserSession(email, provider) //guardar la sesion del usuario

        Log.i("Entrando Oncreate", "Entrando en oncreate")
       /* if (provider != null && email != null) {//crea el usuario en firestore
            createUserInFirestore(email, provider)
            Log.i("OnCreate" ,"creando usuario")
        }*/
    }

    private fun initUI() {
        setUpNavigation()
        title = "Inicio"
    }


    private fun saveUserSession(email: String?, provider: String?) {
        //guardar la sesion del usuario para que una vez que se logee no vuelva a pedir los datos de autenticacion

        val prefs = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit() //aceso al fichero de modo privado
        //colocando datos en el sharedPreferences
        prefs.putString("email", email)
        prefs.putString("provider", provider)
        prefs.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.action_logOut -> {
                //borrado de datos
                val prefs = getSharedPreferences(
                    getString(R.string.prefs_file),
                    Context.MODE_PRIVATE
                ).edit() //aceso al fichero de modo privado
                prefs.clear()
                prefs.apply()

                //deslogueo de FaceBook
                if (provider == ProviderType.FACEBOOK.name) {
                    LoginManager.getInstance().logOut() //cerrando sesion
                }

                FirebaseAuth.getInstance().signOut() //hacer logout de firebase
                //onBackPressed() //volver a la pantalla anterior
                //limpiar la lista al cerrar sesion
                Collections.collectionsList.clear()
                // ir al activity Auth para iniciar con otra cuenta
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                this.finish() // finalizar la activty actual
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    fun setUpNavigation() { //funcion que configura el bottom navigation para funcionar con el nav graph (el recurso de menu debe tener los mismos ids del fragment id en el nav graph para que funcione)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigation,
            navHostFragment.navController
        )
    }

    //evita que se pueda regresar al login
    override fun onBackPressed() {
        moveTaskToBack(true)
    }
}