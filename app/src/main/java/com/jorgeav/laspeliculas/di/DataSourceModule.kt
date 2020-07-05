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

package com.jorgeav.laspeliculas.di

import android.content.Context
import com.jorgeav.core.data.IExternalDataSource
import com.jorgeav.core.data.IInternalDataSource
import com.jorgeav.laspeliculas.database.network.ExternalMovieDatabase
import com.jorgeav.laspeliculas.database.network.api.NetworkResponseAdapterFactory
import com.jorgeav.laspeliculas.database.network.api.TheMovieDBApiService
import com.jorgeav.laspeliculas.database.network.domain.ListCreatedByJsonAdapter
import com.jorgeav.laspeliculas.database.room.InternalMovieDatabase
import com.jorgeav.laspeliculas.database.room.api.MovieDatabase
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module(includes = [DataSourceModule.BindsModule::class])
class DataSourceModule {

    @InstallIn(ApplicationComponent::class)
    @Module
    abstract class BindsModule {
        @Singleton
        @Binds
        abstract fun bindsInternalDataSource(
            internalMovieDatabase: InternalMovieDatabase
        ): IInternalDataSource

        @Singleton
        @Binds
        abstract fun bindsExternalDataSource(
            externalMovieDatabase: ExternalMovieDatabase
        ): IExternalDataSource
    }

    @Singleton
    @Provides
    fun provideConverterFactory(): Converter.Factory {
        return MoshiConverterFactory.create(
            Moshi.Builder()
                .add(KotlinJsonAdapterFactory())
                .add(ListCreatedByJsonAdapter())
                .build()
        )
    }

    @Singleton
    @Provides
    fun provideTheMovieDBApiService(factory: Converter.Factory): TheMovieDBApiService {
        val baseUrl = "https://api.themoviedb.org"
        return Retrofit.Builder()
            .addConverterFactory(factory)
            .addCallAdapterFactory(NetworkResponseAdapterFactory())
            .baseUrl(baseUrl)
            .build()
            .create(TheMovieDBApiService::class.java)
    }

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): MovieDatabase {
        return MovieDatabase.getInstance(context)
    }

}