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
import com.jorgeav.laspeliculas.database.network.api.TheMovieDBApiService
import javax.inject.Inject
import com.jorgeav.core.data.NetworkResponse
import com.jorgeav.core.domain.MovieList
import com.jorgeav.laspeliculas.database.network.domain.*

class ExternalMovieDatabase @Inject constructor(
    private val theMovieDBApiService: TheMovieDBApiService) : IExternalDataSource {

    private val authV4: String = "Bearer " + BuildConfig.THE_MOVIE_DB_API_v4_TOKEN

    override suspend fun getList(listID: Int): NetworkResponse<Any, Any> {
        val networkResponse = theMovieDBApiService.getList(authV4, listID)
        Log.d("ExternalMovieDatabase", "getList:networkResponse $networkResponse")
        when (networkResponse) {
            is NetworkResponse.Success -> {
                Log.d("ExternalMovieDatabase", "getList: Success")
                val movieList = queryMoviesFromAllListPages(networkResponse.body)
                return NetworkResponse.Success(movieList)
            }
            is NetworkResponse.ApiError -> Log.d("ExternalMovieDatabase", "getList: ApiError")
            is NetworkResponse.NetworkError -> Log.d("ExternalMovieDatabase", "getList: NetworkError")
            is NetworkResponse.UnknownError -> Log.d("ExternalMovieDatabase", "getList: UnknownError")
        }
        return networkResponse
    }

    private suspend fun queryMoviesFromAllListPages(movieListExternal: MovieListExternal): MovieList {
        val movieList: MovieList

        movieList =
            if (movieListExternal.total_pages > 1) {
                // Extract movies from page 1
                val allMoviesInList =
                    ArrayList(movieListExternal.extractMovieListItemsExternalAsMovieListItems())

                // Obtain movies for pages 2 to 'last page'
                for (page in 2..movieListExternal.total_pages) {
                    val networkResponse =
                        theMovieDBApiService.getList(authV4, movieListExternal.id, page)

                    if (networkResponse is NetworkResponse.Success)
                        allMoviesInList.addAll(
                            networkResponse.body.extractMovieListItemsExternalAsMovieListItems())
                    else break
                }

                // Create MovieList with all movies
                movieListExternal.toMovieListWithMovies(allMoviesInList)
            } else
                // Create MovieList with movies from page 1
                movieListExternal.toMovieList()

        return movieList
    }
}

// Handle error in kotlin retrofit coroutines
// https://proandroiddev.com/create-retrofit-calladapter-for-coroutines-to-handle-response-as-states-c102440de37a
// https://dev.to/eagskunst/making-safe-api-calls-with-retrofit-and-coroutines-1121