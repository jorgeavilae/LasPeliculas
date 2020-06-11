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

package com.jorgeav.laspeliculas.database.network.domain

import com.squareup.moshi.Json

data class MovieListExternal(
    val id: Int,
    val name: String,
    val description: String,
    val results: List<MovieListItemExternal>,
    @CreatorUsername
    @Json(name = "created_by")
    val creatorUsername: String
)

/*
{
  "iso_639_1": "es",
--------------------------------------------------------------"id": 105937,
  "page": 1,
  "iso_3166_1": "ES",
  "total_results": 2,
  "object_ids": {
    "movie:496243": "5a4db2bd0e0a26481d0042b8",
    "movie:530915": "5b280c5ec3a36864c600166c"
  },
  "revenue": 613951714,
  "total_pages": 1,
----------------------------------------------------------------"name": "HDD HDD HDD",
  "public": true,
  "comments": {
    "movie:496243": null,
    "movie:530915": null
  },
  "sort_by": "original_order.asc",
------------------------------------------------------------------"description": "Peliculas del HDD",
  "backdrop_path": null,
--------------------------------------------------------------------"results": [...]
  "average_rating": 8.24,
  "runtime": 252,
  "created_by": {
    "gravatar_hash": "c8b59687a5a791aa08edf664941bc8d6",
    "name": "",
    "username": "jorgeavila",
    "id": "58a81ad6c3a3686641005807"
  },
  "poster_path": null
 */
