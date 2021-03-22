package com.molchanov.cats.ui.catcard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentCatCardBinding
import com.molchanov.cats.viewmodels.catcard.CatCardViewModel
import com.molchanov.cats.viewmodels.catcard.CatCardViewModelFactory


class CatCardFragment : Fragment() {
    private var _binding : FragmentCatCardBinding? = null
    val binding get() = _binding!!

    private val args by navArgs<CatCardFragmentArgs>()
    private val imageId by lazy { args.imageId }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val application = requireNotNull(activity).application
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cat_card, container, false)
        binding.lifecycleOwner = this
        val viewModelFactory = CatCardViewModelFactory(imageId, application)
        binding.viewModel = ViewModelProvider(this, viewModelFactory).get(CatCardViewModel::class.java)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}