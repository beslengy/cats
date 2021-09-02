package com.molchanov.cats

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.color.MaterialColors
import com.molchanov.cats.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavBar: BottomNavigationView
    private lateinit var appBar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        appBar = findViewById(R.id.toolbar)
        bottomNavBar = binding.bottomNavigation

//        binding.toolbarImage.apply {
//            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//                maxHeight =
//                    resources.getDimensionPixelOffset(R.dimen.toolbar_image_landscape_max_height)
//                scaleType = ImageView.ScaleType.CENTER_CROP
//            } else {
//                binding.toolbarImage.maxHeight =
//                    resources.getDimensionPixelOffset(R.dimen.toolbar_image_portrait_max_height)
//                scaleType = ImageView.ScaleType.FIT_CENTER
//            }
//        }

        setSupportActionBar(findViewById(R.id.toolbar))
//        enableExpandedToolbar(false)
//        setDraggableAppBar(false)

        navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.homeFragment, R.id.favoritesFragment, R.id.uploadedFragment))
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration)
        NavigationUI.setupWithNavController(bottomNavBar, navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.favoritesFragment, R.id.uploadedFragment -> {
                    bottomNavBar.isVisible = true
                    appBar.background = ColorDrawable(MaterialColors.getColor(appBar, R.attr.colorPrimary))
//                    setViewsForMainFragments()
                }
                else -> {
                    appBar.background = ColorDrawable(Color.TRANSPARENT)
                    bottomNavBar.isVisible = false
//                    setViewsForCardFragment()
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController,
            appBarConfiguration) || super.onSupportNavigateUp()
    }

//    private fun enableExpandedToolbar(enable: Boolean) {
//        appBar.setExpanded(enable)
//    }
//
//    private fun setDraggableAppBar(isDraggable: Boolean) {
//        val params = appBar.layoutParams as CoordinatorLayout.LayoutParams
//        if (params.behavior == null)
//            params.behavior = AppBarLayout.Behavior()
//        val behavior = params.behavior as AppBarLayout.Behavior
//        behavior.setDragCallback(object : AppBarLayout.Behavior.DragCallback() {
//            override fun canDrag(appBarLayout: AppBarLayout): Boolean {
//                return isDraggable
//            }
//        })
//    }
//
//    private fun setViewsForMainFragments() {
//        bottomNavBar.isVisible = true
//        setDraggableAppBar(false)
//        enableExpandedToolbar(false)
//    }
//
//    private fun setViewsForCardFragment() {
//        bottomNavBar.isVisible = false
//        setDraggableAppBar(true)
//        enableExpandedToolbar(true)
//    }
}