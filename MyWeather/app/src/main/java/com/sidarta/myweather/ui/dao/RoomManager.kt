package com.sidarta.myweather.ui.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sidarta.myweather.dao.CityDao
import com.sidarta.myweather.entity.Cities
import com.sidarta.myweather.ui.favorites.FavoritesFragment

@Database(entities = [Cities::class], version = 1)
abstract class RoomManager: RoomDatabase() {
    abstract fun getCityDao() : CityDao

    companion object{
        var INSTANCE : RoomManager? = null

        fun getInstance(context: Context):RoomManager?{
            synchronized(RoomManager::class.java) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        RoomManager::class.java,
                        "weather.db"
                    ).allowMainThreadQueries()
                        .build()
                }
                return INSTANCE
            }
        }

    }
}
