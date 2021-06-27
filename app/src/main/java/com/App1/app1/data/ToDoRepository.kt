package com.App1.app1.data

import androidx.lifecycle.LiveData
import com.App1.app1.data.model.Item
import com.App1.app1.data.model.User
import com.App1.app1.data.model.Liste
import com.App1.app1.data.source.local.database.ToDoDao

class ToDoRepository(private val toDoDao: ToDoDao) {

    val readAllUsers: LiveData<List<User>> = toDoDao.readAllUsers()
    val readAllListes: LiveData<List<Liste>> = toDoDao.readAllListes()
    val readAllItems: LiveData<List<Item>> = toDoDao.readAllItems()

    suspend fun addUser(user: User) {
        toDoDao.addUser(user = user)
    }

    suspend fun addListe(liste: Liste) {
        toDoDao.addListe(liste = liste)
    }

    suspend fun addItem(item: Item) {
        toDoDao.addItem(item = item)
    }

    fun readUserListes(userPseudo: String): LiveData<List<Liste>> {
        return toDoDao.getListesOfUser(userPseudo = userPseudo)
    }

    fun readListeItems(listeRemoteId: Int, userPseudo: String): LiveData<List<Item>> {
        return toDoDao.getItemsOfListe(listeRemoteId = listeRemoteId, userPseudo = userPseudo)
    }

    fun getAllCredentials(pseudo: String, password: String): LiveData<List<User>> {
        return toDoDao.getAllCredentials(pseudo = pseudo, password = password)
    }

    fun getAllItemsToBeUpdatedRemotely(pseudo: String): LiveData<List<Item>> {
        return toDoDao.getAllItemsToUpdateRemotely(userPseudo = pseudo)
    }

    suspend fun deleteListe(liste: Liste) {
        toDoDao.deleteListe(liste = liste)
    }

    suspend fun deleteItem(item: Item) {
        toDoDao.deleteItem(item = item)
    }

    suspend fun updateItem(item: Item) {
        toDoDao.updateItem(item = item)
    }

}