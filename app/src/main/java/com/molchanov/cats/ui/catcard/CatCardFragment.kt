package com.molchanov.cats.ui.catcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentCatCardBinding
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.viewmodels.catcard.CatCardViewModel
import com.molchanov.cats.viewmodels.catcard.CatCardViewModelFactory


class CatCardFragment : Fragment() {
    private var _binding : FragmentCatCardBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<CatCardFragmentArgs>()
    private val imageId by lazy { args.imageId }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        APP_ACTIVITY.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.GONE
        val application = requireNotNull(activity).application
        val viewModelFactory = CatCardViewModelFactory(imageId, application)
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cat_card, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(CatCardViewModel::class.java)


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        APP_ACTIVITY.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility = View.VISIBLE
    }
}