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

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "movie_list_join_movie",
    primaryKeys = arrayOf("listID", "movieID"),
    foreignKeys = arrayOf(
        ForeignKey(
            entity = MovieListInternal::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("listID")
        ),
        ForeignKey(
            entity = MovieListItemInternal::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("movieID")
        )
    )
)
data class ListJoinMovie (
    val listID: Int,

    @ColumnInfo(index = true)
    val movieID: Int
)