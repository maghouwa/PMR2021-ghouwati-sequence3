package com.App1.app1.data.model

import com.google.gson.annotations.SerializedName

data class AddItemResponse(
    @SerializedName("item")
    val monItem: Item,
    @SerializedName("success")
    val success: Boolean
)
