package com.orbits.ticketmodule.helper

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.wifi.WifiManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings.Secure
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.ClientProtocolException
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.HttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.entity.UrlEncodedFormEntity
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.client.methods.HttpPost
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.impl.client.DefaultHttpClient
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.util.EntityUtils
import com.orbits.softkeypad.helper.LicenseDataHolder
import com.orbits.ticketmodule.R
import com.orbits.ticketmodule.databinding.DialogLicenseEntryBinding
import com.orbits.ticketmodule.helper.PrefUtils.getAppConfig
import com.orbits.ticketmodule.helper.PrefUtils.setAppConfig
import com.orbits.ticketmodule.helper.helper_model.AppConfigModel
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar
import java.util.Locale

//import android.util.Log;
class LicenseDialog : DialogFragment() {
    private lateinit var mActivity: BaseActivity
    private lateinit var binding : DialogLicenseEntryBinding
    private var mLicenseDialogListener: OnLicenseDialogFinishListener? = null

    private var strAndroidId = ""
    private var strMacAddress: String? = ""
    private var mLicense: LicenseDataHolder? = null
    private var reqType = TRIAL_LIC
    private val mSaveBtnClickListener = View.OnClickListener {
        val activity: Context? = activity
        if (activity != null) {
            try {
                mLicenseDialogListener = activity as OnLicenseDialogFinishListener?
                dialog.dismiss()
                mLicenseDialogListener!!.onLicenseDialogFinished(mLicense)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)  // Use the LayoutInflater from context
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.dialog_license_entry,
            null,
            false
        )



        // Create the dialog
        val builder = AlertDialog.Builder(mActivity)
        builder.setView(binding.root)

        binding.tvCustCode.text = Html.fromHtml("Customer Code <sup><small>*</small></sup>")
        binding.tvInvNum.text = Html.fromHtml("Invoice Number  <sup><small>*</small></sup>")
        binding.tvBranchCode.text = Html.fromHtml("Branch Code  <sup><small>*</small></sup>")
        binding.tvEmail.text = Html.fromHtml("Email  <sup><small>#</small></sup>")
        binding.tvPhone.text = Html.fromHtml("Contact No.  <sup><small>#</small></sup>")

        binding.btnGetFullLicense.tag = FULL_LIC
        binding.btnGetTrialLicense.tag = TRIAL_LIC

        binding.btnGetFullLicense.setOnClickListener(RequestLicenseBtnClickListener())
        binding.btnGetTrialLicense.setOnClickListener(RequestLicenseBtnClickListener())
        binding.btnSaveLicense.setOnClickListener(mSaveBtnClickListener)
        binding.btnExitLicense.setOnClickListener(ExitLicenseBtnClickListener())

        val dialog: Dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(
            ColorDrawable(Color.TRANSPARENT)
        )
        //AlertDialog dialog = builder.create();
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity as BaseActivity
    }

