package com.App1.app1.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.App1.app1.data.source.remote.APIHandler
import com.App1.app1.R
import com.App1.app1.data.model.Liste
import com.App1.app1.ui.ShowListActivity
import com.App1.app1.ui.viewmodels.ChoixListViewModel

class ListeAdapter(val pseudo: String, val isConnected: Boolean?, val apiHandler: APIHandler?,
                   var listes: MutableList<Liste> = ArrayList(), var mChoixListViewModel: ChoixListViewModel) : RecyclerView.Adapter<ListeAdapter.ListeViewHolder>() {

    inner class ListeViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        private val titreListeView = view?.findViewById(R.id.liste_name) as? TextView
        private val deleteButtonView = view?.findViewById(R.id.liste_remove) as? ImageView
        private var listeRemoteId: Int = 0
        private var listeLocalId: Int = 0

        fun bindListe(liste: Liste) {
            titreListeView?.text = liste.titreListeToDo
            listeRemoteId = liste.remoteId!!
            listeLocalId = liste.localId
        }

        init {
            titreListeView?.setOnClickListener {
                val listeIntent = Intent(view?.context, ShowListActivity::class.java)
                listeIntent.putExtra("listName", titreListeView.text.toString())
                listeIntent.putExtra("listeRemoteId", listeRemoteId)
                listeIntent.putExtra("listeLocalId", listeLocalId)
                listeIntent.putExtra("pseudo", pseudo)
                listeIntent.putExtra("isConnected", isConnected)
                if (isConnected == true) {
                    listeIntent.putExtra("baseUrl", apiHandler!!.baseUrl)
                    listeIntent.putExtra("hashToken", apiHandler.hashToken)
                }
                else {
                    listeIntent.putExtra("baseUrl", "")
                    listeIntent.putExtra("hashToken", "")
                }
                view?.context?.startActivity(listeIntent)
            }
        }

        fun getDeleteButton(): ImageView? {
            return deleteButtonView
        }
    }

    override fun getItemCount(): Int {
        return listes.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListeViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)?.inflate(R.layout.liste, parent, false)

        return ListeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListeViewHolder, position: Int) {
        holder.bindListe(listes[position])
        holder.getDeleteButton()?.setOnClickListener {
            if (isConnected == true) {
                if (listes[position].remoteId?.let { it1 -> apiHandler!!.deleteListe(listeId = it1) } == true) {
                    val listeToDelete = listes[position]
                    mChoixListViewModel.deleteListe(liste = listeToDelete)
                    /*listes.removeAt(index = position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, listes.size)*/
                }
            }
        }
    }

    fun addData(newListe: List<Liste>) {
        listes.addAll(newListe)
        notifyItemChanged(listes.size)
    }

    fun setData(newListes: List<Liste>) {
        listes = ArrayList()
        listes.addAll(newListes)
        notifyDataSetChanged()
    }
}