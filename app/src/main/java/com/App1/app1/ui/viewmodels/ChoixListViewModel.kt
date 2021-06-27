package com.App1.app1.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.App1.app1.data.ToDoRepository
import com.App1.app1.data.model.Liste
import com.App1.app1.data.source.local.database.ToDoDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChoixListViewModel(application: Application): AndroidViewModel(application) {
    private val readAllListes: LiveData<List<Liste>>
    private val repository: ToDoRepository

    init {
        val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
        repository = ToDoRepository(toDoDao)
        readAllListes = repository.readAllListes
    }

    fun addListe(liste: Liste) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addListe(liste=liste)
        }
    }

    fun addListes(listes: List<Liste>, userPseudo: String) {
        for (liste in listes) {
            liste.localId = 0
            liste.userPseudo = userPseudo
            addListe(liste)
        }
    }

    fun getUserListes(userPseudo: String): LiveData<List<Liste>> {
        return repository.readUserListes(userPseudo = userPseudo)
    }

    fun deleteListe(liste: Liste) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteListe(liste = liste)
        }
    }
}