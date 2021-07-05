package com.molchanov.cats.utils


import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.molchanov.cats.BuildConfig.APPLICATION_ID
import com.molchanov.cats.MainActivity
import com.molchanov.cats.ui.Decoration

lateinit var APP_ACTIVITY: MainActivity
lateinit var DECORATION: Decoration
lateinit var FAB: FloatingActionButton
lateinit var CURRENT_PHOTO_PATH: String
lateinit var APP_BAR: AppBarLayout



const val USER_ID = "user-17"
const val BASE_URL = "https://api.thecatapi.com/v1/"
const val FILE_AUTHORITY = "${APPLICATION_ID}.provider"

const val DEFAULT_FILTER_TYPE = "Любой"
const val BREEDS_FILTER_TYPE = "Порода"
const val CATEGORIES_FILTER_TYPE = "Категория"
