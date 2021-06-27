package com.App1.app1.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.App1.app1.data.ToDoRepository
import com.App1.app1.data.model.Item
import com.App1.app1.data.model.User
import com.App1.app1.data.source.local.database.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application): AndroidViewModel(application) {
    private val readAllUsers: LiveData<List<User>>
    private val repository: ToDoRepository

    init {
        val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
        repository = ToDoRepository(toDoDao)
        readAllUsers = repository.readAllUsers
    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user = user)
        }
    }

    fun getAllCredentials(pseudo: String, password: String): LiveData<List<User>> {
        return repository.getAllCredentials(pseudo = pseudo, password = password)
    }

    fun getAllItemsToBeUpdatedRemotely(pseudo: String): LiveData<List<Item>> {
        return repository.getAllItemsToBeUpdatedRemotely(pseudo = pseudo)
    }

    fun updateItem(item: Item) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(item = item)
        }
    }
}