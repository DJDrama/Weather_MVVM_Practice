/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dj.weather_mvvm.db

import androidx.room.TypeConverter
import com.dj.weather_mvvm.model.Daily
import com.dj.weather_mvvm.model.FeelsLike
import com.dj.weather_mvvm.model.Temp
import com.dj.weather_mvvm.model.Weather
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {
    /** Need to find out how to inject moshi into this typeconverters class without creating a new moshi **/
    private var moshi: Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    //Daily
    private val dailyType = Types.newParameterizedType(List::class.java, Daily::class.java)
    private val dailyAdapter = moshi.adapter<List<Daily>>(dailyType)
    @TypeConverter
    fun stringToDailyList(string: String): List<Daily> {
        return dailyAdapter.fromJson(string).orEmpty()
    }

    @TypeConverter
    fun toDailyJson(dailys: List<Daily>): String {
        return dailyAdapter.toJson(dailys)
    }

    //Temp
    private val tempAdapter = moshi.adapter(Temp::class.java)
    @TypeConverter
    fun stringToTemp(string: String): Temp?{
        return tempAdapter.fromJson(string)
    }

    @TypeConverter
    fun toJson(temp: Temp): String {
        return tempAdapter.toJson(temp)
    }

    //FeelsLike
    private val feelsLikeAdapter = moshi.adapter(FeelsLike::class.java)
    @TypeConverter
    fun stringToFeelsLike(string: String): FeelsLike?{
        return feelsLikeAdapter.fromJson(string)
    }

    @TypeConverter
    fun toJson(feelsLike: FeelsLike): String {
        return feelsLikeAdapter.toJson(feelsLike)
    }

    //Weather
    private val weatherAdapter = moshi.adapter(Weather::class.java)
    @TypeConverter
    fun stringToWeather(string: String): Weather?{
        return weatherAdapter.fromJson(string)
    }

    @TypeConverter
    fun toJson(weather: Weather): String {
        return weatherAdapter.toJson(weather)
    }
}