package com.molchanov.cats.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentHomeBinding
import com.molchanov.cats.ui.Decoration
import com.molchanov.cats.viewmodels.home.HomeViewModel
import com.molchanov.cats.viewmodels.home.HomeViewModelFactory


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by lazy {
        val activity = requireNotNull(this.activity)
            ViewModelProvider(this, HomeViewModelFactory(activity.application))
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
        val adapter = HomeAdapter(ItemClickListener({
            viewModel.displayCatCard(it)
        }, {
            viewModel.addToFavorites(it)
//            Toast.makeText(context, "Сердечко нажато", Toast.LENGTH_LONG).show()
        }))

        binding.rvHome.adapter = adapter
        binding.rvHome.addItemDecoration(Decoration(resources.getDimensionPixelOffset(R.dimen.rv_item_margin)))



        viewModel.navigateToCard.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                this.findNavController().navigate(
                    HomeFragmentDirections.actionHomeFragmentToCatCardFragment(it.id)
                )
                viewModel.displayCatCardComplete()
            }
        })
        //TODO: реализовать смену цвета при нажатии на сердечко через лайв дата
        //Добавляем возможность изменить цвет сердечка при нажатии через лайв дата
        viewModel.checkFavorite.observe()


        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}