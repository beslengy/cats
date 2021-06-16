package com.molchanov.cats.ui.uploaded

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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentUploadedBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.*
import com.molchanov.cats.utils.Functions.setupManager
import com.molchanov.cats.viewmodels.uploaded.UploadedViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Provider

@AndroidEntryPoint
class UploadedFragment : Fragment(R.layout.fragment_uploaded), ItemClickListener {
    private var _binding: FragmentUploadedBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var manager: Provider<GridLayoutManager>

    private val uploadedViewModel: UploadedViewModel by viewModels()
    private val adapter = PageAdapter(this)
    private val headerAdapter = CatsLoadStateAdapter { adapter.retry() }
    private val footerAdapter = CatsLoadStateAdapter { adapter.retry() }


    private val cameraContract = registerForActivityResult(PhotoContract()) {
        uploadedViewModel.uploadFileByUri(it)
    }
    private val galleryContract = registerForActivityResult(GalleryContract()) {
        uploadedViewModel.uploadFileByUri(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentUploadedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUploadedBinding.bind(view)


        binding.apply {
            rvUploaded.apply {
                this.adapter = this@UploadedFragment.adapter.withLoadStateHeaderAndFooter(
                    header = headerAdapter,
                    footer = footerAdapter
                )
                setHasFixedSize(true)
                addItemDecoration(DECORATION)
                layoutManager = setupManager(manager.get(),
                    this@UploadedFragment.adapter,
                    footerAdapter,
                    headerAdapter)
            }
            srlUploaded.apply {
                setOnRefreshListener {
                    adapter.refresh()
                    this.isRefreshing = false
                }
            }
        }

        uploadedViewModel.uploadedImages.observe(viewLifecycleOwner) {
            it?.let { adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }

        uploadedViewModel.navigateToCard.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(
                    UploadedFragmentDirections.actionUploadedFragmentToCatCardFragment(it.id)
                )
                uploadedViewModel.displayCatCardComplete()
            }
        })
        //Настраиваем видимость кнопки загрузки картинки
        FAB.visibility = View.VISIBLE
        FAB.setOnClickListener {
            selectImage()
        }

        adapter.addLoadStateListener { loadState ->
            binding.apply {
                pb.isVisible = loadState.source.refresh is LoadState.Loading
                rvUploaded.isVisible = loadState.source.refresh is LoadState.NotLoading
                btnRetry.isVisible = loadState.source.refresh is LoadState.Error
                tvError.isVisible = loadState.source.refresh is LoadState.Error
                ivError.isVisible = loadState.source.refresh is LoadState.Error

                if (loadState.source.refresh is LoadState.NotLoading &&
                    loadState.append.endOfPaginationReached &&
                    adapter.itemCount < 1
                ) {
                    rvUploaded.isVisible = false
                    tvEmpty.isVisible = true
                    ivEmpty.isVisible = true
                } else {
                    tvEmpty.isVisible = false
                    ivEmpty.isVisible = false
                }
            }
        }
    }


    private fun selectImage() {
        val items = arrayOf(resources.getString(R.string.dialog_btn_camera),
            resources.getString(R.string.dialog_btn_gallery))
        MaterialAlertDialogBuilder(APP_ACTIVITY)
            .setTitle(resources.getString(R.string.dialog_label))
            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setItems(items) { _, which ->
                when (items[which]) {
                    resources.getString(R.string.dialog_btn_camera) -> cameraContract.launch(
                        getNewImageUri())
                    resources.getString(R.string.dialog_btn_gallery) -> galleryContract.launch("image/*")
                }
            }
            .show()
    }

    override fun onItemClicked(selectedImage: CatItem) {
        uploadedViewModel.displayCatCard(selectedImage)
    }

    override fun onFavoriteBtnClicked(selectedImage: CatItem) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}