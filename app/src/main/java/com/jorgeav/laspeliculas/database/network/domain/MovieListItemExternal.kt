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

import com.jorgeav.core.domain.MovieListItem
import com.squareup.moshi.Json

data class MovieListItemExternal(
    val id: Int,
    val title: String,
    val overview: String,
    @Json(name = "poster_path") val posterPath: String
)

fun List<MovieListItemExternal>.toListOfMovieListItem(): List<MovieListItem> {
    val result = mutableListOf<MovieListItem>()
    forEach { result.add(MovieListItem(it.id, it.title, it.overview, it.posterPath)) }
    return result
}

/*
{
---------------------------------------------------------------"poster_path": "/axdhbcZeOsfX3ZpwtgdgPIjc06l.jpg",
      "popularity": 98.403,
      "vote_count": 5277,
      "video": false,
      "media_type": "movie",
----------------------------------------------------------------"id": 530915,
      "adult": false,
      "backdrop_path": "/cqa3sa4c4jevgnEJwq3CMF8UfTG.jpg",
      "original_language": "en",
      "original_title": "1917",
      "genre_ids": [
        28,
        18,
        36,
        10752
      ],
---------------------------------------------------------------"title": "1917",
      "vote_average": 7.9,
--------------------------------------------------------------"overview": "Nos encontramos en el año 1917. La Guerra Mundial amenazaba con cambiar, para siempre, el orden mundial. Ante la amenaza que se cernía, Estados Unidos decidió entrar en el conflicto con el objetivo de desequilibrar la balanza que caracterizaba a la contienda.",
      "release_date": "2019-12-25"
    }
 */
