package com.orbits.ticketmodule.mvvm.comfirmation.view

import android.os.Bundle
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
import com.orbits.ticketmodule.helper.PrefUtils.saveServerAddress
import com.orbits.ticketmodule.helper.helper_model.ServerAddressModel
import com.orbits.ticketmodule.interfaces.CommonInterfaceClickEvent
import com.orbits.ticketmodule.mvvm.main.view.MainActivity

class ConfirmationFragment : BaseFragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding : FragmentConfirmationBinding

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
            isBackArrow = true,
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

    private fun setData(){
        val model = mActivity.viewModel.dataModel
        val token = model?.getAsJsonObject("transaction")?.get("token")?.asString
        binding.txtTokenValue.text = token
        binding.txtServiceName.text = "Service Name : ${model?.get("serviceName")?.asString}"
    }

}