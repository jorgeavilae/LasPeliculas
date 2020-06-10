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

package com.jorgeav.laspeliculas.database.room

import android.content.Context
import com.jorgeav.core.data.IInternalDataSource
import com.jorgeav.core.domain.MovieList
import com.jorgeav.laspeliculas.database.room.api.MovieDatabase
import com.jorgeav.laspeliculas.database.room.domain.toMovieList

class InternalMovieDatabase(private val context: Context) : IInternalDataSource {

    private val movieRoomDao = MovieDatabase.getInstance(context).movieDatabaseDao

    override suspend fun getList(listID: Int): MovieList {
        val movieListInternal = movieRoomDao.getMovieList(listID)
        val movieListItemInternalArray = movieRoomDao.getMovieListItemForMovieList(listID)
        return movieListInternal.toMovieList(movieListItemInternalArray)
    }

}
