package com.logicare.test

import android.app.Application
import android.content.Context

import com.polidea.rxandroidble2.RxBleClient
import com.polidea.rxandroidble2.internal.RxBleLog

class BleApplication : Application() {

    private var rxBleClient: RxBleClient? = null

    override fun onCreate() {
        super.onCreate()
        rxBleClient = RxBleClient.create(this)
        RxBleClient.setLogLevel(RxBleLog.VERBOSE)
    }

    companion object {

        /**
         * In practise you will use some kind of dependency injection pattern.
         */
        fun getRxBleClient(context: Context): RxBleClient? {
            val application = context.applicationContext as BleApplication
            return application.rxBleClient
        }
    }
}