package com.App1.app1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.App1.app1.data.source.remote.APIHandler
import com.App1.app1.ui.adapter.ListeAdapter
import com.App1.app1.R
import com.App1.app1.data.model.Liste
import com.App1.app1.ui.viewmodels.ChoixListViewModel
import com.App1.app1.ui.viewmodels.MainActivityViewModel

class ChoixListActivity : AppCompatActivity() {
    private lateinit var mChoixListViewModel: ChoixListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        mChoixListViewModel = ViewModelProvider(this).get(ChoixListViewModel::class.java)

        val bundle = intent.extras
        val pseudo = bundle?.get("pseudo").toString()
        val baseUrl = bundle?.get("baseUrl").toString()
        val hashToken = bundle?.get("hashToken").toString()
        val isConnected= bundle?.getBoolean("isConnected", false)

        var apiHandler: APIHandler? = null
        if (isConnected == true) {
            apiHandler = APIHandler(baseUrl=baseUrl, hashToken=hashToken)
            val fetchedListes = apiHandler.getUserLists()!!.mesListes
            mChoixListViewModel.addListes(fetchedListes, pseudo)
        }

        val newListField = findViewById<EditText>(R.id.editText2)
        val confirmButton = findViewById<Button>(R.id.choix_list_confirm_button)

        val backButton = findViewById<ImageView>(R.id.choix_back_icon)

        val recyclerView = findViewById<RecyclerView>(R.id.liste_list)
        val layoutManager = LinearLayoutManager(this)

        var adapter = ListeAdapter(pseudo, isConnected, apiHandler, ArrayList(), mChoixListViewModel)

        val observer = mChoixListViewModel.getUserListes(userPseudo = pseudo)
        observer.observe(this, Observer {
            println(it)
            adapter.setData(it)
        })
        // adapter.addData(fetchedListes)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        backButton.setOnClickListener{
            onBackPressed()
        }

        confirmButton.setOnClickListener {
            val newListName = newListField.text.toString()
            if (isConnected == true) {
                val addListeResponse = apiHandler!!.addListe(label = newListName)
                val newListe = addListeResponse!!.maListe
                newListe.localId = 0
                newListe.userPseudo = pseudo
                mChoixListViewModel.addListe(newListe)
            }
            else {
                Toast.makeText(this,"Unavailable offline", Toast.LENGTH_SHORT).show()
            }
        }
    }

}