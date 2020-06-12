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

package com.jorgeav.laspeliculas.database.room.domain

import com.jorgeav.core.domain.MovieList
import com.jorgeav.core.domain.MovieListItem

fun MovieListInternal.toMovieList(movieListItemInternalArray: Array<MovieListItemInternal>) : MovieList =
    MovieList(
        this.id,
        this.name,
        this.description,
        movieListItemInternalArray.map { it.toMovieListItem() },
        this.creatorUsername
    )

fun MovieListItemInternal.toMovieListItem() : MovieListItem =
    MovieListItem(
        this.id,
        this.title,
        this.overview,
        this.posterUrl
    )

fun MovieList.extractMovieListItemInternalArray() : Array<MovieListItemInternal> =
    this.results.map { it.toMovieListItemInternal() }.toTypedArray()

fun MovieList.extractListJoinMoviesArray() : Array<ListJoinMovie> =
    this.results.map { ListJoinMovie(this.id, it.id) }.toTypedArray()


fun MovieList.toMovieListInternal() : MovieListInternal =
    MovieListInternal(
        this.id,
        this.name,
        this.description,
        this.creatorUsername
    )

fun MovieListItem.toMovieListItemInternal() : MovieListItemInternal =
    MovieListItemInternal(
        this.id,
        this.title,
        this.overview,
        this.posterUrl
    )