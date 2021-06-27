package com.App1.app1.data.model

import com.google.gson.annotations.SerializedName

data class AddListeResponse(
    @SerializedName("list")
    val maListe: Liste,
    @SerializedName("success")
    val success: Boolean
)
