package com.molchanov.cats.utils

import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.molchanov.cats.MainActivity
import com.molchanov.cats.repository.CatsRepository
import com.molchanov.cats.ui.Decoration

lateinit var APP_ACTIVITY: MainActivity
lateinit var REPOSITORY: CatsRepository
//lateinit var DATABASE: CatsDatabase
lateinit var DECORATION: Decoration
lateinit var FAV_QUERY_OPTIONS: Map<String, String>

lateinit var CIRCULAR_PROGRESS_DRAWABLE: CircularProgressDrawable

const val USER_ID = "user-17"
const val API_KEY = "x-api-key: 177e2034-b213-4178-834f-a3d237cc68ad"
const val BASE_URL = "https://api.thecatapi.com/v1/"