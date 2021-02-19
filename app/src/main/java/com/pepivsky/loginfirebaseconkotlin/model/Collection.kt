package com.pepivsky.loginfirebaseconkotlin.model


data class Collection(val tittle: String, val listCard: MutableList<FlashCard>)

//object statico
object Collections {
    val collectionsList = mutableListOf<Collection>()
    /*fun obtenerRespuestas(index: Int): List<FlashCard> {
        val listaRespuestas = mutableListOf<FlashCard>()

       *//* val flashCard = collectionsList.filter{ (indice, calificacion) -> indice != index }[0]
        listaRespuestas = Collection.filter{ calificacion -> calificacion != index}


        return*//*
    }*/
}

