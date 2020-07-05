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
import com.jorgeav.laspeliculas.R
import com.jorgeav.laspeliculas.database.room.api.MovieDatabase
import com.jorgeav.laspeliculas.database.room.domain.*
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InternalMovieDatabase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val movieDatabase: MovieDatabase) : IInternalDataSource {

    override suspend fun getList(listID: Int): MovieList {
        val movieListInternal = movieDatabase.movieDatabaseDao().getMovieList(listID)
        val movieListItemInternalArray = movieDatabase.movieDatabaseDao().getMovieListItemForMovieList(listID)
        return movieListInternal.toMovieList(movieListItemInternalArray)
    }

    override suspend fun insertList(movieList: MovieList) {
        val movieLisInternal = movieList.toMovieListInternal()
        val arrayOfMovieListItemInternal = movieList.extractMovieListItemInternalArray()
        val arrayOfListJoinMovie = movieList.extractListJoinMoviesArray()

        movieDatabase.movieDatabaseDao().insertMovieListAndMovieListItems(
            movieLisInternal,
            arrayOfMovieListItemInternal,
            arrayOfListJoinMovie
        )
    }

    override suspend fun setCurrentListID(listID: Int) {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_name), Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putInt(context.getString(R.string.current_list_preference_key), listID)
            commit()
        }
    }

    override suspend fun getCurrentListID(): Int {
        val sharedPref = context.getSharedPreferences(
            context.getString(R.string.preference_file_name), Context.MODE_PRIVATE) ?: return -1
        return sharedPref.getInt(context.getString(R.string.current_list_preference_key), -1)
    }
}