    override fun onStart() {
        super.onStart()


        binding.edtCustCode.setText(mActivity.getAppConfig()?.customerCode)

        binding.edtInvoiceNumber.setText(mActivity.getAppConfig()?.invoice)

        binding.edtBranchCode.setText(mActivity.getAppConfig()?.branchCode)

        binding.edtEmail.setText(mActivity.getAppConfig()?.email)

        binding.edtPhone.setText(mActivity.getAppConfig()?.phone)

        binding.edtStaff.setText(mActivity.getAppConfig()?.staffId)

        binding.edtPassword.setText(mActivity.getAppConfig()?.password)

        mLicense = LicenseDataHolder()
        strAndroidId = Secure.getString(
            activity.contentResolver,
            Secure.ANDROID_ID
        )
        strMacAddress = mACAddress
        if (strMacAddress == null) {
            Toast.makeText(
                activity, "Ensure that wifi is switched on.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private val mACAddress: String
        get() {

            try {
                val wifiManager = activity.applicationContext
                    .getSystemService(Context.WIFI_SERVICE) as WifiManager
                val wInfo = wifiManager.connectionInfo
                val macAddress = wInfo.macAddress
                return macAddress
            } catch (e: Exception) {
                // TODO: handle exception
                return ""
            }
        }

    private fun isEmailValid(strEmail: String?): Boolean {
        val EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$"
        return strEmail?.matches(EMAIL_REGEX.toRegex()) ?: false
    }

    internal interface OnLicenseDialogFinishListener {
        fun onLicenseDialogFinished(license: LicenseDataHolder?)
    }

    private inner class ExitLicenseBtnClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            // TODO Auto-generated method stub
            dialog.dismiss()
            activity.finishAffinity()
            System.exit(0)
        }
    }

    private inner class RequestLicenseBtnClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            // TODO Auto-generated method stub

            if (binding.edtInvoiceNumber.text.toString() != "" || binding.edtBranchCode.text.toString() != "" ||
                binding.edtCustCode.text.toString() != "" || binding.edtEmail.text.toString() != ""
                || binding.edtPhone.text.toString() != "" || (binding.edtStaff.text.toString() != ""
                        && binding.edtPassword.text.toString() != "")
            ) {

                mActivity.setAppConfig(
                    AppConfigModel(
                        customerCode = binding.edtCustCode.text.toString(),
                        invoice = binding.edtInvoiceNumber.text.toString(),
                        branchCode = binding.edtBranchCode.text.toString(),
                        email = binding.edtEmail.text.toString(),
                        phone = binding.edtPhone.text.toString(),
                        staffId = binding.edtStaff.text.toString(),
                        password = binding.edtPassword.text.toString()

                    )
                )
            }
            reqType = v.tag.toString()
            val strUrl = ("http://jeddah.from-ak.com:86" + "/"
                    + "License.aspx")
            if (v.tag.toString() == FULL_LIC && (binding.edtInvoiceNumber.text.toString() == "" || binding.edtBranchCode.text.toString() == "" || binding.edtCustCode.getText().toString() == "")
            ) {
                binding.tvInfo.setTextColor(Color.RED)
                binding.tvInfo.text = "Please provide Invoice-No, Branch-code and Customer-code."
                return
            }
            if (binding.edtStaff.text.toString() != "" && binding.edtPassword!!.text.toString() == "") {
                binding.tvInfo.setTextColor(Color.RED)
                binding.tvInfo.text = "Password field cannot be left empty."
                return
            }
            if (v.tag.toString() == TRIAL_LIC && binding.edtEmail.text.toString() == "" && binding.edtPhone.text.toString() == "") {
                binding.tvInfo.setTextColor(Color.RED)
                binding.tvInfo.text = "Email-address or phone-number is required for trial license."
                return
            } else if (v.tag.toString() == TRIAL_LIC && binding.edtEmail.text.toString() != ""
                && !isEmailValid(binding.edtEmail.text.toString())
            ) {
                binding.tvInfo.setTextColor(Color.RED)
                binding.tvInfo.text = "Invalid email-address."
                return
            }
            AsyncTask_RequestLicense().executeOnExecutor(
                AsyncTask.THREAD_POOL_EXECUTOR, strUrl, "android_id",
                strAndroidId, "mac", strMacAddress, "app_id", APP_ID,
                "cust_code", binding.edtCustCode.text.toString(),
                "invoice_no", binding.edtInvoiceNumber.text.toString(),
                "branch_code", binding.edtBranchCode.text.toString(),
                "staff_id", binding.edtStaff.text.toString(), "staff_pwd",
                binding.edtPassword.text.toString(), "request_type", v.tag
                    .toString(), "email",
                binding.edtEmail.text.toString(), "phone", binding.edtPhone.text
                    .toString()
            )
        }
    }

    private inner class AsyncTask_RequestLicense :
        AsyncTask<String?, String?, LicenseDataHolder>() {
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute()
            progressDialog = ProgressDialog.show(
                activity, "",
                "sending...", true, false
            )
        }

        override fun doInBackground(vararg params: String?): LicenseDataHolder? {
            var strLicense = ""
            var TrialDays = ""
            val license = LicenseDataHolder()
            val httpClient: HttpClient
            val httpPost: HttpPost

            try {
                httpClient = DefaultHttpClient()
                httpPost = HttpPost(params[0])
                val dataList: MutableList<NameValuePair> = ArrayList()
                var i = 1
                while (i < params.size) {
                    dataList.add(BasicNameValuePair(params[i], params[++i]))
                    i++
                }
                httpPost.entity = UrlEncodedFormEntity(dataList, "UTF-8")
                val httpResponse = httpClient.execute(httpPost)
                val httpResponseEntity = httpResponse.entity
                val str = EntityUtils.toString(httpResponseEntity, "UTF-8")
                val json = JSONObject(str)
                var strResponseCode = ""
                strResponseCode = json.getString("response")
                license.responseCode = strResponseCode
                if (strResponseCode != "") {
                    if (strResponseCode.equals(APPROVED, ignoreCase = true)) {
                        strLicense = json.getString("license")
                        //Sarvajeet 11-5
                        TrialDays = json.getString("TrialDays")
                        license.trialDays = TrialDays
                        val dateFormat: DateFormat = SimpleDateFormat(
                            "dd-MM-yyyy", Locale.US
                        )
                        var date: Date? = Date()
                        date = dateFormat.parse(json.getString("startDate"))
                        val cal: Calendar = GregorianCalendar()
                        cal.time = date
                        license.startDate = cal
                    }
                }

                license.referenceNo = json.getString("refNo")
                license.message = json.getString("msg")
                license.setContainsError(false)
                license.errorMsg = ""
            } catch (e: IllegalArgumentException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Server not found"
            } catch (e: ArrayIndexOutOfBoundsException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Insufficient data entered"
            } catch (e: UnsupportedEncodingException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Internal encoding error"
            } catch (e: ClientProtocolException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Connection error"
            } catch (e: IOException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Connection broken"
            } catch (e: ParseException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Corrupt data received"
            }  catch (e: JSONException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Improper data received"
            } catch (e: NullPointerException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Internal error - nullpointer exception"
            } catch (e: IllegalStateException) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Internal error - data already consumed"
            } catch (e: Exception) {
                strLicense = ""
                license.setContainsError(true)
                license.errorMsg = "Some error occurred"

            }
            license.licenseKey = strLicense

            return license
        }

        override fun onPostExecute(license: LicenseDataHolder) {
            super.onPostExecute(license)

            progressDialog!!.dismiss()
            if (!license.ContainsError()) {
                mLicense = license
                var strMsg = ""
                if (license.responseCode != "") {
                    if (license.responseCode.equals(APPROVED, ignoreCase = true)) {

                        binding.tvInfo.setTextColor(Color.rgb(15, 159, 48))
                        strMsg = "Your request has been approved, press Save to continue."
                    } else if (license.responseCode.equals(
                            PENDING, ignoreCase = true
                        )
                    ) {

                        binding.tvInfo.setTextColor(Color.rgb(15, 159, 48))
                        strMsg = "Your request is pending approval."
                    } else if (license.responseCode.equals(
                            REJECTED, ignoreCase = true
                        )
                    ) {
                        binding.tvInfo.setTextColor(Color.RED)
                        strMsg =
                            ("Request rejected, please contact company salesperson and quote the reference number: "
                                    + license.referenceNo
                                    + "."
                                    + (if (license.message == "") ""
                            else """
 Reason: ${license.message}"""))
                    } else if (license.responseCode.equals(
                            REJECTED_AMT_PAID, ignoreCase = true
                        )
                    ) {

                        binding.tvInfo.setTextColor(Color.RED)
                        strMsg =
                            "Your request is rejected because your invoice is not fully paid. Please go for trial version."
                    } else if (license.responseCode.equals(
                            RECEIVED, ignoreCase = true
                        )
                    ) {

                        if (reqType == TRIAL_LIC) {
                            mActivity.setAppConfig(
                                AppConfigModel(
                                    email = binding.edtEmail.text.toString(),
                                    phone = binding.edtPhone.text.toString()

                                )
                            )
                        } else {
                            mActivity.setAppConfig(
                                AppConfigModel(
                                    phone = binding.edtPhone.text.toString()

                                )
                            )
                        }

                        binding.tvInfo.setTextColor(Color.rgb(15, 159, 48))
                        strMsg = "Request sent successfully."
                    } else if (license.responseCode.equals(
                            INVALID_REQUEST, ignoreCase = true
                        )
                    ) {

                        binding.tvInfo.setTextColor(Color.RED)
                        strMsg = "Request failed, please re-check the data you have provided."
                    } else if (license.responseCode.equals(
                            TRIAL_EXPIRED, ignoreCase = true
                        )
                    ) {

                        binding.tvInfo.setTextColor(Color.RED)
                        strMsg =
                            ("Your trial-license has expired, you may contact the company salesperson and quote the reference number: "
                                    + license.referenceNo)
                    }
                }
                binding.tvInfo.text = strMsg
            } else {
                binding.tvInfo.setTextColor(Color.RED)
                binding.tvInfo.text = license.errorMsg
            }
        }
    }

    companion object {
        private const val APP_ID = "queue_window_display"
        private const val FULL_LIC = "2"
        private const val TRIAL_LIC = "3"


        private const val RECEIVED = "8"
        private const val PENDING = "9"
        private const val REJECTED = "10"
        private const val APPROVED = "11"
        private const val INVALID_REQUEST = "12"
        private const val TRIAL_EXPIRED = "13"
        private const val REJECTED_AMT_PAID = "14"
    }
}
