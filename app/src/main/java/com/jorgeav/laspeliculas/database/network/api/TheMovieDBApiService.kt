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

package com.jorgeav.laspeliculas.database.network.api

import com.jorgeav.core.data.NetworkResponse
import com.jorgeav.laspeliculas.database.network.domain.MovieListExternal
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Path

interface TheMovieDBApiService {
    @Headers("content-type: application/json;charset=utf-8")
    @GET("/4/list/{listID}?page=1&language=es-ES")
    suspend fun getList(
        @Header("authorization") auth: String,
        @Path("listID") listID: Int
    ): NetworkResponse<MovieListExternal, Any>
}