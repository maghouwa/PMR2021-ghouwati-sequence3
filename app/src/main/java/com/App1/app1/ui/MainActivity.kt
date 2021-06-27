package com.App1.app1.ui

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.App1.app1.data.source.remote.APIHandler
import com.App1.app1.R
import com.App1.app1.data.model.User
import com.App1.app1.ui.viewmodels.MainActivityViewModel


class MainActivity : AppCompatActivity() {
    private lateinit var mMainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mMainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)

        val backArrowButton = findViewById<ImageView>(R.id.main_back_icon)
        val menuButton = findViewById<ImageView>(R.id.main_menu_icon)

        backArrowButton.setOnClickListener{
            // return to previous activity
            onBackPressed()
        }

        menuButton.setOnClickListener{
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        // finding the button
        val confirmButton = findViewById<Button>(R.id.main_confirm_button)
        val newUserButton = findViewById<Button>(R.id.main_create_user_button)

        // finding the edit text
        val pseudoField = findViewById<EditText>(R.id.pseudo_field)
        val passwordField = findViewById<EditText>(R.id.password_field)

        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)

        val lastUsedPseudo = sharedPreferences.getString("LAST_USED_PSEUDO", null)

        pseudoField.setText(lastUsedPseudo.toString())

        // Setting On Click Listener
        confirmButton.setOnClickListener {
            val pseudo = pseudoField.text.toString()
            val password = passwordField.text.toString()

            savePseudo(pseudo = pseudo)

            val intent = Intent(this, ChoixListActivity::class.java)
            intent.putExtra("pseudo", pseudo)

            val connected = verifReseau()

            if (connected) {
                val baseUrl = sharedPreferences.getString("URL", "http://tomnab.fr/todo-api")
                val apiHandler = APIHandler(baseUrl = baseUrl!!)
                if (apiHandler.authenticate(
                    user = pseudo,
                    password = password
                )) {
                    println("Starting the item update")
                    //applyOfflineModifications(mMainActivityViewModel, pseudo, apiHandler)
                    insertUserToDatabase(pseudo, password, apiHandler)
                    intent.putExtra("baseUrl", apiHandler.baseUrl)
                    intent.putExtra("hashToken", apiHandler.hashToken)
                    intent.putExtra("isConnected", connected)
                    startActivity(intent)
                }

            }
            else {
                val dialog = Dialog(this)
                dialog.setContentView(R.layout.alert_connection_dialogue)
                dialog.setCanceledOnTouchOutside(false)

                dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT)

                val retryWifiCheck = dialog.findViewById<Button>(R.id.retry_wifi_check)
                val proceedAnyway = dialog.findViewById<Button>(R.id.proceed_anyway)

                retryWifiCheck.setOnClickListener {
                    dialog.cancel()
                }

                proceedAnyway.setOnClickListener {
                    intent.putExtra("isConnected", connected)
                    println("connected is $connected just before launching the activity")
                    intent.putExtra("baseUrl", "")
                    intent.putExtra("hashToken", "")
                    val observer = mMainActivityViewModel.getAllCredentials(pseudo = pseudo, password = password)
                    var canConnect = false
                    observer.observe(this, {
                        println(it)
                        if (it.isNotEmpty()) {
                            canConnect = true
                            if (canConnect) {
                                startActivity(intent)
                            }
                            else {
                                Toast.makeText(dialog.context, "this pseudo and password have no match", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }

                dialog.show()
            }
        }

        newUserButton.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.add_new_user_dialog)
            dialog.setCanceledOnTouchOutside(false)

            dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT)

            val newPseudo = dialog.findViewById<EditText>(R.id.add_user_pseudo_field).text
            val newPassword = dialog.findViewById<EditText>(R.id.add_user_password_field).text
            val createNewUserButton = dialog.findViewById<Button>(R.id.add_user_confirm_button)

            createNewUserButton.setOnClickListener {
                val connected = verifReseau()
                if (connected) {
                    val baseUrl = sharedPreferences.getString("URL", "http://tomnab.fr/todo-api")
                    val apiHandler = APIHandler(baseUrl = baseUrl!!)
                    val result = apiHandler.addUser(username = newPseudo.toString(), password = newPassword.toString())
                    println(result)
                    dialog.cancel()
                }
            }

            dialog.show()
        }
    }

    private fun applyOfflineModifications(mMainActivityViewModel: MainActivityViewModel, pseudo: String, apiHandler: APIHandler) {
        val tempObserver = mMainActivityViewModel.getAllItemsToBeUpdatedRemotely(pseudo = pseudo)
        var finished: Boolean? = null
        tempObserver.observe(this, {
            print(it)
            for (item in it) {
                println(item)
                val addItemResponse = apiHandler.checkUncheckItem(
                    idListe = item.remoteId!!,
                    idItem = item.remoteId!!,
                    check = item.fait
                )
                if (addItemResponse!!.success) {
                    item.updated = false
                    mMainActivityViewModel.updateItem(item = item)
                }
            }
            finished = true
        })
        tempObserver.removeObservers(this)
        while (finished == null) {
            Thread.sleep(1)
        }
    }

    private fun insertUserToDatabase(pseudo: String, password: String, apiHandler: APIHandler) {
        val user = User(
            0,
            pseudo = pseudo,
            pass = password,
            hash = apiHandler.hashToken!!
        )
        mMainActivityViewModel.addUser(user = user)
        println("User Added")
        Toast.makeText(this, "User $pseudo added", Toast.LENGTH_SHORT).show()
    }

    private fun savePseudo(pseudo: String) {
        val sharedPreferences = getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.apply {
            putString("LAST_USED_PSEUDO", pseudo)
        }.apply()
    }

    private fun verifReseau(): Boolean {
        // On vérifie si le réseau est disponible,
        // si oui on change le statut du bouton de connexion
        val cnMngr = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val netInfo = cnMngr.activeNetworkInfo
        var sType = "Aucun réseau détecté"
        var bStatut = false
        if (netInfo != null) {
            val netState = netInfo.state
            if (netState.compareTo(NetworkInfo.State.CONNECTED) == 0) {
                bStatut = true
                val netType = netInfo.type
                when (netType) {
                    ConnectivityManager.TYPE_MOBILE -> sType = "Réseau mobile détecté"
                    ConnectivityManager.TYPE_WIFI -> sType = "Réseau wifi détecté"
                }
                Toast.makeText(this, "Connection available", Toast.LENGTH_LONG).show()
            }
        }
        return bStatut
    }
}
