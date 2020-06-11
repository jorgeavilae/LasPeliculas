/*
 * Copyright 2020 Jorge Ávila
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.jorgeav.laspeliculas.database.room.api

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.jorgeav.laspeliculas.database.room.domain.ListJoinMovie
import com.jorgeav.laspeliculas.database.room.domain.MovieListInternal
import com.jorgeav.laspeliculas.database.room.domain.MovieListItemInternal

@Database(
    entities = [
        MovieListInternal::class,
        MovieListItemInternal::class,
        ListJoinMovie::class
    ],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract val movieDatabaseDao: MovieDatabaseDao

    companion object {
        private const val databaseName = "movie_database"

        fun getInstance(context: Context) : MovieDatabase {
            // https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
            return Room.databaseBuilder(context, MovieDatabase::class.java, databaseName)
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}