package com.orbits.ticketmodule.mvvm.payment.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.LvItemPaymentMethodBinding
import com.orbits.ticketmodule.interfaces.CommonInterfaceClickEvent
import com.orbits.ticketmodule.mvvm.payment.model.PaymentDataModel

class PaymentMethodAdapter() : RecyclerView.Adapter<PaymentMethodAdapter.MyViewHolder>() {

    var arrClientList: ArrayList<PaymentDataModel> = ArrayList()
    var onClickEvent: CommonInterfaceClickEvent? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding: LvItemPaymentMethodBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.lv_item_payment_method,
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val a = arrClientList[position]

        holder.binding.txtName.text = a.name ?: ""
        holder.binding.ivPayment.setImageResource(a.image ?: 0)

        holder.binding.rootLayout.setOnClickListener {
            onClickEvent?.onItemClick("itemClicked", position)
        }

    }

    override fun getItemCount(): Int {
        return arrClientList.size
    }

    class MyViewHolder(var binding: LvItemPaymentMethodBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<PaymentDataModel>) {
        if (data.isNullOrEmpty()) {
            arrClientList = ArrayList()
        }
        arrClientList = data
        notifyDataSetChanged()
    }
}