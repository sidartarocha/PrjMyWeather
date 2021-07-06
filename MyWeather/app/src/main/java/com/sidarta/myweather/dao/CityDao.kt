package com.sidarta.myweather.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sidarta.myweather.entity.Cities

@Dao
interface CityDao {
    @Insert
    fun insertCity(city: Cities)

    @Delete
    fun deleteCity(city: Cities)

    @Query("SELECT * FROM TB_CITIES WHERE id = :id")
    fun findFavorite(id:Int):Cities

    @Query("SELECT * FROM TB_CITIES")
    fun allFavoriteCities():List<Cities>

}