package com.pepivsky.loginfirebaseconkotlin.adapter



import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.pepivsky.loginfirebaseconkotlin.model.Collection

import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.databinding.ItemCollectionCardBinding
import com.pepivsky.loginfirebaseconkotlin.model.Collections
import com.pepivsky.loginfirebaseconkotlin.view.CreateCollectionFragment
import com.pepivsky.loginfirebaseconkotlin.view.QuizzActivity

class CollectionAdapter(val collections: List<Collection>, val email: String): RecyclerView.Adapter<CollectionAdapter.CollectionHolder>() {

    //lateinit var clickListener: (Collection) -> Unit
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        val binding = ItemCollectionCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CollectionHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        val collection = holder.render(collections[position])
        val activity = holder.itemView.context as Activity
        //todo pasar el objeto al darle tap
        holder.binding.itemCollectionCard.setOnClickListener {
            val intent = Intent(activity, QuizzActivity::class.java )
            intent.putExtra("position", position)
            intent.putParcelableArrayListExtra("lsitaTarjetas", ArrayList(collections[position].listCard))
            Log.i("Lista enviada", "${ArrayList(collections[position].listCard)}")
            Log.i("pos", " $position")
            activity.startActivity(intent)

        }

        holder.binding.btnDeleteCollection.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("¿Estás seguro que deseas eliminar esta coleccion?")
            builder.setMessage("Se eliminara definitivamente")

            builder.setPositiveButton(R.string.dialog_exit) { dialog, which ->
                CreateCollectionFragment.deleteCollectionToBD(collections[position], email) // elimina de firebase
                Collections.collectionsList.remove(collections[position]) // elimina de la lista estatica
                notifyDataSetChanged() // notificar que los datos han cambiado
            }

            builder.setNegativeButton(R.string.dialog_cancel) { dialog, which ->
                dialog.dismiss()
            }
            builder.show()

        }




    }

    override fun getItemCount() = collections.size

    class CollectionHolder(val binding: ItemCollectionCardBinding): RecyclerView.ViewHolder(binding.root) {
        fun render(collection: Collection) {
            binding.tvTittle.text = collection.tittle
        }
    }
}