package com.App1.app1.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.App1.app1.data.ToDoRepository
import com.App1.app1.data.model.Item
import com.App1.app1.data.source.local.database.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ShowListViewHolder (application: Application): AndroidViewModel(application) {
    val readAllItems: LiveData<List<Item>>
    private val repository: ToDoRepository

    init {
        val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
        repository = ToDoRepository(toDoDao)
        readAllItems = repository.readAllItems
    }

    fun addItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addItem(item = item)
        }
    }

    fun addItems(items: List<Item>, listeLocalId: Int, listeRemoteId: Int, userPseudo: String) {
        for (item in items) {
            item.localId = 0
            item.listeLocalId = listeLocalId
            item.listeRemoteId = listeRemoteId
            item.userPseudo = userPseudo
            addItem(item)
        }
    }

    fun getListeItems(listeRemoteId: Int, userPseudo: String): LiveData<List<Item>> {
        return repository.readListeItems(listeRemoteId = listeRemoteId, userPseudo = userPseudo)
    }

    fun deleteItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(item = item)
        }
    }

    fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(item = item)
        }
    }
}