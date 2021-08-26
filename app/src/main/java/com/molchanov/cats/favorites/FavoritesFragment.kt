package com.molchanov.cats.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentFavoritesBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.DECORATION
import com.molchanov.cats.utils.Functions.setupManager
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites), ItemClickListener {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoritesViewModel by viewModels()
    private val adapter = PageAdapter(this)
    private val headerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private val footerAdapter = CatsLoadStateAdapter { adapter.retry() }

    @Inject
    lateinit var manager: Provider<GridLayoutManager>

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("M_FavoritesFragment", "onCreateView")
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("M_FavoritesFragment", "onViewCreated")

        binding.apply {
            rvFavorites.apply {
                adapter = this@FavoritesFragment.adapter.withLoadStateHeaderAndFooter(
                    header = headerAdapter,
                    footer = footerAdapter
                )
                addItemDecoration(DECORATION)
                setHasFixedSize(true)
                layoutManager = setupManager(
                    manager.get(),
                    this@FavoritesFragment.adapter,
                    footerAdapter,
                    headerAdapter
                )
            }
            btnRetry.setOnClickListener { adapter.retry() }
            srlFavorites.apply {
                setOnRefreshListener {
                    adapter.refresh()
                    this.isRefreshing = false
                }
            }
        }

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

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                pb.isVisible = loadState.refresh is LoadState.Loading
                rvFavorites.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error
                ivError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvFavorites.isVisible = false
                    tvEmpty.isVisible = true
                    ivEmpty.isVisible = true
                } else {
                    tvEmpty.isVisible = false
                    ivEmpty.isVisible = false
                }
            }
        }

    }


    override fun onItemClicked(selectedImage: CatItem) {
        viewModel.displayCatCard(selectedImage)
    }

    override fun onItemLongTap(selectedImage: CatItem) {}

    override fun onFavoriteBtnClicked(selectedImage: CatItem) {
        viewModel.deleteFromFavorites(selectedImage)
        binding.apply {
            adapter.refresh()
            pb.isVisible = false
            rvFavorites.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("M_FavoritesFragment", "onDestroyView")
        _binding = null
    }
}