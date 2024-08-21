package com.orbits.ticketmodule.mvvm.main.view

import android.os.Bundle
import android.os.Environment
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupWithNavController
import com.google.gson.JsonObject
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.ActivityMainBinding
import com.orbits.ticketmodule.helper.BaseActivity
import com.orbits.ticketmodule.helper.Constants
import com.orbits.ticketmodule.helper.FileConfig.createExcelFile
import com.orbits.ticketmodule.interfaces.MessageListener
import com.orbits.ticketmodule.mvvm.main.view_model.MainViewModel
import java.io.File
import java.io.FileNotFoundException

class MainActivity : BaseActivity(){

    lateinit var binding: ActivityMainBinding
    private var isBackPressed: Long = 0
    private var currentMenuItemId: Int? = 0
    lateinit var viewModel: MainViewModel
    private val isExternalStorageAvailable: Boolean = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    private val navController by lazy {
        Navigation.findNavController(this, R.id.nav_host_fragment)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        initializeFields()
        onClickListeners()
    }




    private fun onClickListeners() {

    }


    private fun initializeFields() {
        ifStoragePermissionIsEnabled{
            val logFile = File(
                Environment.getExternalStorageDirectory()
                    .toString() + "/Ticket_Config"
            )
            if (!logFile.exists()) {
                logFile.mkdir()
            }

            createDirIfNotExists()
        }



        onBackPressedDispatcher.addCallback(this@MainActivity, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                onBackPressedCallback()
            }
        })
    }


    private fun createDirIfNotExists() {
        try {
            if (isExternalStorageAvailable) {
                val companyLogoFile = File(
                    Environment.getExternalStorageDirectory()
                        .toString() + "/Ticket_Config/Company_Images"
                )
                if (!companyLogoFile.exists()) {
                    companyLogoFile.mkdirs()
                }

                if (!Constants.configFile.exists()) {
                    createExcelFile(Constants.configFile.absolutePath)
                }
            }
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
    }

    fun onBackPressedCallback() {
        if (navController.currentDestination?.id == R.id.navigation_home) {
            if (isBackPressed + 2000 > System.currentTimeMillis()) {
                finish()
            } else {
                isBackPressed = System.currentTimeMillis()
            }
        } else {
            navController.popBackStack()
        }
    }
}