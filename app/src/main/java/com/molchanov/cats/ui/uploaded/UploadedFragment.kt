package com.molchanov.cats.ui.uploaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentUploadedBinding
import com.molchanov.cats.ui.ImageItemAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.utils.*
import com.molchanov.cats.viewmodels.uploaded.UploadedViewModel

class UploadedFragment : Fragment() {
    private var _binding: FragmentUploadedBinding? = null
    private val binding get() = _binding!!


    private val uploadedViewModel: UploadedViewModel by lazy {
        ViewModelProvider(this).get(UploadedViewModel::class.java)
    }


    private val cameraContract = registerForActivityResult(PhotoContract()) {
        uploadedViewModel.uploadFileByUri(it)
    }
    private val galleryContract = registerForActivityResult(GalleryContract()) {
        uploadedViewModel.uploadFileByUri(it)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_uploaded, container, false)
        val adapter = ImageItemAdapter(ItemClickListener({
            uploadedViewModel.displayCatCard(it)
        }, {}))
        binding.apply {
            lifecycleOwner = this@UploadedFragment
            viewModel = uploadedViewModel

            rvUploaded.apply {
                this.adapter = adapter
                setHasFixedSize(true)
                addItemDecoration(DECORATION)
            }

        }
        uploadedViewModel.navigateToCard.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(
                    UploadedFragmentDirections.actionUploadedFragmentToCatCardFragment(it.imageId)
                )
                uploadedViewModel.displayCatCardComplete()
            }
        })
        //Настраиваем видимость кнопки загрузки картинки
        FAB.visibility = View.VISIBLE
        FAB.setOnClickListener {
            selectImage()
        }
        return binding.root
    }

    private fun selectImage() {
        val items = arrayOf("Камера", "Галерея")
        MaterialAlertDialogBuilder(APP_ACTIVITY)
            .setTitle("Загрузить изображение")
            .setNeutralButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .setItems(items) { _, which ->
                when (items[which]) {
                    "Камера" -> cameraContract.launch(getNewImageUri())
                    "Галерея" -> galleryContract.launch("image/*")
                }
            }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}