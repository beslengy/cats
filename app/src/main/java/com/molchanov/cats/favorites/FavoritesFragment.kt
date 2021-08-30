package com.molchanov.cats.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.transition.MaterialFadeThrough
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentMainBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.Functions.setupManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: FavoritesViewModel by activityViewModels()
    private val adapter = PageAdapter(this)
    private val headerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private val footerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private lateinit var decoration: Decoration
    private lateinit var manager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        exitTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        manager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        decoration = Decoration(resources.getDimensionPixelOffset(R.dimen.rv_item_margin))

        binding.apply {
            rvMain.apply {
                adapter = this@FavoritesFragment.adapter.withLoadStateHeaderAndFooter(
                    header = headerAdapter,
                    footer = footerAdapter
                )
                addItemDecoration(decoration)
                setHasFixedSize(true)
                layoutManager = setupManager(
                    manager,
                    this@FavoritesFragment.adapter,
                    footerAdapter,
                    headerAdapter
                )
            }
            btnRetry.setOnClickListener { adapter.retry() }
            srl.apply {
                setOnRefreshListener {
                    adapter.refresh()
                    this.isRefreshing = false
                }
            }
            fab.isVisible = false
        }

        viewModel.rvIndex.observe(viewLifecycleOwner) {
            it?.let { index ->
                val top = viewModel.rvTop.value
                if (index != -1 && top != null) {
                    manager.scrollToPositionWithOffset(index, top)
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
                progressBar.isVisible = loadState.refresh is LoadState.Loading
                rvMain.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error
                ivError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvMain.isVisible = false
                    tvEmpty.isVisible = true
                    ivEmpty.isVisible = true
                } else {
                    tvEmpty.isVisible = false
                    ivEmpty.isVisible = false
                }
            }
        }
    }

    //Переопределяем метод и добавляем обновление списка для реализации кейса:
    //прокручиваем избранные до конца -> переходим на главную -> добавляем картинку в избранное ->
    //-> возвращаемся на вкладку избранное -> новая картинка должна появиться в конце списка
    override fun onResume() {
        super.onResume()
        binding.apply {
            adapter.refresh()
            progressBar.isVisible = false
            rvMain.isVisible = true
        }
    }

    private fun saveScroll() {
        val index = manager.findFirstVisibleItemPosition()
        val v: View? = binding.rvMain.getChildAt(0)
        val top = if (v == null) 0 else v.top - binding.rvMain.paddingTop
        viewModel.saveScrollPosition(index, top)
    }

    override fun onItemClicked(selectedImage: CatItem, imageView: ImageView) {
        viewModel.displayCatCard(selectedImage)
    }

    override fun onItemLongTap(selectedImage: CatItem) {}

    override fun onFavoriteBtnClicked(selectedImage: CatItem) {
        viewModel.deleteFromFavorites(selectedImage)
        binding.apply {
            adapter.refresh()
            progressBar.isVisible = false
            rvMain.isVisible = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        saveScroll()
    }
}