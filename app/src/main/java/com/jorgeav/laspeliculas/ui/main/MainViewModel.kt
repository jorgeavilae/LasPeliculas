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

package com.jorgeav.laspeliculas.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jorgeav.core.data.Repository
import com.jorgeav.core.domain.MovieList
import com.jorgeav.laspeliculas.database.network.ExternalMovieDatabase
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val DEFAULT_LIST_ID: Int = 105937


    private val _movies = MutableLiveData<String>()
    val movies : LiveData<String>
        get() = _movies

    init {
        viewModelScope.launch {
            _movies.value = Repository(ExternalMovieDatabase()).getList(DEFAULT_LIST_ID)
        }
    }
}