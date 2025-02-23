package sco.carlukesoftware.batteryinformation.di

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

val appModule = module {
    single { provideIoDispatcher() }
}
