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

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.jorgeav.core.domain.MovieList
import com.jorgeav.core.interactors.GetCurrentListIDUseCase
import com.jorgeav.core.interactors.GetListUseCase
import com.jorgeav.core.interactors.RefreshListUseCase
import kotlinx.coroutines.launch

class MainViewModel @ViewModelInject constructor(
    private val getListUseCase: GetListUseCase,
    private val refreshListUseCase: RefreshListUseCase,
    private val getCurrentListIDUseCase: GetCurrentListIDUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _movies = MutableLiveData<MovieList>()
    val movies : LiveData<MovieList>
        get() = _movies

    private val _eventNavigateToInsertList = MutableLiveData<Boolean>()
    val eventNavigateToInsertList : LiveData<Boolean>
        get() = _eventNavigateToInsertList

    init {
        _eventNavigateToInsertList.value = false

        viewModelScope.launch {
            getCurrentListIDUseCase()?.let {
                _movies.value = getListUseCase(it)
            }?: navigateToInsertListEvent()
        }
    }

    private fun navigateToInsertListEvent() { _eventNavigateToInsertList.value = true }
    fun navigateToInsertListEventConsumed() { _eventNavigateToInsertList.value = false }
}