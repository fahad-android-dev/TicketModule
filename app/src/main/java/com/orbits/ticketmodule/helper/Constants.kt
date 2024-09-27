package com.orbits.ticketmodule.helper

import android.os.Build
import android.os.Environment
import com.orbits.ticketmodule.BuildConfig
import java.io.File


object Constants {

    var DEVICE_TOKEN = ""
    val DEVICE_MODEL: String = Build.MODEL
    const val DEVICE_TYPE = "A" //passed in banners
    val OS_VERSION = Build.VERSION.RELEASE
    const val DATE_FORMAT = "yyyy-MM-dd hh:mm:ss"
    var DEVICE_DENSITY = 0.0
    val fontBold = "bold"
    val fontRegular = "regular"
    val fontMedium = "medium"
    val fontRegularRev = "regular_reverse"
    const val TOOLBAR_ICON_ONE = "iconOne"
    const val TOOLBAR_ICON_TWO = "iconTwo"
    const val TOOLBAR_ICON_BACK = "iconBack"




    // File Config Variables
    val configFile = File(
        Environment.getExternalStorageDirectory()
            .toString() + "/Ticket_Config/Config.xls"
    )

    const val TICKET_TILES_COLOR = "TicketColor"
    const val TICKET_TILES_CURVE = "TicketTilesCurve"
    const val TICKET_CONFIRM_COLOR = "TicketConfirmColor"
    const val TICKET_TILES_TEXT_COLOR = "TicketTextColor"
    const val COMPANY_LOGO = "CompanyLogo"

}

