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

package com.jorgeav.laspeliculas.database.network

import android.util.Log
import com.jorgeav.laspeliculas.BuildConfig
import com.jorgeav.core.data.IExternalDataSource
import com.jorgeav.core.domain.MovieList
import com.jorgeav.laspeliculas.database.network.api.NetworkResponse
import com.jorgeav.laspeliculas.database.network.api.TheMovieDBApiService
import com.jorgeav.laspeliculas.database.network.domain.toMovieList
import javax.inject.Inject

class ExternalMovieDatabase @Inject constructor(
    private val theMovieDBApiService: TheMovieDBApiService) : IExternalDataSource {

    private val authV4: String = "Bearer " + BuildConfig.THE_MOVIE_DB_API_v4_TOKEN

    override suspend fun getList(listID: Int): MovieList? {
        val movieListExternal = theMovieDBApiService.getList(authV4, listID)
        when (movieListExternal) {
            is NetworkResponse.Success -> return movieListExternal.body.toMovieList()
            is NetworkResponse.ApiError -> Log.d("TAG", "getList: ApiError")
            is NetworkResponse.NetworkError -> Log.d("TAG", "getList: NetworkError")
            is NetworkResponse.UnknownError -> Log.d("TAG", "getList: UnknownError")
        }
        return null
    }
}

// Handle error in kotlin retrofit coroutines
// https://proandroiddev.com/create-retrofit-calladapter-for-coroutines-to-handle-response-as-states-c102440de37a
// https://dev.to/eagskunst/making-safe-api-calls-with-retrofit-and-coroutines-1121