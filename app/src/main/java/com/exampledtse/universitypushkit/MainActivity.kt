package com.exampledtse.universitypushkit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*



class MainActivity : AppCompatActivity() {

    private var state = ""
    private var token = ""
    private lateinit var receiver: RideBroadcastReceiver
    private lateinit var intentFilter: IntentFilter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getToken()
        info_text.text = "--"
        receiver = RideBroadcastReceiver()
        intentFilter = IntentFilter().apply {
            addAction(RIDE_ACTION)
        }
    }

    override fun onResume() {
        super.onResume()
        registerReceiver(receiver, intentFilter)

    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(receiver)
    }

    private fun getToken() {

        //TODO (4): Obtener token a traves de  HmsInstanceId ->getInstance->getToken
        //Se debe obtener appId y token scope (HCM)

    }

    inner class RideBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            when (intent.action) {
                ON_NEW_TOKEN -> {
                    token = intent.getStringExtra(EXTRA_TOKEN) ?: ""
                }
                RIDE_ACTION -> {
                    state = intent.getStringExtra(EXTRA_APP_STATE) ?: ""
                    info_text.text = state
                }
            }
        }
    }

    companion object {
        val TAG: String = MainActivity::class.java.simpleName
        const val ON_NEW_TOKEN: String = "com.ean.ON_NEW_TOKEN"
        const val RIDE_ACTION: String = "com.ean.CUSTOM_INTENT"
        const val EXTRA_TOKEN: String = "token"
        const val EXTRA_APP_STATE: String = "estado_app"
    }
}