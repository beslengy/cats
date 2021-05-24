package com.molchanov.cats.ui.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentFavoritesBinding
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.ui.ImageItemAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.viewmodels.favorites.FavoritesViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by lazy {
        ViewModelProvider(this)
            .get(FavoritesViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//         Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
//        val binding = ImageItemBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = ImageItemAdapter(ItemClickListener({
            viewModel.displayCatCard(it)
        }, {
            viewModel.deleteFromFavorites(it)
//            Toast.makeText(context, "Сердечко нажато", Toast.LENGTH_LONG).show()
        }))

        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.addItemDecoration(Decoration(resources.getDimensionPixelOffset(R.dimen.rv_item_margin)))
        binding.rvFavorite.setHasFixedSize(true)

        viewModel.navigateToCard.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(
                    FavoritesFragmentDirections.actionFavoritesFragmentToCatCardFragment(it.imageId)
                )
                viewModel.displayCatCardComplete()
            }
        })

        //TODO: реализовать смену цвета при нажатии на сердечко через лайв дата
        //Добавляем возможность изменить цвет сердечка при нажатии через лайв дата
        //viewModel.checkFavorite.observe()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}