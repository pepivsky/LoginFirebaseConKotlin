package com.pepivsky.loginfirebaseconkotlin.adapter



import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.pepivsky.loginfirebaseconkotlin.model.Collection

import com.pepivsky.loginfirebaseconkotlin.R
import com.pepivsky.loginfirebaseconkotlin.view.QuizzActivity

class CollectionAdapter(val collections: List<Collection>): RecyclerView.Adapter<CollectionAdapter.CollectionHolder>() {

    //lateinit var clickListener: (Collection) -> Unit


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return CollectionHolder(
            layoutInflater.inflate(
                R.layout.item_collection_card,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CollectionHolder, position: Int) {
        val collection = holder.render(collections[position])
        val activity = holder.itemView.context as Activity
        //todo pasar el objeto al darle tap
        holder.view.setOnClickListener {
            val intent = Intent(activity, QuizzActivity::class.java )
            intent.putExtra("position", position)
            intent.putParcelableArrayListExtra("lsitaTarjetas", ArrayList(collections[position].listCard))
            Log.i("Lista enviada", "${ArrayList(collections[position].listCard)}")
            Log.i("pos", " $position")
            activity.startActivity(intent)

        }


    }

    override fun getItemCount(): Int {
        return collections.size
    }

    class CollectionHolder(val view: View): RecyclerView.ViewHolder(view) {
        lateinit var tvTitle: TextView

        lateinit var itemCard: CardView


        fun render(collection: Collection) {
            tvTitle = view.findViewById(R.id.tvTittle)
            //itemCard = view.findViewById(R.id.item_collection_card)

            tvTitle.text = collection.tittle

        }
    }
}