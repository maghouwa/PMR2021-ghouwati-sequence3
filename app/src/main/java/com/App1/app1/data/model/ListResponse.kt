package com.App1.app1.data.model

import com.google.gson.annotations.SerializedName

data class ListResponse(
    @SerializedName("lists")
    val mesListes: List<Liste>
)