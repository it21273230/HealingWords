//package com.example.healingwords
//
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.IntentFilter
//import android.net.ConnectivityManager
//import android.net.Network
//import android.net.NetworkInfo
//import android.os.Build
//import android.widget.Toast
//import androidx.lifecycle.LiveData
//
//class NetworkConnectivity(private val context: Context) : LiveData<Boolean>() {
//    private val connectivityManager: ConnectivityManager =
//        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//
//    private lateinit var networkConnectionCallback: ConnectivityManager.NetworkCallback
//    private var wasConnected = false // to keep track of previous network connection state
//
//    override fun onActive() {
//        super.onActive()
//        updateNetworkConnection()
//        when {
//            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
//                connectivityManager.registerDefaultNetworkCallback(connectionCallback())
//            }
//            else -> {
//                context.registerReceiver(
//                    networkReceiver,
//                    IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//                )
//            }
//        }
//    }
//
//    override fun onInactive() {
//        super.onInactive()
//        try {
//            connectivityManager.unregisterNetworkCallback(connectionCallback())
//        } catch (e: IllegalArgumentException) {
//            // Ignore the exception, as the callback is not registered
//        }
//    }
//
//    private fun connectionCallback(): ConnectivityManager.NetworkCallback {
//        networkConnectionCallback = object : ConnectivityManager.NetworkCallback() {
//            override fun onAvailable(network: Network) {
//                super.onAvailable(network)
//                val isConnected = true
//                postValue(isConnected)
//                showConnectionToastIfNeeded(isConnected)
//            }
//
//            override fun onLost(network: Network) {
//                super.onLost(network)
//                val isConnected = false
//                postValue(isConnected)
//                showConnectionToastIfNeeded(isConnected)
//            }
//        }
//        return networkConnectionCallback
//    }
//
//    private fun updateNetworkConnection() {
//        val networkConnection: NetworkInfo? = connectivityManager.activeNetworkInfo
//        val isConnected = networkConnection?.isConnected == true
//        postValue(isConnected)
//        showConnectionToastIfNeeded(isConnected)
//    }
//
//    private fun showConnectionToastIfNeeded(isConnected: Boolean) {
//        if (isConnected && !wasConnected) {
//            // Show "Connected" Toast only if the network was disconnected and now connected
//            Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show()
//        }
//        if(!isConnected){
//            Toast.makeText(context, "Disconnected", Toast.LENGTH_LONG).show()
//            wasConnected = isConnected
//        }
//
//    }
//
//    private val networkReceiver = object : BroadcastReceiver() {
//        override fun onReceive(context: Context?, intent: Intent?) {
//            updateNetworkConnection()
//        }
//    }
//}
