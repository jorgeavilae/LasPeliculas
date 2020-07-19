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
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.jorgeav.laspeliculas.databinding.InsertListFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class InsertListFragment : Fragment() {

    private lateinit var binding : InsertListFragmentBinding
    private val viewModel: InsertListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = InsertListFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel

        binding.searchButtonInsertList.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.movieList.observe(viewLifecycleOwner) {movieList ->
            movieList?.let {
                binding.editTextInsertList.setText(it.id.toString())
                binding.movieListDetailsTextInsertList.text = "${it.name} \n\n${it.description}"
            }
        }

        viewModel.snackbar.observe(viewLifecycleOwner) {snackbar ->
            snackbar?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok) {
                        // Snackbar is dismissed by default when clicking the action
                    }
                    .show()
                viewModel.onSnackbarShown()
            }
        }

        setupEditText()

        return binding.root
    }

    private fun setupEditText() {
        binding.editTextInsertList.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_GO) {
                updateMovieListFromInput()
                true
            } else {
                false
            }
        }
        binding.editTextInsertList.setOnKeyListener { _, keyCode, event ->
            if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                updateMovieListFromInput()
                true
            } else {
                false
            }
        }
    }

    private fun updateMovieListFromInput() {
        binding.editTextInsertList.text?.trim().let {
            if (it != null && it.isNotEmpty()) {
                viewModel.searchListId(it.toString())
            }
        }
    }
}