package com.dj.weather_mvvm.db

import androidx.room.*
import com.dj.weather_mvvm.model.LocationLatLng
import com.dj.weather_mvvm.model.WeatherInfo

@Dao
interface LocationLatLngDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationLatLng: LocationLatLng) : Long

    @Query("SELECT * FROM location_lat_lng ORDER BY id DESC LIMIT 1")
    suspend fun getLatestLatLng(): LocationLatLng

    @Query("DELETE FROM location_lat_lng")
    suspend fun deleteAll()
}