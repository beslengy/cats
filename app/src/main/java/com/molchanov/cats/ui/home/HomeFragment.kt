package com.molchanov.cats.ui.home

import android.os.Bundle
import android.util.Log
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
import com.molchanov.cats.databinding.FragmentHomeBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.network.networkmodels.FilterItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.*
import com.molchanov.cats.utils.Functions.setupManager
import com.molchanov.cats.viewmodels.home.HomeViewModel
import com.molchanov.cats.viewmodels.home.HomeViewModel.Companion.ToastRequest.*
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
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
    private lateinit var itemMenuAdapter: ArrayAdapter<String>


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        Log.d("M_HomeFragment", "onCreateView")
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("M_HomeFragment", "onViewCreated")

        //Настраиваем recyclerView
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

        //Настраиваем видимость элементов в зависимости от состояния PagedList
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
        //Наблюдатель для показа тоста
        viewModel.toast.observe(viewLifecycleOwner) {
            Log.d("M_HomeFragment", "Toast Request: $it")
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
        Log.d("M_HomeFragment", "$selectedImage")
        viewModel.displayCatCard(selectedImage)
    }

    override fun onItemLongTap(selectedImage: CatItem) {}

    //Прослушиватель нажатия на кнопку "сердечко"
    override fun onFavoriteBtnClicked(selectedImage: CatItem) {
        Log.d("M_HomeFragment",
            "selected image - $selectedImage, isFav = ${selectedImage.isFavorite}")
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
                binding.rvHome.scrollToPosition(0)
                dialog.dismiss()
            }

            dialog.setContentView(view)
            dialog.show()

            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("M_HomeFragment", "onDestroyView()")
        _binding = null
    }
}