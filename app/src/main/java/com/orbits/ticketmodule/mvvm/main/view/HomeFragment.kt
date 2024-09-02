package com.orbits.ticketmodule.mvvm.main.view

import NetworkMonitor
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.gson.JsonObject
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.FragmentHomeBinding
import com.orbits.ticketmodule.helper.AlertDialogInterface
import com.orbits.ticketmodule.helper.BaseFragment
import com.orbits.ticketmodule.helper.Constants
import com.orbits.ticketmodule.helper.Dialogs
import com.orbits.ticketmodule.helper.Dialogs.showCustomAlert
import com.orbits.ticketmodule.helper.Extensions
import com.orbits.ticketmodule.helper.Extensions.isInternetEnabled
import com.orbits.ticketmodule.helper.FileConfig.image_FilePaths
import com.orbits.ticketmodule.helper.FileConfig.readImageFile
import com.orbits.ticketmodule.helper.LocaleHelper
import com.orbits.ticketmodule.helper.NetworkChecker
import com.orbits.ticketmodule.helper.PrefUtils.getServerAddress
import com.orbits.ticketmodule.helper.PrefUtils.saveServerAddress
import com.orbits.ticketmodule.helper.helper_model.ServerAddressModel
import com.orbits.ticketmodule.interfaces.CommonInterfaceClickEvent
import com.orbits.ticketmodule.interfaces.MessageListener
import com.orbits.ticketmodule.interfaces.NetworkListener
import com.orbits.ticketmodule.mvvm.main.adapter.ProductListAdapter
import com.orbits.ticketmodule.mvvm.main.model.ProductListDataModel
import com.orbits.ticketmodule.mvvm.payment.view.ProductObj


class HomeFragment : BaseFragment() , NetworkListener {
    private lateinit var mActivity: MainActivity
    private lateinit var binding: FragmentHomeBinding
    private var productListAdapter = ProductListAdapter()
    private var arrListServices = ArrayList<ProductListDataModel>()
    private lateinit var networkMonitor: NetworkMonitor
    private var ticketId = ""
    private var pos = 0
    private var networkChecker :NetworkChecker ?= null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as MainActivity
        networkChecker = NetworkChecker(mActivity)

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
        networkChecker?.setNetworkListener(this)

        readImageFile()
        if (image_FilePaths?.size == 1) {
            binding.ivCompany.setImageDrawable(Drawable.createFromPath(image_FilePaths?.get(pos)))
            binding.layoutToolbar.ivToolbarLogo.setImageDrawable(Drawable.createFromPath(image_FilePaths?.get(pos)))
        }

        initializeToolbar()
        initializeFields()
    }

    private fun initializeFields(){
        networkChecker?.start()
        Extensions.handler(400){
            networkChecker?.start()
        }

    }


    private fun initializeToolbar() {
        println("here is one")
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

                                        initData()
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

    private fun initData(){
        Extensions.handler(700) {
            mActivity.hideProgressDialog()
            println("here is list ${mActivity.viewModel.dataList}")
            if (mActivity.viewModel.dataList.isNotEmpty()) {
                setData(mActivity.viewModel.dataList)
                ticketId = generateCustomId()
            }
        }
    }



    private fun setData(list: ArrayList<ProductListDataModel>) {
        arrListServices.clear()
        arrListServices.addAll(list)
        val spanCount = 2 // Span count for GridLayoutManager

        // Create a GridLayoutManager
        val layoutManager = GridLayoutManager(mActivity, spanCount)
        if (arrListServices.size > 2) {
            println("here is size 000 ${arrListServices.size}")
            // Set horizontal orientation and span count
            layoutManager.spanCount = 2
            layoutManager.orientation = GridLayoutManager.HORIZONTAL
        } else {
            println("here is size 111 ${arrListServices.size}")
            // Set vertical orientation and span count
            layoutManager.spanCount = 1
            layoutManager.orientation = GridLayoutManager.VERTICAL
        }
        binding.rvProducts.layoutManager = layoutManager
        binding.rvProducts.adapter = productListAdapter


        productListAdapter.onClickEvent = object : CommonInterfaceClickEvent {
            override fun onItemClick(type: String, position: Int) {
                if (type == "itemClicked") {
                    mActivity.showProgressDialog()
                    sendMessage(list[position].id ?: "", list[position].name ?: "")
                    Extensions.handler(800) {
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
        Extensions.handler(1000) {
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


    override fun onSuccess() {
        println("here is connected")
        networkMonitor = NetworkMonitor(mActivity) {
            // Network available, reconnect WebSocket
            if (!mActivity.getServerAddress()?.ipAddress.isNullOrEmpty()){
                mActivity.viewModel.connectWebSocket(
                    mActivity.getServerAddress()?.ipAddress ?: "",
                    mActivity.getServerAddress()?.port ?: ""
                )
            }
            initData()
        }
        networkMonitor.registerNetworkCallback()

    }

    override fun onFailure() {
        Toast.makeText(mActivity,"Failed", Toast.LENGTH_SHORT).show()
    }


}