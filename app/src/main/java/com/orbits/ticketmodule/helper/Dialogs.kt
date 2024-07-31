package com.orbits.ticketmodule.helper

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.LayoutCustomAlertBinding
import com.orbits.ticketmodule.databinding.LayoutGenerateCodeDialogBinding
import com.orbits.ticketmodule.databinding.LayoutPairingDialogBinding
import com.orbits.ticketmodule.helper.Global.getDimension
import com.orbits.ticketmodule.helper.PrefUtils.getServerAddress
import com.orbits.ticketmodule.helper.AlertDialogInterface
import com.orbits.ticketmodule.helper.Global.getTypeFace
import com.orbits.ticketmodule.interfaces.WheelViewEvent

object Dialogs {

    var customDialog: Dialog? = null
    var pairingDialog: Dialog? = null
    var codeDialog: Dialog? = null



    fun showPairingDialog(
        activity: Context,
        isCancellable: Boolean? = true,
        alertDialogInterface: AlertDialogInterface,
    ) {
        try {
            pairingDialog = Dialog(activity)
            pairingDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            pairingDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val binding: LayoutPairingDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.layout_pairing_dialog, null, false
            )
            pairingDialog?.setContentView(binding.root)
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(pairingDialog?.window?.attributes)
            lp.width = getDimension(activity as Activity, 300.00)
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            pairingDialog?.window?.attributes = lp
            pairingDialog?.setCanceledOnTouchOutside(isCancellable ?: true)
            pairingDialog?.setCancelable(isCancellable ?: true)

            binding.edtAddress.setText(activity.getServerAddress()?.ipAddress)
            binding.edtPort.setText(activity.getServerAddress()?.port)

            binding.btnAlertPositive.setOnClickListener {
                if (binding.edtAddress.text.isEmpty()){
                    Toast.makeText(activity,"Please enter ip address", Toast.LENGTH_SHORT).show()
                }else if (binding.edtPort.text.isEmpty()){
                    Toast.makeText(activity,"Please enter port number", Toast.LENGTH_SHORT).show()
                }
                else{
                    pairingDialog?.dismiss()
                    alertDialogInterface.onConnectionConfirm(
                        binding.edtAddress.text.toString(),
                        binding.edtPort.text.toString(),
                    )
                }
            }
            pairingDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showCodeDialog(
        activity: Context,
        code : String ?= "",
        isCancellable: Boolean? = true,
        alertDialogInterface: AlertDialogInterface,
    ) {
        try {
            codeDialog = Dialog(activity)
            codeDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            codeDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val binding: LayoutGenerateCodeDialogBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.layout_generate_code_dialog, null, false
            )
            codeDialog?.setContentView(binding.root)
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(codeDialog?.window?.attributes)
            lp.width = getDimension(activity as Activity, 300.00)
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            codeDialog?.window?.attributes = lp
            codeDialog?.setCanceledOnTouchOutside(isCancellable ?: true)
            codeDialog?.setCancelable(isCancellable ?: true)

            println("herer is code 111 $code")

            binding.txtCode.text = addSpacesBetweenLetters(code ?: "")

            if (code?.isEmpty() == true){
                binding.btnAlertPositive.text = activity.getString(R.string.label_generate)
            }else{
                binding.btnAlertPositive.text = activity.getString(R.string.label_regenerate)
            }

            binding.btnAlertPositive.setOnClickListener {
                alertDialogInterface.onYesClick()
                binding.txtCode.text = addSpacesBetweenLetters(code ?: "")
                codeDialog?.dismiss()
            }
            codeDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showCustomAlert(
        activity: Context,
        title: String = "",
        msg: String = "",
        yesBtn: String,
        noBtn: String,
        singleBtn: Boolean = false,
        isCancellable: Boolean? = true,
        alertDialogInterface: AlertDialogInterface,
    ) {
        try {
            customDialog = Dialog(activity)
            customDialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
            customDialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            val binding: LayoutCustomAlertBinding = DataBindingUtil.inflate(
                LayoutInflater.from(activity),
                R.layout.layout_custom_alert, null, false
            )
            customDialog?.setContentView(binding.root)
            val lp: WindowManager.LayoutParams = WindowManager.LayoutParams()
            lp.copyFrom(customDialog?.window?.attributes)
            lp.width = getDimension(activity as Activity, 300.00)
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            lp.gravity = Gravity.CENTER
            customDialog?.window?.attributes = lp
            customDialog?.setCanceledOnTouchOutside(isCancellable ?: true)
            customDialog?.setCancelable(isCancellable ?: true)


            binding.txtAlertTitle.text = title
            binding.txtAlertMessage.text = msg
            binding.btnAlertNegative.text = noBtn
            binding.btnAlertPositive.text = yesBtn

            binding.btnAlertNegative.visibility = if (singleBtn) View.GONE else View.VISIBLE
            binding.btnAlertNegative.setOnClickListener {
                customDialog?.dismiss()
                alertDialogInterface.onNoClick()
            }
            binding.btnAlertPositive.setOnClickListener {
                customDialog?.dismiss()
                alertDialogInterface.onYesClick()
            }
            customDialog?.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showWheelView(
        activity: Activity,
        arrayListData: ArrayList<String>,
        listener: WheelViewEvent
    ) {
        val parentView = activity.layoutInflater.inflate(R.layout.layout_bottom_sheet_picker, null)
        val bottomSheerDialog = BottomSheetDialog(activity)
        bottomSheerDialog.setContentView(parentView)
        bottomSheerDialog.setCanceledOnTouchOutside(false)
        bottomSheerDialog.setCancelable(false)
        val wheelView = parentView.findViewById(R.id.wheelView) as WheelView
        val txtDone = parentView.findViewById(R.id.txtDone) as TextView
        val txtCancel = parentView.findViewById(R.id.txtCancel) as TextView
        txtCancel.typeface = getTypeFace(activity, Constants.fontMedium)
        txtDone.typeface = getTypeFace(activity, Constants.fontMedium)

        try {
            if (arrayListData.isNotEmpty()) {
                wheelView.setItems(arrayListData)
                txtCancel.setOnClickListener {
                    bottomSheerDialog.dismiss()
                }
                txtDone.setOnClickListener {
                    bottomSheerDialog.dismiss()
                    listener.onDoneClicked(wheelView.seletedIndex)
                }
                bottomSheerDialog.show()
            }
        } catch (e: Exception) {
        }
    }

    fun addSpacesBetweenLetters(input: String): String {
        // Convert the string to a list of characters, join them with spaces, and convert back to string
        return input.toCharArray().joinToString("   ")
    }


}
