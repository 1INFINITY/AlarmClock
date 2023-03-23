package ru.mirea.ivashechkinav.alarmclock

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.mirea.ivashechkinav.alarmclock.data.room.AlarmDao
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var dao: AlarmDao

    override fun onCreate() {
        super.onCreate()
        GlobalScope.launch {
            dao.clearAll()
        }
    }
}