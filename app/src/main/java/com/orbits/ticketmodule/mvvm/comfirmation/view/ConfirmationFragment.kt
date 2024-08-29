package com.orbits.ticketmodule.mvvm.comfirmation.view

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.FragmentConfirmationBinding
import com.orbits.ticketmodule.helper.AlertDialogInterface
import com.orbits.ticketmodule.helper.BaseFragment
import com.orbits.ticketmodule.helper.Constants
import com.orbits.ticketmodule.helper.Dialogs
import com.orbits.ticketmodule.helper.Extensions
import com.orbits.ticketmodule.helper.FileConfig.image_FilePaths
import com.orbits.ticketmodule.helper.FileConfig.readExcelFile
import com.orbits.ticketmodule.helper.FileConfig.readImageFile
import com.orbits.ticketmodule.helper.PrefUtils.saveServerAddress
import com.orbits.ticketmodule.helper.helper_model.ServerAddressModel
import com.orbits.ticketmodule.interfaces.CommonInterfaceClickEvent
import com.orbits.ticketmodule.mvvm.main.view.MainActivity

class ConfirmationFragment : BaseFragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding : FragmentConfirmationBinding
    private var pos = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_confirmation,
            container,
            false
        )
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initializeToolbar()
        setData()
    }

    private fun initializeToolbar() {
        setUpToolbar(
            binding.layoutToolbar,
            title = getString(R.string.ticket),
            isBackArrow = false,
            navController = findNavController(),
            toolbarClickListener = object : CommonInterfaceClickEvent {
                override fun onToolBarListener(type: String) {
                    when (type) {
                        Constants.TOOLBAR_ICON_ONE -> {

                        }
                    }
                }
            }
        )
    }

    @SuppressLint("ResourceType")
    private fun setData(){
        val colors = readExcelFile(
            Environment.getExternalStorageDirectory()
                .toString() + "/Ticket_Config/Config.xls"
        )

        println("here is all colors $colors")
        val backgroundColor = colors[Constants.TICKET_CONFIRM_COLOR]
        println("here is bg color $backgroundColor")
        if (backgroundColor != null) {
            println("here is bg applied")
            binding.main.setBackgroundColor(Color.parseColor(backgroundColor))
        }

        readImageFile()
        if (image_FilePaths?.size == 1) {
            binding.ivLogo.setImageDrawable(Drawable.createFromPath(image_FilePaths?.get(pos)))
        }


        val model = mActivity.viewModel.dataModel
        val token = model?.getAsJsonObject("transaction")?.get("token")?.asString
        binding.txtTokenValue.text = token
        binding.txtServiceName.text = "Service Name : ${model?.get("serviceName")?.asString}"

        Extensions.handler(2000){
            findNavController().popBackStack()
        }
    }

}