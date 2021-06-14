package com.molchanov.cats.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentHomeBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.DECORATION
import com.molchanov.cats.viewmodels.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = PageAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        binding.apply {
            lifecycleOwner = this@HomeFragment
            viewModel = this@HomeFragment.viewModel
            rvHome.apply {
                adapter = this@HomeFragment.adapter
                addItemDecoration(DECORATION)
                setHasFixedSize(true)
            }
            srlHome.apply {
                setOnRefreshListener {
                    adapter.refresh()
                    this.isRefreshing = false
                }
            }
        }


        //Настраиваем видимость кнопки загрузки картинки
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        viewModel.homeImages.observe(viewLifecycleOwner) {
            it?.let { adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }

        viewModel.navigateToCard.observe(viewLifecycleOwner, { catItem ->
            catItem?.image?.let {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToCatCardFragment(it.id)
                )
                viewModel.displayCatCardComplete()
            }
        })

        //TODO: реализовать смену цвета при нажатии на сердечко через лайв дата

        return binding.root
    }

    override fun onItemClicked(selectedImage: CatItem) {
        viewModel.displayCatCard(selectedImage)
    }

    override fun onFavoriteBtnClicked(selectedImage: CatItem) {
        viewModel.addToFavorites(selectedImage)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}