package com.App1.app1.ui.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.App1.app1.data.source.remote.APIHandler
import com.App1.app1.R
import com.App1.app1.data.model.Item
import com.App1.app1.ui.viewmodels.ShowListViewHolder

class ItemAdapter(
    val listeRemoteId: Int,
    val listeLocalId: Int,
    val isConnected: Boolean?,
    val apiHandler: APIHandler?,
    var items: MutableList<Item> = ArrayList(),
    var mShowListViewHolder: ShowListViewHolder
): RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(view: View?): RecyclerView.ViewHolder(view!!) {
        private val descriptionView = view?.findViewById(R.id.item_description) as? TextView
        private val faitCheckBox = view?.findViewById(R.id.item_completed) as? CheckBox
        private val deleteButtonView = view?.findViewById(R.id.item_remove) as? ImageView
        private var itemId: Int = 0

        private fun intToBoolean(i: Int): Boolean {
            return i != 0
        }

        fun bindItem(item: Item) {
            descriptionView?.text = item.description
            faitCheckBox?.isChecked = intToBoolean(item.fait)
            itemId = item.remoteId!!
        }

        fun getDeleteButton(): ImageView? {
            return deleteButtonView
        }

        fun getFaitCheckBox(): CheckBox? {
            return faitCheckBox
        }
    }
    override fun getItemCount(): Int {
        return items.size
    }

    private fun booleanToInt(b: Boolean): Int {
        return if (b) 1
        else 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val context = parent.context
        val view = LayoutInflater.from(context)?.inflate(R.layout.item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bindItem(items[position])
        holder.getDeleteButton()?.setOnClickListener {
            if (isConnected == true){
                if (items[position].remoteId?.let { it1 ->
                        apiHandler!!.deleteItem(
                            idListe = listeRemoteId,
                            idItem = it1
                        )
                    } == true) {
                    val itemToDelete = items[position]
                    mShowListViewHolder.deleteItem(item = itemToDelete)
                    /*items.removeAt(index = position)
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, items.size)*/
                }
            }
        }
        holder.getFaitCheckBox()?.setOnCheckedChangeListener { _, isChecked ->
            items[position].fait = booleanToInt(isChecked)
            println(booleanToInt(isChecked).toString())
            if (isConnected == true){
                val result = items[position].remoteId?.let {
                    apiHandler!!.checkUncheckItem(
                        idListe = listeRemoteId, idItem = it,
                        check = booleanToInt(isChecked)
                    )
                }
                println(result)
                if (result!!.success) {
                    val itemToUpdate = items[position]
                    mShowListViewHolder.updateItem(item = itemToUpdate)
                }
            }
            else {
                val itemToUpdate = items[position]
                println(itemToUpdate)
                itemToUpdate.updated = !itemToUpdate.updated
                println(itemToUpdate)
                mShowListViewHolder.updateItem(item = itemToUpdate)
            }
        }
    }

    fun addData(newItems: List<Item>) {
        items.addAll(newItems)
        notifyItemChanged(itemCount)
    }

    fun setData(newItems: List<Item>) {
        items = ArrayList()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}