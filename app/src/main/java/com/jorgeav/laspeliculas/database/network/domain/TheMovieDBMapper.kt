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

import com.jorgeav.core.domain.MovieList
import com.jorgeav.core.domain.MovieListItem

fun MovieListExternal.toMovieList(): MovieList =
    MovieList(
        this.id,
        this.name,
        this.description,
        this.results.map { it.toListOfMovieListItem() },
        ""
    )

fun MovieListItemExternal.toListOfMovieListItem(): MovieListItem =
    MovieListItem(
        this.id,
        this.title,
        this.overview,
        this.posterPath
    )