package com.molchanov.cats.ui.uploaded

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentUploadedBinding
import com.molchanov.cats.ui.ImageItemAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.utils.DECORATION
import com.molchanov.cats.viewmodels.uploaded.UploadedViewModel

class UploadedFragment: Fragment() {
    private var _binding:FragmentUploadedBinding? = null
    private val binding get() = _binding!!

    private val uploadedViewModel: UploadedViewModel by lazy {
        ViewModelProvider(this).get(UploadedViewModel::class.java)
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
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.VISIBLE



        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}