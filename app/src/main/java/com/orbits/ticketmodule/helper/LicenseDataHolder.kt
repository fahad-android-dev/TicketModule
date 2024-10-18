package com.orbits.softkeypad.helper

import java.util.Calendar
import java.util.GregorianCalendar

class LicenseDataHolder {

    var licenseKey: String
    private var mCalStartDate: Calendar
    private var mCalLastRunDate: Calendar? = null
    private var mCalEndDate: Calendar? = null
    private var mContainsError = false
    var message: String? = null
    var errorMsg: String? = null
    var referenceNo: String? = null
    var responseCode: String? = null

    var trialDays: String

    constructor() {
        this.licenseKey = ""
        this.mCalStartDate = GregorianCalendar()
        this.mCalLastRunDate = GregorianCalendar()
        this.trialDays = ""
        this.mCalEndDate = GregorianCalendar()
        this.mContainsError = true
        this.message = ""
        this.errorMsg = ""
        this.referenceNo = ""
        this.responseCode = ""
    }

    fun ContainsError(): Boolean {
        return mContainsError
    }

    fun setContainsError(containsError: Boolean) {
        this.mContainsError = containsError
    }

    constructor(mStrLicenseKey: String, mCalInstallDate: Calendar, days: String) {
        this.licenseKey = mStrLicenseKey
        this.mCalStartDate = mCalInstallDate.clone() as Calendar
        this.trialDays = days
    }

    var startDate: Calendar
        get() = mCalStartDate.clone() as Calendar
        set(startDate) {
            this.mCalStartDate = startDate.clone() as Calendar
        }

    var lastRunDate: Calendar
        get() = mCalLastRunDate!!.clone() as Calendar
        set(lastRunDate) {
            this.mCalLastRunDate = lastRunDate.clone() as Calendar
        }

    var endDate: Calendar
        get() = mCalEndDate!!.clone() as Calendar
        set(calEndDate) {
            this.mCalEndDate = calEndDate.clone() as Calendar
        }
}
