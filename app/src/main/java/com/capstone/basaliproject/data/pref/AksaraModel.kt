package com.capstone.basaliproject.data.pref

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AksaraModel(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable