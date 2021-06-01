package com.molchanov.cats.utils


import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.molchanov.cats.BuildConfig.APPLICATION_ID
import com.molchanov.cats.MainActivity
import com.molchanov.cats.repository.CatsRepository
import com.molchanov.cats.ui.Decoration

lateinit var APP_ACTIVITY: MainActivity
lateinit var REPOSITORY: CatsRepository
lateinit var DECORATION: Decoration
lateinit var FAV_QUERY_OPTIONS: Map<String, String>
lateinit var FAB: FloatingActionButton
lateinit var CURRENT_PHOTO_PATH: String


const val USER_ID = "user-17"
const val API_KEY = "x-api-key: 177e2034-b213-4178-834f-a3d237cc68ad"
const val BASE_URL = "https://api.thecatapi.com/v1/"
const val FILE_AUTHORITY = "${APPLICATION_ID}.provider"

enum class ApiStatus {
    LOADING,
    ERROR,
    DONE,
    EMPTY
}