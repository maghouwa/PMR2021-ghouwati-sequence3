package com.App1.app1.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.App1.app1.data.source.remote.APIHandler
import com.App1.app1.ui.adapter.ItemAdapter
import com.App1.app1.R
import com.App1.app1.ui.viewmodels.ShowListViewHolder

class ShowListActivity : AppCompatActivity() {
    private lateinit var mShowListViewHolder: ShowListViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        mShowListViewHolder = ViewModelProvider(this).get(ShowListViewHolder::class.java)

        val bundle = intent.extras
        val pseudo = bundle?.get("pseudo").toString()
        val baseUrl = bundle?.get("baseUrl").toString()
        val hashToken = bundle?.get("hashToken").toString()
        val listeRemoteId: Int? = bundle?.getInt("listeRemoteId")
        val listelocalId: Int? = bundle?.getInt("listelocalId")
        val isConnected = bundle?.getBoolean("isConnected", false)

        var apiHandler: APIHandler? = null
        if (isConnected == true){
            apiHandler = APIHandler(baseUrl = baseUrl, hashToken = hashToken)
            val fetchedItems = apiHandler.getListeItems(idListe = listeRemoteId!!)!!.mesItems
            mShowListViewHolder.addItems(
                items = fetchedItems,
                listeLocalId = listelocalId!!,
                listeRemoteId = listeRemoteId,
                userPseudo = pseudo
            )
        }
        val newItemField = findViewById<EditText>(R.id.show_field)
        val confirmButton = findViewById<Button>(R.id.show_list_confirm_button)

        val backButton = findViewById<ImageView>(R.id.show_back_icon)


        val recyclerView = findViewById<RecyclerView>(R.id.item_list)
        val layoutManager = LinearLayoutManager(this)
        var adapter = ItemAdapter(
            listeRemoteId!!,
            listelocalId!!,
            isConnected,
            apiHandler,
            ArrayList(),
            mShowListViewHolder
        )
        val observer = mShowListViewHolder.getListeItems(
            listeRemoteId = listeRemoteId,
            userPseudo = pseudo
        )
        observer.observe(this, {
            println(it)
            adapter.setData(it)
        })
        // adapter.addData(apiHandler.getListeItems(idListe = listeRemoteId)!!.mesItems)

        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter

        backButton.setOnClickListener{
            onBackPressed()
        }

        confirmButton.setOnClickListener {
            val newItemName = newItemField.text.toString()
            if (isConnected == true) {
                val addItemResponse =
                    apiHandler!!.addItem(label = newItemName, idListe = listeRemoteId)
                val newItem = addItemResponse!!.monItem
                newItem.localId = 0
                newItem.userPseudo = pseudo
                newItem.listeLocalId = listelocalId
                newItem.listeRemoteId = listeRemoteId
                println(newItem)
                mShowListViewHolder.addItem(newItem)
            }
            else {
                Toast.makeText(this,"Unavailable offline", Toast.LENGTH_SHORT).show()
            }
        }
    }
}