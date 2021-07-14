package com.molchanov.cats.ui.catcard

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentCatCardBinding
import com.molchanov.cats.ui.catcard.VoteStates.*
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.Functions.enableExpandedToolbar
import com.molchanov.cats.utils.bindCardText
import com.molchanov.cats.viewmodels.catcard.CatCardViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import javax.inject.Named


@AndroidEntryPoint
class CatCardFragment : Fragment(R.layout.fragment_cat_card) {

    private var _binding: FragmentCatCardBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<CatCardFragmentArgs>()

    private lateinit var voteState: VoteStates
    private val viewModel: CatCardViewModel by viewModels()



//    private val args by navArgs<CatCardFragmentArgs>()
//    private var voteValue: Int = args.voteValue

    @Inject
    lateinit var voteLayout: ConstraintLayout

    @Inject
    @Named("voteUp")
    lateinit var voteUpButton: ImageButton

    @Inject
    @Named("voteDown")
    lateinit var voteDownButton: ImageButton

    companion object {
        private const val CARD_EMPTY_TITLE = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentCatCardBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        APP_ACTIVITY.findViewById<CollapsingToolbarLayout>(R.id.toolbar_layout).title =
            CARD_EMPTY_TITLE

        APP_ACTIVITY.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.GONE
        enableExpandedToolbar(true)

        voteLayout.isVisible = true
//        setVoteButtons(voteState)

        viewModel.cat.observe(viewLifecycleOwner) {
            it?.let {
                val imageView = APP_ACTIVITY.findViewById<ImageView>(R.id.toolbar_image)
                imageView.apply {
                    Glide.with(this@CatCardFragment)
                        .load(it.url)
                        .error(R.drawable.ic_broken_image)
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                binding.progressBar.isVisible = false
                                return false
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean,
                            ): Boolean {
                                binding.apply {
                                    progressBar.isVisible = false
                                    tvCatCardText.isVisible = true
                                }
                                return false
                            }
                        })
                        .into(this)
                }
                binding.tvCatCardText.bindCardText(it)
            }
        }

        viewModel.voteValue.observe(viewLifecycleOwner) { voteValue ->
            voteState = VoteStates.values().find {
                it.voteValue == voteValue
            }!!
            setVoteButtons(voteState)
        }

        voteUpButton.setOnClickListener {
            when(voteState) {
                NOT_VOTED, VOTE_DOWN -> {
                    viewModel.voteUp()
//                    voteState = VOTE_UP
                }
                VOTE_UP -> {
                    viewModel.removeVote()
//                    voteState = NOT_VOTED
                }
            }

        }
        voteDownButton.setOnClickListener {
            when(voteState) {
                NOT_VOTED, VOTE_UP -> {
                    viewModel.voteDown()
//                    voteState = VOTE_DOWN
                }
                VOTE_DOWN -> {
                    viewModel.removeVote()
//                    voteState = NOT_VOTED
                }
            }
        }
    }

    private fun setVoteButtons(voteState: VoteStates) {
        when (voteState) {
            VOTE_UP -> {
                voteUpButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_checked, APP_ACTIVITY.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_up_filled,
                        APP_ACTIVITY.theme))
                }
                voteDownButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, APP_ACTIVITY.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_down,
                        APP_ACTIVITY.theme))
                }
            }
            VOTE_DOWN -> {
                voteUpButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, APP_ACTIVITY.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_up,
                        APP_ACTIVITY.theme))
                }
                voteDownButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_checked, APP_ACTIVITY.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_down_filled,
                        APP_ACTIVITY.theme))
                }
            }
            NOT_VOTED -> {
                voteUpButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, APP_ACTIVITY.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_up,
                        APP_ACTIVITY.theme))
                }
                voteDownButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, APP_ACTIVITY.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_down,
                        APP_ACTIVITY.theme))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        enableExpandedToolbar(false)
        APP_ACTIVITY.findViewById<BottomNavigationView>(R.id.bottom_navigation).visibility =
            View.VISIBLE
        voteLayout.isVisible = false
    }
}