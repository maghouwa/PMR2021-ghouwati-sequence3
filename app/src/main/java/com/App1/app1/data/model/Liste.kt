package com.App1.app1.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "listes", indices = [Index(value=["apiId"], unique = true)])
data class Liste(
    @PrimaryKey(autoGenerate = true)
    var localId: Int,
    @ColumnInfo(name = "apiId")
    @SerializedName("id")
    val remoteId: Int? = null,
    @SerializedName("label")
    val titreListeToDo: String,
    var userPseudo: String
)