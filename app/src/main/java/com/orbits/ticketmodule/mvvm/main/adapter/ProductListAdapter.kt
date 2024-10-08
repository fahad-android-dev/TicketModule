package com.orbits.ticketmodule.mvvm.main.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.LvItemProductListBinding
import com.orbits.ticketmodule.helper.Constants
import com.orbits.ticketmodule.helper.Extensions.asFloat
import com.orbits.ticketmodule.helper.FileConfig.image_FilePaths
import com.orbits.ticketmodule.helper.FileConfig.readExcelFile
import com.orbits.ticketmodule.helper.FileConfig.readImageFile
import com.orbits.ticketmodule.interfaces.CommonInterfaceClickEvent
import com.orbits.ticketmodule.mvvm.main.model.ProductListDataModel

class ProductListAdapter() : RecyclerView.Adapter<ProductListAdapter.MyViewHolder>() {

    var arrClientList: ArrayList<ProductListDataModel> = ArrayList()
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


        val colors = readExcelFile(
            Environment.getExternalStorageDirectory()
            .toString() + "/Ticket_Config/Config.xls")


        val backgroundColor = colors[Constants.TICKET_TILES_COLOR]
      //  val radius = Constants.TICKET_TILES_CURVE.toDoubleOrNull() // Try to convert it to a Double

        if (backgroundColor != null) {
            holder.binding.rootLayout.setBackgroundColor(Color.parseColor(backgroundColor))
        }

        /*if (radius != null) {
           // holder.binding.rootLayout.radius = radius.toFloat()
        }*/

        val textColor = colors[Constants.TICKET_TILES_TEXT_COLOR]
        if (textColor != null) {
            holder.binding.txtName.setTextColor(Color.parseColor(textColor))
        }


        holder.binding.txtName.text = a.name ?: ""
      //  holder.binding.txtPrice.text = "SAR ${a.price ?: ""}"
        holder.binding.ivMain.setImageResource(R.drawable.ic_burger_one)

        holder.binding.rootLayout.setOnClickListener {
            onClickEvent?.onItemClick("itemClicked", position)
        }

    }

    override fun getItemCount(): Int {
        return arrClientList.size
    }

    class MyViewHolder(var binding: LvItemProductListBinding) :
        RecyclerView.ViewHolder(binding.root)

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: ArrayList<ProductListDataModel>) {
        if (data.isNullOrEmpty()) {
            arrClientList = ArrayList()
        }
        arrClientList = data
        notifyDataSetChanged()
    }
}