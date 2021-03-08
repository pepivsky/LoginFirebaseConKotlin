package com.pepivsky.loginfirebaseconkotlin.model


data class Collection(val tittle: String? = null, val listCard: MutableList<FlashCard>? = null)

//object statico
object Collections {
    val collectionsList = mutableListOf<Collection>()
}

