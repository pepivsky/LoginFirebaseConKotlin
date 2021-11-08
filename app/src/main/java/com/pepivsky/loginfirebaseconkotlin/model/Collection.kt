package com.pepivsky.loginfirebaseconkotlin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Collection(val tittle: String = "", val listCard: MutableList<FlashCard> = mutableListOf()) : Parcelable

//object statico
object Collections {
    val collectionsList = mutableListOf<Collection>()
    //contador de respuestas correctas
    var counter = 0
    //total de tarjetas
    var total = 0
}

