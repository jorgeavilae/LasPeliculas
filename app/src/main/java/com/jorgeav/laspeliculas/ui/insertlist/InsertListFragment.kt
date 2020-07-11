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

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.jorgeav.core.data.NetworkResponse
import com.jorgeav.core.interactors.GetCurrentListIDUseCase
import com.jorgeav.core.interactors.RefreshListUseCase
import com.jorgeav.core.interactors.SetCurrentListIDUseCase
import com.jorgeav.laspeliculas.R
import com.jorgeav.laspeliculas.databinding.FragmentInsertListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class InsertListFragment : Fragment() {

    private lateinit var binding : FragmentInsertListBinding

    @Inject lateinit var getCurrentListIDUseCase: GetCurrentListIDUseCase
    @Inject lateinit var setCurrentListIDUseCase: SetCurrentListIDUseCase

    @Inject lateinit var refreshListUseCase: RefreshListUseCase

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.fragment_insert_list,
            container,
            false)


        lifecycleScope.launch {
            val currentListID = getCurrentListIDUseCase()
            if (currentListID != null)
                binding.editTextInsertList.setText(currentListID.toString())
        }

        binding.searchButtonInsertList.setOnClickListener {
            val listID: Int? = try {
                binding.editTextInsertList.text.toString().toInt()
            } catch (e: NumberFormatException) {
                null
            }

            storeListID(listID)

        }

        return binding.root
    }

    // todo move to a viewmodel or presenter
    private fun storeListID(listID: Int?) {
        if (listID == null)
            binding.errorTextInsertList.text = "Wrong number format"
        else {
            lifecycleScope.launch {
                val networkResponse = refreshListUseCase(listID)
                when (networkResponse) {
                    is NetworkResponse.Success -> {
                        setCurrentListIDUseCase(listID)
                        findNavController().navigateUp()
                    }
                    is NetworkResponse.ApiError -> binding.errorTextInsertList.text =
                        "List is not found or private"
                    is NetworkResponse.NetworkError -> binding.errorTextInsertList.text =
                        "No network connection"
                    is NetworkResponse.UnknownError -> binding.errorTextInsertList.text =
                        "Unknown error"
                }
            }
        }
    }
}