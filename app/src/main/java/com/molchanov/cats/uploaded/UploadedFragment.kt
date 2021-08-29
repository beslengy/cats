package com.molchanov.cats.uploaded

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentMainBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.*
import com.molchanov.cats.utils.CatImagePicker.Companion.getNewImageUri
import com.molchanov.cats.utils.Functions.setupManager
import com.molchanov.cats.utils.Global.CURRENT_IMAGE_URI
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

@AndroidEntryPoint
class UploadedFragment : Fragment(), ItemClickListener {
    private lateinit var binding: FragmentMainBinding

    private val viewModel: UploadedViewModel by activityViewModels()
    private val adapter = PageAdapter(this)
    private val headerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private val footerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private lateinit var decoration: Decoration
    private lateinit var manager: GridLayoutManager

    private val cameraContract = registerForActivityResult(PhotoContract()) {
        if (it) {
            prepareFilePart(CURRENT_IMAGE_URI)?.let { part ->
                viewModel.uploadFile(part)
            }
        }
    }
    private val galleryContract = registerForActivityResult(GalleryContract()) {
        if (it) {
            prepareFilePart(CURRENT_IMAGE_URI)?.let { part ->
                viewModel.uploadFile(part)
            }
        }
    }

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
        adapter.stateRestorationPolicy = PREVENT_WHEN_EMPTY

        binding.apply {
            rvMain.apply {
                this.adapter = this@UploadedFragment.adapter.withLoadStateHeaderAndFooter(
                    header = headerAdapter,
                    footer = footerAdapter
                )
                setHasFixedSize(true)
                addItemDecoration(decoration)
                layoutManager = setupManager(manager,
                    this@UploadedFragment.adapter,
                    footerAdapter,
                    headerAdapter)
            }
            srl.apply {
                setOnRefreshListener {
                    adapter.refresh()
                    this.isRefreshing = false
                }
            }
            fab.apply {
                isVisible = true
                setOnClickListener {
                    selectImage()
                }
            }
        }

        viewModel.rvIndex.observe(viewLifecycleOwner) {
            it?.let { index ->
                val top = viewModel.rvTop.value
                if (index != -1 && top != null) {
                    manager.scrollToPositionWithOffset(index, top)
                }
            }
        }

        viewModel.uploadedImages.observe(viewLifecycleOwner) {
            it?.let { adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }

        viewModel.navigateToAnalysis.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(
                    UploadedFragmentDirections.actionUploadedFragmentToCatCardFragment(analysis = it)
                )
                viewModel.displayAnalysisComplete()
            }
        })

        viewModel.onRefreshTrigger.observe(viewLifecycleOwner) {
            it?.let {
                adapter.refresh()
                viewModel.refreshComplete()
            }
        }

        //Настраиваем долгое нажатие на итем
        adapter.setItemLongTapAble(true)

        //Настраиваем видимость элементов в зависимости от состояния PagedList
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                progressBar.isVisible = loadState.source.refresh is LoadState.Loading
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

    /**
     * Метод возвращает находящийся по Uri файл в формате [MultipartBody.Part].
     * @param [fileUri] - [Uri] необходимого файла
     * @return [MultipartBody.Part]
     */
    private fun prepareFilePart(fileUri: Uri): MultipartBody.Part? {
        val file = File(fileUri.path!!)
        val stream = context?.contentResolver?.openInputStream(fileUri)
        val inputData: ByteArray? = stream?.readBytes()
        val requestFile: RequestBody? =
            inputData?.toRequestBody("image/jpeg".toMediaTypeOrNull())
        return requestFile?.let { MultipartBody.Part.createFormData("file", file.name, it) }
    }

    /**
     * Метод открывает [диалоговое окно][MaterialAlertDialogBuilder], в котором предоставляет
     * возможность выбрать источник изображения - камера или галерея. Выбор вызывает
     * соответствующие контракты - [PhotoContract] или [GalleryContract]
     */
    private fun selectImage() {
        val items = arrayOf(resources.getString(R.string.dialog_btn_camera),
            resources.getString(R.string.dialog_btn_gallery))
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(resources.getString(R.string.dialog_label))
            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setItems(items) { _, which ->
                when (items[which]) {
                    resources.getString(R.string.dialog_btn_camera) -> cameraContract.launch(
                        getNewImageUri(requireContext()))
                    resources.getString(R.string.dialog_btn_gallery) -> galleryContract.launch("image/*")
                }
            }
            .show()
    }

    private fun saveScroll() {
        val index = manager.findFirstVisibleItemPosition()
        val v: View? = binding.rvMain.getChildAt(0)
        val top = if (v == null) 0 else v.top - binding.rvMain.paddingTop
        viewModel.saveScrollPosition(index, top)
    }

    override fun onItemClicked(selectedImage: CatItem) {
        viewModel.displayAnalysis(selectedImage)
    }

    override fun onItemLongTap(selectedImage: CatItem) {
        viewModel.deleteImageFromServer(selectedImage)
    }

    override fun onFavoriteBtnClicked(selectedImage: CatItem) {}

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.setItemLongTapAble(false)
        saveScroll()
    }
}