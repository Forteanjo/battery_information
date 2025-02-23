package sco.carlukesoftware.batteryinformation.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import sco.carlukesoftware.batteryinformation.di.appModule

class BatteryInfoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@BatteryInfoApplication)
            androidLogger()

            modules(appModule)
        }
    }
}
