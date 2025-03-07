package sco.carlukesoftware.batteryinformation.di

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import sco.carlukesoftware.batteryinformation.viewmodels.PowerManagerViewModel

fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

@RequiresApi(Build.VERSION_CODES.Q)
val appModule = module {
    viewModel { PowerManagerViewModel(get(), get()) }
    single { provideIoDispatcher() }
}
