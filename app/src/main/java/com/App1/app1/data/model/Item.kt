package com.App1.app1.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "items", indices = [Index(value=["apiId"], unique = true)])
data class Item(
    @PrimaryKey(autoGenerate = true)
    var localId: Int,
    @ColumnInfo(name = "apiId")
    @SerializedName("id")
    var remoteId: Int? = null,
    @SerializedName("label")
    val description: String,
    @SerializedName("checked")
    var fait: Int,
    @SerializedName("url")
    var url: String? = null,
    var listeLocalId: Int,
    var listeRemoteId: Int,
    var userPseudo: String,
    var updated: Boolean = false
)