package com.amrh.verticalpicker.adaptor

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.amrh.verticalpicker.R

class PickerAdaptor: RecyclerView.Adapter<PickerAdaptor.ItemViewHolder>() {



    private val data: ArrayList<String> = ArrayList();
    var callback: Callback? = null
    val clickListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            v?.let { callback?.onItemClicked(it) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView: View = LayoutInflater.from(parent.context).inflate(R.layout.item_picker_view150, parent, false)

        itemView.setOnClickListener(clickListener)

        val ViewHolder = ItemViewHolder(itemView)
        return ViewHolder
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.tvItem?.text = data[position]
    }

    fun SetData(data: ArrayList<String>) {
        this.data.clear()
        this.data.addAll(data)
        notifyDataSetChanged()
    }

    interface Callback {
        fun onItemClicked(view: View)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val tvItem: TextView? = itemView.findViewById(R.id.tv_picker_view_item150)
    }
}