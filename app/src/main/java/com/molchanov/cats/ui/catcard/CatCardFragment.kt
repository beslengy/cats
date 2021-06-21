package com.molchanov.cats.ui.catcard

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentCatCardBinding
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.bindCardText
import com.molchanov.cats.viewmodels.catcard.CatCardViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatCardFragment : Fragment(R.layout.fragment_cat_card) {

    private var _binding: FragmentCatCardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatCardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCatCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE
        viewModel.cat.observe(viewLifecycleOwner) {
            it?.let {
                binding.apply {
                    ivCatCardImage.apply {
                        Glide.with(this@CatCardFragment)
                            .load(it.url)
                            .error(R.drawable.ic_broken_image)
                            .transition(DrawableTransitionOptions.withCrossFade())
                            .listener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean,
                                ): Boolean {
                                    progressBar.isVisible = false
                                    return false
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean,
                                ): Boolean {
                                    progressBar.isVisible = false
                                    tvCatCardText.isVisible = true
                                    return false
                                }
                            })
                            .into(this)
                    }
                    tvCatCardText.bindCardText(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        APP_ACTIVITY.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
    }
}