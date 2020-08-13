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
import java.util.*

/**
 * Type converters to allow Room to reference complex data types.
 */
class Converters {

//    @TypeConverter
//    fun dailyListToString(value: String?): List<String?>? {
//        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.getType()
//        return Gson().fromJson(value, listType)
//    }
//
//    @TypeConverter
//    fun stringToDailyList(list: ArrayList<String?>?): String? {
//
//    }
}