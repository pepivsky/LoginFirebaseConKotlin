package com.pepivsky.loginfirebaseconkotlin.model

data class User(val provider: String = "", val collections: MutableList<Collection> = mutableListOf())
