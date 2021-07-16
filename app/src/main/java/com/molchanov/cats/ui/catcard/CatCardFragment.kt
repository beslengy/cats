package com.molchanov.cats.ui.catcard

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
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
import com.molchanov.cats.R
import com.molchanov.cats.databinding.FragmentCatCardBinding
import com.molchanov.cats.ui.catcard.VoteStates.*
import com.molchanov.cats.utils.APP_ACTIVITY
import com.molchanov.cats.utils.BOTTOM_NAV_BAR
import com.molchanov.cats.utils.Functions.enableExpandedToolbar
import com.molchanov.cats.utils.Functions.setDraggableAppBar
import com.molchanov.cats.utils.VOTE_LAYOUT
import com.molchanov.cats.utils.bindCardText
import com.molchanov.cats.viewmodels.catcard.CatCardViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatCardFragment : Fragment(R.layout.fragment_cat_card) {

    private var _binding: FragmentCatCardBinding? = null
    private val binding get() = _binding!!

    private val args by navArgs<CatCardFragmentArgs>()

    private lateinit var voteState: VoteStates
    private val viewModel: CatCardViewModel by viewModels()

//    @Inject
//    @Named("voteUp")
//    lateinit var voteUpButton: Provider<ImageButton>
//
//    @Inject
//    @Named("voteDown")
//    lateinit var voteDownButton: Provider<ImageButton>

//    private val args by navArgs<CatCardFragmentArgs>()
//    private var voteValue: Int = args.voteValue

    private lateinit var voteUpButton : ImageButton
    private lateinit var voteDownButton : ImageButton

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
        Log.d("M_CatCardFragment", "onViewCreated")

        voteUpButton = VOTE_LAYOUT.getViewById(R.id.btn_like) as ImageButton
        voteDownButton = VOTE_LAYOUT.getViewById(R.id.btn_dislike) as ImageButton

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


    }
    override fun onStart() {
        super.onStart()
        Log.d("M_CatCardFragment", "onStart")

    }

    override fun onResume() {
        super.onResume()
        Log.d("M_CatCardFragment", "onResume")
        BOTTOM_NAV_BAR.visibility =
            View.GONE
        setDraggableAppBar(true)
        enableExpandedToolbar(true)
        VOTE_LAYOUT.isVisible = true
        voteUpButton = VOTE_LAYOUT.getViewById(R.id.btn_like) as ImageButton
        voteDownButton = VOTE_LAYOUT.getViewById(R.id.btn_dislike) as ImageButton
        setVoteButtons(voteState)
    }

    private fun setVoteButtons(voteState: VoteStates) {
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
        BOTTOM_NAV_BAR.visibility =
            View.VISIBLE
        VOTE_LAYOUT.isVisible = false
    }
}