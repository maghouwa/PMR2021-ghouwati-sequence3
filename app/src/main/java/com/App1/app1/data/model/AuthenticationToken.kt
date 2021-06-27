package com.App1.app1.data.model

import com.google.gson.annotations.SerializedName

data class AuthenticationToken(
    @SerializedName("version")
    val version: String,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("status")
    val status: String,
    @SerializedName("hash")
    val token: String
    )
