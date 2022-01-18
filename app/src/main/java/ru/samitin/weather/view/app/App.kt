package ru.samitin.weather.view.app

import android.app.Application
import androidx.room.Room
import ru.samitin.weather.model.room.HistoryDao
import ru.samitin.weather.model.room.HistoryDataBase
import java.lang.IllegalStateException

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstace = this
    }

    companion object{
        private var appInstace:App?=null
        private var db: HistoryDataBase?=null
        private const val DB_NAME="History.db"

        fun getHistoryDao():HistoryDao{
            if (db == null){
                synchronized(HistoryDataBase::class.java){
                    if (db == null){
                        if (appInstace == null) throw IllegalStateException("Application is null while creating DataBase")
                        db = Room.databaseBuilder(appInstace!!.applicationContext, HistoryDataBase::class.java, DB_NAME)
                            .allowMainThreadQueries()
                            .build()
                    }
                }
            }
            return db!!.historyDao()
        }
    }

}