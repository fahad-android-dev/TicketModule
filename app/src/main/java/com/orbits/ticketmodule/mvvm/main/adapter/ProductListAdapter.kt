package com.orbits.ticketmodule.mvvm.main.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Environment
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.LvItemProductListBinding
import com.orbits.ticketmodule.helper.Constants
import com.orbits.ticketmodule.helper.Extensions
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

        readImageFile()

        val context = holder.itemView.context


        val colors = readExcelFile(
            Environment.getExternalStorageDirectory()
            .toString() + "/Ticket_Config/Config.xls")


        val backgroundColor = colors[Constants.TICKET_TILES_COLOR]
        println("here is radius set 000")
        val radius = colors[Constants.TICKET_TILES_CURVE] // Try to convert it to a Double
        println("here is radius set 222 ${radius}")



        if (radius != null) {
            println("here is radius set 1111")
            holder.binding.rootLayout.radius = radius.toFloat()
        }



        val textColor = colors[Constants.TICKET_TILES_TEXT_COLOR]
        if (textColor != null) {
            holder.binding.txtName.setTextColor(Color.parseColor(textColor))
        }


        if (image_FilePaths?.isNotEmpty() == true) {
            if (position < (image_FilePaths?.size ?: 0)){
                val filePath = image_FilePaths?.get(position)
                holder.binding.txtName.isVisible = false
                holder.binding.rootLayout.background = Drawable.createFromPath(filePath)
                println("Image applied for position: $position")
            }

        }else {
            println("here is image  empty  111")
            holder.binding.txtName.isVisible = true
            if (backgroundColor != null) {
                holder.binding.rootLayout.setCardBackgroundColor(Color.parseColor(backgroundColor)) // Use setCardBackgroundColor
            }
        }



        holder.binding.txtName.text = a.name ?: ""

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