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

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jorgeav.laspeliculas.R
import com.jorgeav.laspeliculas.databinding.MainFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.recyclerviewList.layoutManager = LinearLayoutManager(this.activity)
        val movieListAdapter = MovieListAdapter(
            MovieListHeaderListener { movieListId ->
                Snackbar.make(binding.root, "Header clicked $movieListId", Snackbar.LENGTH_INDEFINITE).show()
            },
            MovieListItemListener { movieId ->
                Snackbar.make(binding.root, "Item clicked $movieId", Snackbar.LENGTH_INDEFINITE).show()
            }
        )
        binding.recyclerviewList.adapter = movieListAdapter

        viewModel.movies.observe(viewLifecycleOwner, Observer { movieList ->
            movieListAdapter.submitListAndAddHeader(movieList)
        })

        viewModel.eventNavigateToInsertList.observe(viewLifecycleOwner, Observer { navigate ->
            if (navigate != null && navigate == true) {
                findNavController().navigate(R.id.action_mainFragment_to_insertListFragment)
                viewModel.navigateToInsertListEventConsumed()
            }
        })
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        viewModel.loadData()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.add(Menu.NONE, R.id.insert_list_menu_item, Menu.NONE, R.string.new_list_id)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.insert_list_menu_item -> {
                findNavController().navigate(R.id.action_mainFragment_to_insertListFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}