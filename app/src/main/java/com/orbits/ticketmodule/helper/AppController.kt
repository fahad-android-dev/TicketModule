package com.orbits.ticketmodule.helper

import android.app.Application
import android.os.LocaleList
import com.orbits.ticketmodule.helper.LocaleHelper
import com.orbits.ticketmodule.helper.PrefUtils.getAppConfig
import com.orbits.ticketmodule.helper.PrefUtils.setAppConfig
import com.orbits.ticketmodule.helper.helper_model.AppConfigModel
import java.util.Locale


class AppController : Application() {
    companion object {
        lateinit var instance: AppController
            private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        if (getAppConfig()?.lang?.isEmpty() == true) {
            if (Locale.getDefault().displayLanguage.equals("English", true)) {
                LocaleHelper.setLocale(this, "en")
            } else {
                LocaleHelper.setLocale(this, "ar")
            }
        } else {
            LocaleHelper.setLocale(this, getAppConfig()?.lang ?: "en")
        }

    }

    fun setLocale() {
        val locale: Locale = resources.configuration.locale

        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration

        val localeList = LocaleList(locale)
        LocaleList.setDefault(localeList)
        conf.setLocales(localeList)

        conf.setLayoutDirection(locale)
        res.updateConfiguration(conf, dm)
    }

    fun arabicLanguage() {
        val model = getAppConfig()
        setAppConfig(AppConfigModel(lang = "ar", cartBadgeCount = model?.cartBadgeCount ?: ""))
        setLocale()
    }

    fun englishLanguage() {
        val model = getAppConfig()
        setAppConfig(AppConfigModel(lang = "en", cartBadgeCount = model?.cartBadgeCount ?: ""))
        setLocale()
    }
}