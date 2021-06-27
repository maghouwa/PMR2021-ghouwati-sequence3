package com.App1.app1.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "profiles")
data class User(
    val localId: Int? = null,
    @SerializedName("id")
    var remoteId: Int? = null,
    @PrimaryKey
    @SerializedName("pseudo")
    val pseudo: String,
    @SerializedName("pass")
    val pass: String,
    @SerializedName("hash")
    val hash: String
)