package com.orbits.ticketmodule.mvvm.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.LvItemProductListBinding
import com.orbits.ticketmodule.interfaces.CommonInterfaceClickEvent

class ClientListAdapter(): RecyclerView.Adapter<ClientListAdapter.MyViewHolder>() {

    var arrClientList: MutableList<String> = ArrayList()
    var onClickEvent: CommonInterfaceClickEvent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: LvItemProductListBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lv_item_product_list,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val a = arrClientList[position]


        holder.binding.rootLayout.setOnClickListener {
            onClickEvent?.onItemClick("itemClicked",position)
        }

    }

    override fun getItemCount(): Int {
        return arrClientList.size
    }

    class MyViewHolder(var binding: LvItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<String>) {
        if (data.isNullOrEmpty()) {
            arrClientList = ArrayList()
        }
        arrClientList = data
        notifyDataSetChanged()
    }

    fun updateClients(newClients: List<String>) {
        arrClientList.clear()
        arrClientList.addAll(newClients)
        notifyDataSetChanged()
    }
}