package com.molchanov.cats.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentHomeBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.DECORATION
import com.molchanov.cats.utils.Functions.setupManager
import com.molchanov.cats.viewmodels.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), ItemClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var manager: Provider<GridLayoutManager>

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = PageAdapter(this)
    private val headerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private val footerAdapter = CatsLoadStateAdapter { adapter.retry() }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            rvHome.apply {
                adapter = this@HomeFragment.adapter.withLoadStateHeaderAndFooter(
                    header = headerAdapter,
                    footer = footerAdapter
                )
                addItemDecoration(DECORATION)
                setHasFixedSize(true)
                layoutManager = setupManager(manager.get(),
                    this@HomeFragment.adapter,
                    footerAdapter,
                    headerAdapter)
            }
            srlHome.apply {
                setOnRefreshListener {
                    adapter.refresh()
                    this.isRefreshing = false
                }
            }
            btnRetryHome.setOnClickListener {
                adapter.retry()
            }
        }
        //Настраиваем видимость элементов в зависимости от состояния
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                //Загрузка
                pbHome.isVisible = loadState.source.refresh is LoadState.Loading
                //Состояние просмотра
                rvHome.isVisible = loadState.source.refresh is LoadState.NotLoading
                //Ошибка
                btnRetryHome.isVisible = loadState.source.refresh is LoadState.Error
                tvErrorHome.isVisible = loadState.source.refresh is LoadState.Error
                ivErrorHome.isVisible = loadState.source.refresh is LoadState.Error
                //Пустой список
                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvHome.isVisible = false
                    tvEmptyHome.isVisible = true
                    ivEmptyHome.isVisible = true
                } else {
                    tvEmptyHome.isVisible = false
                    ivEmptyHome.isVisible = false
                }
            }
        }


        //Настраиваем видимость кнопки загрузки картинки
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE

        viewModel.homeImages.observe(viewLifecycleOwner)
        {
            it?.let { adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }

        viewModel.navigateToCard.observe(viewLifecycleOwner,
            { catItem ->
                catItem?.let {
                    this.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToCatCardFragment(it.id)
                    )
                    viewModel.displayCatCardComplete()
                }
            })
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