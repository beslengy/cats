package com.molchanov.cats.ui.uploaded

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentUploadedBinding
import com.molchanov.cats.network.networkmodels.CatItem
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.ui.PageAdapter
import com.molchanov.cats.utils.*
import com.molchanov.cats.viewmodels.uploaded.UploadedViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UploadedFragment : Fragment(R.layout.fragment_uploaded), ItemClickListener {
    private var _binding: FragmentUploadedBinding? = null
    private val binding get() = _binding!!


    private val uploadedViewModel: UploadedViewModel by viewModels()
    private val adapter = PageAdapter(this)


    private val cameraContract = registerForActivityResult(PhotoContract()) {
        uploadedViewModel.uploadFileByUri(it)
    }
    private val galleryContract = registerForActivityResult(GalleryContract()) {
        uploadedViewModel.uploadFileByUri(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentUploadedBinding.bind(view)

        binding.apply {
            rvUploaded.apply {
                this.adapter = adapter
                setHasFixedSize(true)
                addItemDecoration(DECORATION)
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
    }


    private fun selectImage() {
        val items = arrayOf(resources.getString(R.string.dialog_btn_camera), resources.getString(R.string.dialog_btn_gallery))
        MaterialAlertDialogBuilder(APP_ACTIVITY)
            .setTitle(resources.getString(R.string.dialog_label))
            .setNeutralButton(resources.getString(R.string.dialog_btn_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setItems(items) { _, which ->
                when (items[which]) {
                    resources.getString(R.string.dialog_btn_camera) -> cameraContract.launch(getNewImageUri())
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