package com.molchanov.cats.home

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.LinearLayout
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentFilterBinding
import com.molchanov.cats.databinding.FragmentMainBinding
import com.molchanov.cats.home.HomeViewModel.Companion.BREEDS_FILTER_TYPE
import com.molchanov.cats.home.HomeViewModel.Companion.CATEGORIES_FILTER_TYPE
import com.molchanov.cats.home.HomeViewModel.Companion.DEFAULT_FILTER_TYPE
import com.molchanov.cats.home.HomeViewModel.Companion.ToastRequest.*
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.FilterItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.*
import com.molchanov.cats.utils.Functions.setupManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(), ItemClickListener {

    private lateinit var binding: FragmentMainBinding

    private val viewModel: HomeViewModel by viewModels()
    private val adapter = PageAdapter(this)
    private val headerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private val footerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private lateinit var decoration: Decoration
    private lateinit var itemMenuAdapter: ArrayAdapter<String>
    private lateinit var manager: GridLayoutManager


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        decoration = Decoration(resources.getDimensionPixelOffset(R.dimen.rv_item_margin))

        //Настраиваем recyclerView
        binding.apply {
            rvMain.apply {
                adapter = this@HomeFragment.adapter.withLoadStateHeaderAndFooter(
                    header = headerAdapter,
                    footer = footerAdapter
                )
                addItemDecoration(decoration)
                setHasFixedSize(true)
                layoutManager = setupManager(manager,
                    this@HomeFragment.adapter,
                    footerAdapter,
                    headerAdapter)
            }
            srl.apply {
                setOnRefreshListener {
                    adapter.refresh()
                    this.isRefreshing = false
                }
            }
            btnRetry.setOnClickListener {
                adapter.retry()
            }
            fab.isVisible = false
        }

        //Настраиваем видимость элементов в зависимости от состояния PagedList
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                //Загрузка
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
                //Состояние просмотра
                rvMain.isVisible = loadState.source.refresh is LoadState.NotLoading
                //Ошибка
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error
                ivError.isVisible = loadState.source.refresh is LoadState.Error
                //Пустой список
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
        //Наблюдатель для показа тоста
        viewModel.toast.observe(viewLifecycleOwner) {
            it?.let { request ->
                context?.showToast(
                    when (request) {
                        ADD_FAV -> R.string.added_to_favorite_toast_text
                        ADD_FAV_FAIL -> R.string.already_added_to_favorite_toast_text
                        DELETE_FAV -> R.string.deleted_from_favorites_toast_text
                        DELETE_FAV_FAIL -> R.string.already_deleted_from_favorites_toast_text
                    }
                )
                viewModel.toastShowComplete()
            }
        }
        //Наблюдатель списка картинок. Обновляет адаптер при изменении
        viewModel.homeImages.observe(viewLifecycleOwner)
        {
            it?.let { adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }

        //Наблюдатель переменной навигации.
        viewModel.navigateToCard.observe(viewLifecycleOwner,
            { catItem ->
                catItem?.let {
                    this.findNavController().navigate(
                        HomeFragmentDirections.actionHomeFragmentToCatCardFragment(it.id)
                    )
                    viewModel.displayCatCardComplete()
                }
            })
        setHasOptionsMenu(true)
    }

    //Прослушиватель нажатия на элемент recyclerView
    override fun onItemClicked(selectedImage: CatItem) {
        viewModel.displayCatCard(selectedImage)
    }

    override fun onItemLongTap(selectedImage: CatItem) {}

    //Прослушиватель нажатия на кнопку "сердечко"
    override fun onFavoriteBtnClicked(selectedImage: CatItem) {
        if (!selectedImage.isFavorite) {
            viewModel.addToFavorites(selectedImage)
        } else {
            viewModel.deleteFromFavorites(selectedImage)
        }
    }

    //Фильтр
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_home, menu)

        val filterItem = menu.findItem(R.id.action_filter)
        filterItem.setOnMenuItemClickListener {
            val dialog = BottomSheetDialog(requireContext())
            val view = LayoutInflater.from(context).inflate(R.layout.fragment_filter,
                requireActivity().findViewById(R.id.ll_filter) as LinearLayout?)
            val dialogBinding = FragmentFilterBinding.bind(view)
            val typeMenu = dialogBinding.menuFilterType.editText as? AutoCompleteTextView
            val itemsMenu = dialogBinding.menuFilterItem.editText as? AutoCompleteTextView
            val typeItems = resources.getStringArray(R.array.filter_types)
            val typeMenuAdapter =
                ArrayAdapter(requireContext(), R.layout.dropdown_menu_item, typeItems)

            viewModel.currentFilterItem.observe(viewLifecycleOwner) {
                itemsMenu?.setText(it?.name, false)
            }

            viewModel.currentFilterType.observe(viewLifecycleOwner) { type ->
                dialogBinding.menuFilterItem.apply {
                    hint = if (type != DEFAULT_FILTER_TYPE) type else null
                    isEnabled = (type != DEFAULT_FILTER_TYPE)
                }

                val items: List<FilterItem> = when (type) {
                    BREEDS_FILTER_TYPE -> viewModel.breeds.value!!
                    CATEGORIES_FILTER_TYPE -> viewModel.categories.value!!
                    else -> listOf()
                }

                itemMenuAdapter = ArrayAdapter(requireContext(),
                    R.layout.dropdown_menu_item,
                    (items.map { it.name }))
                itemsMenu?.apply {
                    setAdapter(itemMenuAdapter)
                    setOnItemClickListener { _, _, position, _ ->
                        viewModel.setFilterItem(items[position])
                    }
                }
            }

            typeMenu?.apply {
                if (viewModel.currentFilterItem.value == null) {
                    viewModel.setFilterType(DEFAULT_FILTER_TYPE)
                }
                setText(viewModel.currentFilterType.value, false)
                setAdapter(typeMenuAdapter)

                setOnItemClickListener { _, _, position, _ ->
                    viewModel.setFilterType(typeItems[position])
                }
            }
            dialogBinding.btnClose.setOnClickListener {
                viewModel.setQuery()
                binding.rvMain.scrollToPosition(0)
                dialog.dismiss()
            }

            dialog.setContentView(view)
            dialog.show()

            true
        }
    }
}