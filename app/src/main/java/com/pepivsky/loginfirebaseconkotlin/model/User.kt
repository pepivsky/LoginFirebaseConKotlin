package com.pepivsky.loginfirebaseconkotlin.model

data class User(val provider: String? = null, val collections: MutableList<Collection>? = null)
