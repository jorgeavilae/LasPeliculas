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

package com.jorgeav.laspeliculas.database.room.api

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.jorgeav.laspeliculas.database.room.domain.ListJoinMovie
import com.jorgeav.laspeliculas.database.room.domain.MovieListInternal
import com.jorgeav.laspeliculas.database.room.domain.MovieListItemInternal

@Dao
interface MovieDatabaseDao {

    @Insert
    fun insertMovieList(movieListInternal: MovieListInternal)

    @Insert
    fun insertMovieListItem(movieListItemInternal: MovieListItemInternal)

    @Insert
    fun insertListJoinMovie(listJoinMovie: ListJoinMovie)

    @Query("SELECT * FROM movie_list_table WHERE id = :listID")
    fun getMovieList(listID: Int): MovieListInternal

    @Query("""
        SELECT 
            id, 
            title, 
            overview, 
            posterUrl 
        FROM movie_list_item_table
        INNER JOIN movie_list_join_movie_table
        ON movie_list_item_table.id = movie_list_join_movie_table.movieID
        WHERE movie_list_join_movie_table.listID = :movieListID
    """)
    fun getMovieListItemForMovieList(movieListID: Int): Array<MovieListItemInternal>
}