package com.App1.app1.data.source.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.App1.app1.data.model.Item
import com.App1.app1.data.model.Liste
import com.App1.app1.data.model.User

@Dao
interface ToDoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addListe(liste: Liste)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addItem(item: Item)

    @Update
    suspend fun updateItem(item: Item)

    @Query("SELECT * FROM profiles WHERE pseudo=:pseudo AND pass=:password")
    fun getAllCredentials(pseudo: String, password: String): LiveData<List<User>>

    @Query("SELECT * FROM profiles ORDER BY pseudo ASC")
    fun readAllUsers(): LiveData<List<User>>

    @Query("SELECT * FROM listes ORDER BY localId ASC")
    fun readAllListes(): LiveData<List<Liste>>

    @Query("SELECT * FROM listes WHERE userPseudo=:userPseudo ORDER BY localId ASC")
    fun getListesOfUser(userPseudo: String): LiveData<List<Liste>>

    @Query("SELECT * FROM items ORDER BY localId ASC")
    fun readAllItems(): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE userPseudo=:userPseudo AND listeRemoteId=:listeRemoteId ORDER BY localId ASC")
    fun getItemsOfListe(listeRemoteId: Int, userPseudo: String): LiveData<List<Item>>

    @Query("SELECT * FROM items WHERE userPseudo=:userPseudo AND updated=1 ORDER BY localId ASC")
    fun getAllItemsToUpdateRemotely(userPseudo: String): LiveData<List<Item>>

    @Delete
    suspend fun deleteListe(liste: Liste)

    @Delete
    suspend fun deleteItem(item: Item)
}
