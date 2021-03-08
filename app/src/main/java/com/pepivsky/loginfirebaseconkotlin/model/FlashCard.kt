package com.pepivsky.loginfirebaseconkotlin.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlashCard(var concept: String? = null, var definition: String? = null ) : Parcelable
