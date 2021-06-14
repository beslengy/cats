package com.molchanov.cats.ui.favorites

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
import com.molchanov.cats.databinding.FragmentFavoritesBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.viewmodels.favorites.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(), ItemClickListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter = PageAdapter(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.addItemDecoration(Decoration(resources.getDimensionPixelOffset(R.dimen.rv_item_margin)))
        binding.rvFavorite.setHasFixedSize(true)

        //Настраиваем видимость кнопки загрузки картинки
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        viewModel.favoriteImages.observe(viewLifecycleOwner) {
            it?.let { adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }

        viewModel.navigateToCard.observe(viewLifecycleOwner, { catItem ->
            catItem?.image?.let {
                this.findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToCatCardFragment(it.id)
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
        viewModel.deleteFromFavorites(selectedImage)
        adapter.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}