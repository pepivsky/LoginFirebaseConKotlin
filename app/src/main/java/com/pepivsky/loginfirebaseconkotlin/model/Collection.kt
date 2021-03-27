package com.pepivsky.loginfirebaseconkotlin.model


data class Collection(val tittle: String? = null, val listCard: MutableList<FlashCard>? = null)

//object statico
object Collections {
    val collectionsList = mutableListOf<Collection>()
    //contador de respuestas correctas
    var counter = 0
    //total de tarjetas
    var total = 0
}

