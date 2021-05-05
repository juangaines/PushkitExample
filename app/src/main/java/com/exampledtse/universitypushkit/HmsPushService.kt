package com.exampledtse.universitypushkit

import android.content.Intent
import android.util.Log
import com.huawei.hms.push.HmsMessageService
import com.huawei.hms.push.RemoteMessage

//TODO (5): Crear servicio HmsPushService que extienda  de HmsMessageService
//TODO (6): Registrar servicio en el Manifesto
//TODO (7): Implementar metodo onNewToken y onMessageReceived. Utilizar broadcast para enviar una notificacion al sistema y posteriormente a la actividad.

class HmsPushService : HmsMessageService() {

    override fun onNewToken(token: String) {
        Log.i(MainActivity.TAG, "El token es: $token")
        val intent = Intent()
        intent.action = MainActivity.ON_NEW_TOKEN
        intent.putExtra(MainActivity.EXTRA_TOKEN, token)
        sendBroadcast(intent)
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val map = remoteMessage.dataOfMap
        processRemoteMessage(remoteMessage)
    }

    private fun processRemoteMessage(remoteMessage: RemoteMessage) {
        val map = remoteMessage.dataOfMap
        val state = map["estado_servidor"]
        val intent = Intent()
        intent.action = MainActivity.RIDE_ACTION
        intent.putExtra(MainActivity.EXTRA_APP_STATE, state)
        sendBroadcast(intent)
    }
}