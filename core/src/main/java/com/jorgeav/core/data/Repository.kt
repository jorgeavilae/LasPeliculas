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

package com.jorgeav.core.data

import com.jorgeav.core.domain.MovieList
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Repository @Inject constructor(private val externalDataSource: IExternalDataSource,
                                     private val internalDataSource: IInternalDataSource) {

    suspend fun refreshList(listID: Int) {
        val list = externalDataSource.getList(listID)
        internalDataSource.insertList(list)
    }

    suspend fun getList(listID: Int): MovieList = internalDataSource.getList(listID)
}