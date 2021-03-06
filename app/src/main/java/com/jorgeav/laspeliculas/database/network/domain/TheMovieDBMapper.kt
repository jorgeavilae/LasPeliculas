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
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonQualifier
import com.squareup.moshi.ToJson

fun MovieListExternal.toMovieList(): MovieList =
    MovieList(
        this.id,
        this.name,
        this.description,
        this.results.map { it.toMovieListItem() },
        this.creatorUsername
    )

fun MovieListItemExternal.toMovieListItem(): MovieListItem =
    MovieListItem(
        this.id,
        this.title,
        this.overview,
        this.posterPath
    )

fun MovieListExternal.toMovieListWithMovies(movies: List<MovieListItem>): MovieList =
    MovieList(
        this.id,
        this.name,
        this.description,
        movies,
        this.creatorUsername
    )

fun MovieListExternal.extractMovieListItemsExternalAsMovieListItems(): List<MovieListItem> =
    this.results.map { it.toMovieListItem() }


@Retention(AnnotationRetention.RUNTIME)
@JsonQualifier
annotation class CreatorUsername

class ListCreatedByJsonAdapter {
    @FromJson
    @CreatorUsername
    fun fromJson(json: Map<String, Any?>): String {
        return json.getValue("username") as String
    }

    @ToJson
    fun toJson(@CreatorUsername username: String): Map<String, Any?> {
        return mapOf(
            Pair("gravatar_hash",""),
            Pair("name",""),
            Pair("username",username),
            Pair("id","")
        )
    }

    /*
        "created_by": {
            "gravatar_hash": "c8b59687a5a791aa08edf664941bc8d6",
            "name": "",
            "username": "jorgeavila",
            "id": "58a81ad6c3a3686641005807"
        },
    */
}