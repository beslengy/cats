package com.molchanov.cats

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.molchanov.cats.databinding.ActivityMainBinding
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.utils.*
import com.molchanov.cats.utils.Functions.enableExpandedToolbar
import com.molchanov.cats.utils.Functions.setDraggableAppBar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("M_MainActivity", "OnCreate")
        APP_ACTIVITY = this
        DECORATION = Decoration(resources.getDimensionPixelOffset(R.dimen.rv_item_margin))
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        APP_BAR = binding.appBar
        BOTTOM_NAV_BAR = binding.bottomNavigation
        VOTE_LAYOUT = binding.voteButtonsLayout.voteButtonsLayout
        binding.toolbarImage.apply {
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                maxHeight =
                    resources.getDimensionPixelOffset(R.dimen.toolbar_image_landscape_max_height)
                scaleType = ImageView.ScaleType.CENTER_CROP
            } else {
                binding.toolbarImage.maxHeight =
                    resources.getDimensionPixelOffset(R.dimen.toolbar_image_portrait_max_height)
                scaleType = ImageView.ScaleType.FIT_CENTER
            }
        }

        setSupportActionBar(findViewById(R.id.toolbar))

        enableExpandedToolbar(false)
        setDraggableAppBar(false)

//        val params = APP_BAR.layoutParams as CoordinatorLayout.LayoutParams
//        if (params.behavior == null)
//            params.behavior = AppBarLayout.Behavior()
//        val behavior = params.behavior as AppBarLayout.Behavior
//        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
//            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
//                return false
//            }
//        })

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment, R.id.favoritesFragment, R.id.uploadedFragment))
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(bottomNavigation, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,
            appBarConfiguration) || super.onSupportNavigateUp()
    }
}