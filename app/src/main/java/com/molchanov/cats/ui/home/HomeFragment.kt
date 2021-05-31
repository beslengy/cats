package com.molchanov.cats.ui.home

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
import com.molchanov.cats.databinding.FragmentHomeBinding
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.ui.ImageItemAdapter
import com.molchanov.cats.ui.ItemClickListener
import com.molchanov.cats.viewmodels.home.HomeViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
            ViewModelProvider(this)
                .get(HomeViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//         Inflate the layout for this fragment
        _binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
//        val binding = ImageItemBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val adapter = ImageItemAdapter(ItemClickListener({
            viewModel.displayCatCard(it)
        }, {
            viewModel.addToFavorites(it)
//            Toast.makeText(context, "Сердечко нажато", Toast.LENGTH_LONG).show()
        }))

        binding.rvHome.adapter = adapter
        binding.rvHome.addItemDecoration(Decoration(resources.getDimensionPixelOffset(R.dimen.rv_item_margin)))
        binding.rvHome.setHasFixedSize(true)

        val refreshLayout = binding.srlHome
        refreshLayout.setOnRefreshListener {
            viewModel.getImages()
            refreshLayout.isRefreshing = false
        }

        //Настраиваем видимость кнопки загрузки картинки
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.visibility = View.GONE



        viewModel.navigateToCard.observe(viewLifecycleOwner, {
            if (it != null) {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToCatCardFragment(it.imageId)
                )
                viewModel.displayCatCardComplete()
            }
        })
        //TODO: реализовать смену цвета при нажатии на сердечко через лайв дата
        //Добавляем возможность изменить цвет сердечка при нажатии через лайв дата
        //viewModel.checkFavorite.observe()

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}