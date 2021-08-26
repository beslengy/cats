package com.molchanov.cats.utils


import android.net.Uri
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.molchanov.cats.BuildConfig.APPLICATION_ID
import com.molchanov.cats.MainActivity
import com.molchanov.cats.ui.Decoration

lateinit var APP_ACTIVITY: MainActivity
lateinit var DECORATION: Decoration
lateinit var CURRENT_PHOTO_PATH: String
lateinit var APP_BAR: AppBarLayout
lateinit var BOTTOM_NAV_BAR: BottomNavigationView
lateinit var VOTE_LAYOUT: ConstraintLayout
lateinit var CURRENT_IMAGE_URI: Uri

const val USER_ID = "user-17"
const val BASE_URL = "https://api.thecatapi.com/v1/"
const val FILE_AUTHORITY = "${APPLICATION_ID}.provider"
const val STARTING_PAGE_INDEX = 0

const val DEFAULT_FILTER_TYPE = "Любой"
const val BREEDS_FILTER_TYPE = "Порода"
const val CATEGORIES_FILTER_TYPE = "Категория"
