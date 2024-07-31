package com.orbits.ticketmodule.mvvm.main.view_model

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.orbits.ticketmodule.helper.WebSocketClient
import com.orbits.ticketmodule.interfaces.MessageListener
import com.orbits.ticketmodule.mvvm.comfirmation.model.TransactionData
import com.google.gson.Gson
import com.orbits.ticketmodule.mvvm.main.model.ProductListDataModel
import com.orbits.ticketmodule.mvvm.main.model.parseJsonData

class MainViewModel : ViewModel() , MessageListener {


    var isConnected: Boolean = false

    var webSocketClient: WebSocketClient? = null
    var dataModel: JsonObject? = null
    var dataList = ArrayList<ProductListDataModel>()

    fun connectWebSocket(ipAddress: String, port: String) {
        webSocketClient = WebSocketClient("ws://$ipAddress:$port",this)
        webSocketClient?.connect()

        println("Here is websocket connected")

        isConnected = true
        val jsonObject = JsonObject()
        jsonObject.addProperty("message", "Connection")

        if (isConnected) {
            webSocketClient?.sendMessage(jsonObject)
            Log.d("WebSocketViewModel", "Message sent: $jsonObject")
        } else {
            Log.e("WebSocketViewModel", "WebSocket is not connected, cannot send message.")
        }

    }

    fun sendMessage(message: JsonObject) {
        webSocketClient?.sendMessage(message)
    }


    override fun onCleared() {
        super.onCleared()
        isConnected = false
        webSocketClient?.disconnect() // Clean up WebSocket connection
    }

    override fun onMessageJsonReceived(jsonObject: JsonObject) {

        println("here is msg $jsonObject")

        /*dataModel = jsonObject
        println("here is model 2222 $dataModel")*/

        if (jsonObject.has("tokenNo")){
            dataModel = jsonObject
            println("here is data with token $dataModel")
        }else {
            val items = parseJsonData(jsonObject)
            println("Parsed items: $items")

            dataList.clear()
            dataList.addAll(items)
        }


       /* if (!jsonObject.get("status_message").asString.isNullOrEmpty()){
            confirmationDataModel = ConfirmationDataModel(
                transactionData = TransactionData(
                    isNewTransaction = false,
                    receipts = listOf()
                ), status_message = jsonObject.get("status_message").asString

            )
        }else {

        }*/

    }

    private fun parseJsonToModel(jsonString: String): TransactionData? {
        return try {
            val gson = Gson()
            gson.fromJson(jsonString, TransactionData::class.java)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}