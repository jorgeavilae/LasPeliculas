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

package com.jorgeav.laspeliculas.ui.insertlist

import android.util.Log
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.navigation.fragment.findNavController
import com.jorgeav.core.data.NetworkResponse
import com.jorgeav.core.domain.MovieList
import com.jorgeav.core.interactors.GetCurrentListIDUseCase
import com.jorgeav.core.interactors.GetListUseCase
import com.jorgeav.core.interactors.RefreshListUseCase
import com.jorgeav.core.interactors.SetCurrentListIDUseCase
import com.jorgeav.laspeliculas.R
import kotlinx.coroutines.launch
import javax.inject.Inject

class InsertListViewModel @ViewModelInject constructor(
    private val getListUseCase: GetListUseCase,
    private val refreshListUseCase: RefreshListUseCase,
    private val setCurrentListIDUseCase: SetCurrentListIDUseCase,
    private val getCurrentListIDUseCase: GetCurrentListIDUseCase,
    @Assisted private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _snackbar = MutableLiveData<Int?>()
    val snackbar : LiveData<Int?>
        get() = _snackbar

    private val _movieList = MutableLiveData<MovieList?>()
    val movieList: LiveData<MovieList?>
        get() = _movieList

    init {
        viewModelScope.launch {
            getCurrentListIDUseCase()?.let {
                _movieList.value = getListUseCase(it)
            }
        }
    }

    fun searchListId(listIdStr: String?) {
        val listID: Int? = try {
            listIdStr?.toInt()
        } catch (e: NumberFormatException) {
            null
        }

        if (listID == null)
            _snackbar.value = R.string.number_format_error_movie_list
        else {
            viewModelScope.launch {
                val networkResponse = refreshListUseCase(listID)
                when (networkResponse) {
                    is NetworkResponse.Success -> {
                        setCurrentListIDUseCase(listID)
                        _movieList.value = getListUseCase(listID)
                    }
                    is NetworkResponse.ApiError -> _snackbar.value = R.string.api_error_movie_list
                    is NetworkResponse.NetworkError -> _snackbar.value = R.string.network_error_movie_list
                    is NetworkResponse.UnknownError -> _snackbar.value = R.string.unknown_error_movie_list
                }
            }
        }
    }

    fun onSnackbarShown() {
        _snackbar.value = null
    }
}
