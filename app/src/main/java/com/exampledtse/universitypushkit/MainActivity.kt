package com.exampledtse.universitypushkit

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.huawei.agconnect.config.AGConnectServicesConfig
import com.huawei.hms.aaid.HmsInstanceId
import com.huawei.hms.common.ApiException
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
        object : Thread() {
            override fun run() {
                try {

                    val appId = AGConnectServicesConfig.fromContext(this@MainActivity)
                        .getString("client/app_id")
                    val tokenScope = "HCM"
                    val token =
                        HmsInstanceId.getInstance(this@MainActivity).getToken(appId, tokenScope)
                    Log.i(TAG, "El token es: $token")
                    //En caso de que este vacio el token, como alternativa se
                    // puede obtener por el metodo onNewToken() en el servicio que se extiende de HmsMessageService

                    //Una vez obtenido el token se puede hacer el registro en el backend para almacenarlo
                    // y posteriormente utilizarlo

                } catch (e: ApiException) {
                    Log.e(TAG, "get token failed, $e")
                }
            }
        }.start()
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