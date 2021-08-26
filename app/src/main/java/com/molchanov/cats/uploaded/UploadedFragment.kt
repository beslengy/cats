package com.molchanov.cats.uploaded

import android.net.Uri
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentUploadedBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.CatsLoadStateAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.*
import com.molchanov.cats.utils.Functions.setupManager
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
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
        Log.d("M_UploadedFragment", "Camera contract result: $it")
        if (it) {
            prepareFilePart(CURRENT_IMAGE_URI)?.let { part->
                uploadedViewModel.uploadFile(part)
            }
        }
    }
    private val galleryContract = registerForActivityResult(GalleryContract()) {
        Log.d("M_UploadedFragment", "Gallery contract result: $it")
        if (it) {
            prepareFilePart(CURRENT_IMAGE_URI)?.let { part ->
                uploadedViewModel.uploadFile(part)
            }
        }
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
            fab.setOnClickListener {
                selectImage()
            }
        }

        uploadedViewModel.uploadedImages.observe(viewLifecycleOwner) {
            it?.let { adapter.submitData(viewLifecycleOwner.lifecycle, it) }
        }

        uploadedViewModel.navigateToAnalysis.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(
                    UploadedFragmentDirections.actionUploadedFragmentToCatCardFragment(analysis = it)
                )
                uploadedViewModel.displayAnalysisComplete()
            }
        })

        uploadedViewModel.onRefreshTrigger.observe(viewLifecycleOwner) {
            it?.let {
                Log.d("M_UploadedFragment", "onImageUploaded сработал. Параметр: $it")
                adapter.refresh()
            }
        }




        //Настраиваем видимость кнопки загрузки картинки


        //Настраиваем долгое нажатие на итем
        adapter.setItemLongTapAble(true)

        //Настраиваем видимость элементов в зависимости от состояния PagedList
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
        uploadedViewModel.displayAnalysis(selectedImage)
    }
    override fun onItemLongTap(selectedImage: CatItem) {
        uploadedViewModel.deleteImageFromServer(selectedImage)
    }

    override fun onFavoriteBtnClicked(selectedImage: CatItem) {}

    override fun onDestroyView() {
        super.onDestroyView()
        adapter.setItemLongTapAble(false)
        _binding = null
    }
}