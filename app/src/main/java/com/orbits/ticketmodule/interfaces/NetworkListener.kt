package com.orbits.ticketmodule.interfaces

import com.google.gson.JsonObject

interface NetworkListener {
    fun onSuccess()
    fun onFailure()
}