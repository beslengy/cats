package com.molchanov.cats.ui.catcard

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentCatCardBinding
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.bindCardText
import com.molchanov.cats.utils.bindImage
import com.molchanov.cats.viewmodels.catcard.CatCardViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatCardFragment : Fragment(R.layout.fragment_cat_card) {
    private var _binding: FragmentCatCardBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CatCardViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        APP_ACTIVITY.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE
        _binding = FragmentCatCardBinding.bind(view)
        viewModel.cat.observe(viewLifecycleOwner) {
            it?.let { binding.apply {
                ivCatCardImage.bindImage(it.url)
                tvCatCardText.bindCardText(it) }
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