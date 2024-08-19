package com.orbits.ticketmodule.mvvm.main.view

import com.orbits.ticketmodule.helper.AppNavigation.navigateToMain
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.gson.JsonObject
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.FragmentHomeBinding
import com.orbits.ticketmodule.helper.AlertDialogInterface
import com.orbits.ticketmodule.helper.BaseFragment
import com.orbits.ticketmodule.helper.Constants
import com.orbits.ticketmodule.helper.Dialogs
import com.orbits.ticketmodule.helper.Dialogs.showCustomAlert
import com.orbits.ticketmodule.helper.Extensions
import com.orbits.ticketmodule.helper.LocaleHelper
import com.orbits.ticketmodule.helper.PrefUtils.getServerAddress
import com.orbits.ticketmodule.helper.PrefUtils.saveServerAddress
import com.orbits.ticketmodule.helper.helper_model.ServerAddressModel
import com.orbits.ticketmodule.interfaces.CommonInterfaceClickEvent
import com.orbits.ticketmodule.interfaces.MessageListener
import com.orbits.ticketmodule.mvvm.main.adapter.ProductListAdapter
import com.orbits.ticketmodule.mvvm.main.model.ProductListDataModel
import com.orbits.ticketmodule.mvvm.payment.view.ProductObj


class HomeFragment : BaseFragment() {
    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentHomeBinding
    private var productListAdapter = ProductListAdapter()
    private var arrListServices = ArrayList<ProductListDataModel>()
    private var ticketId = ""

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
            R.layout.fragment_home,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.rvProducts.adapter = productListAdapter

        initializeToolbar()
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
                            Dialogs.showPairingDialog(
                                activity = mActivity,
                                alertDialogInterface = object : AlertDialogInterface {
                                    override fun onConnectionConfirm(
                                        ipAddress: String,
                                        port: String,
                                    ) {
                                        mActivity.showProgressDialog()
                                        mActivity.saveServerAddress(
                                            ServerAddressModel(
                                                ipAddress = ipAddress, port = port

                                            )
                                        )
                                        mActivity.viewModel.connectWebSocket(
                                            ipAddress,
                                            port
                                        ) // Pass necessary

                                        Extensions.handler(2000) {
                                            mActivity.hideProgressDialog()
                                            println("here is list ${mActivity.viewModel.dataList}")
                                            if (mActivity.viewModel.dataList.isNotEmpty()) {
                                                setData(mActivity.viewModel.dataList)
                                                ticketId = generateCustomId()
                                            }
                                        }
                                    }
                                }
                            )
                        }

                        Constants.TOOLBAR_ICON_TWO -> {
                           // showChangeLanguageAlert()
                        }
                    }
                }
            }
        )
    }

    private fun showChangeLanguageAlert() {
        showCustomAlert(
            activity = mActivity,
            title = getString(R.string.alert_title_lang),
            msg = resources.getString(R.string.alert_language),
            yesBtn = resources.getString(R.string.yes_lang),
            noBtn = resources.getString(R.string.no_lang),
            alertDialogInterface = object : AlertDialogInterface {
                override fun onYesClick() {
                    LocaleHelper.changeLanguage(mActivity)
                    mActivity.navigateToMain {}
                }

                override fun onNoClick() {}
            })
    }


    private fun setData(list: ArrayList<ProductListDataModel>) {
        arrListServices.clear()
        arrListServices.addAll(list)

        productListAdapter.onClickEvent = object : CommonInterfaceClickEvent {
            override fun onItemClick(type: String, position: Int) {
                if (type == "itemClicked") {
                    mActivity.showProgressDialog()
                    sendMessage(list[position].id ?: "", list[position].name ?: "")
                    Extensions.handler(2000) {
                        mActivity.hideProgressDialog()
                        if (mActivity.viewModel.dataModel != null) {
                            findNavController().navigate(R.id.action_to_navigation_confirmation)
                        }
                    }
                }
            }
        }

        productListAdapter.setData(arrListServices)
    }

    private fun sendMessage(serviceId: String, serviceType: String) {
        val jsonObject = JsonObject()
        jsonObject.addProperty("serviceId", serviceId)
        jsonObject.addProperty("serviceType", serviceType)
        jsonObject.addProperty("ticketType", "TicketType")
        jsonObject.addProperty("ticketId",ticketId)

        mActivity.viewModel.webSocketClient?.sendMessage(jsonObject)
        Log.d("FragmentTwo", "Sending message: $jsonObject")
    }

    var counter = 1

    fun generateCustomId(): String {
        return "T${counter++}"
    }

    override fun onResume() {
        super.onResume()
        mActivity.viewModel.dataModel = null
        Extensions.handler(2000) {
            println("here is list ${mActivity.viewModel.dataList}")
            if (mActivity.viewModel.dataList.isNotEmpty()) {
                setData(mActivity.viewModel.dataList)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        mActivity.viewModel.webSocketClient?.disconnect()
    }


}