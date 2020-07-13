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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.jorgeav.core.domain.MovieList
import com.jorgeav.core.domain.MovieListItem
import com.jorgeav.laspeliculas.databinding.MovieListItemBinding
import com.jorgeav.laspeliculas.databinding.MovieListItemHeaderBinding

/**
 * Created by Jorge Avila on 13/07/2020.
 */
class MovieListAdapter (private val headerListener: MovieListHeaderListener,
                        private val itemListener: MovieListItemListener) :
    ListAdapter<DataItem, RecyclerView.ViewHolder>(MovieDiffCallback) {
    private val ITEM_VIEW_TYPE_HEADER = 0
    private val ITEM_VIEW_TYPE_ITEM = 1

    // ListAdapter doesn't need to store a Collection of items.
    // Invoke adapter.submitList() with new data instead.

//    private val adapterScope = CoroutineScope(Dispatchers.Default)

    fun submitListAndAddHeader(movieList: MovieList?) {
//        adapterScope.launch {
            val items = when (movieList) {
                null -> emptyList<DataItem>()
                else -> listOf(DataItem.Header(movieList)) + movieList.results.map { DataItem.MovieItem(it) }
            }
//            withContext(Dispatchers.Main) {
                submitList(items)
//            }
//        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> MovieViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                val headerItem = getItem(position) as DataItem.Header
                holder.bind(headerListener, headerItem.movieList)
            }
            is MovieViewHolder -> {
                val movieItem = getItem(position) as DataItem.MovieItem
                holder.bind(itemListener, movieItem.movieListItem, (position != 1))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.MovieItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class HeaderViewHolder private constructor(private val binding: MovieListItemHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListItemHeaderBinding.inflate(layoutInflater, parent, false)

                return HeaderViewHolder(binding)
            }
        }

        fun bind(clickListener: MovieListHeaderListener, movieList: MovieList) {
            binding.movieList = movieList
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }
    }

    class MovieViewHolder private constructor(private val binding: MovieListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup): MovieViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = MovieListItemBinding.inflate(layoutInflater, parent, false)

                return MovieViewHolder(binding)
            }
        }

        fun bind(clickListener: MovieListItemListener, item: MovieListItem, showDivider: Boolean = true) {
            binding.movie = item
            binding.clickListener = clickListener
            binding.movieDivider.visibility = if (showDivider) View.VISIBLE else View.INVISIBLE
            binding.executePendingBindings()
        }
    }
}

class MovieListItemListener(val clickListener: (movieId: Int) -> Unit) {
    fun onClick(movieListItem: MovieListItem) = clickListener(movieListItem.id)
}

class MovieListHeaderListener(val clickListener: (movieListId: Int) -> Unit) {
    fun onClick(movieList: MovieList) = clickListener(movieList.id)
}

object MovieDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

sealed class DataItem {
    data class MovieItem(val movieListItem: MovieListItem): DataItem() {
        override val id = movieListItem.id
    }

    data class Header(val movieList: MovieList): DataItem() {
        override val id = Int.MIN_VALUE
    }

    abstract val id: Int
}