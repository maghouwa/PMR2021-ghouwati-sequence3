package com.App1.app1.data.model

import com.google.gson.annotations.SerializedName

data class ItemResponse(
    @SerializedName("items")
    val mesItems: MutableList<Item>
)