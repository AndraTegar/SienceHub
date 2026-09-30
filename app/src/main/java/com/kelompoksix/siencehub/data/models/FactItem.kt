package com.kelompoksix.siencehub.data.models

import com.google.gson.annotations.SerializedName

data class FactItem(
    @SerializedName("fact") val fact: String
)
