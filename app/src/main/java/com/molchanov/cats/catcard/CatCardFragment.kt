package com.molchanov.cats.catcard

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.transition.MaterialContainerTransform
import com.molchanov.cats.R
import com.molchanov.cats.catcard.VoteStates.*
import com.molchanov.cats.databinding.FragmentCatCardBinding
import com.molchanov.cats.databinding.VoteLayoutBinding
import com.molchanov.cats.network.networkmodels.Analysis
import com.molchanov.cats.network.networkmodels.CatDetail
import com.molchanov.cats.utils.*
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CatCardFragment : Fragment() {

    private lateinit var binding: FragmentCatCardBinding

    private lateinit var voteState: VoteStates
    private val viewModel: CatCardViewModel by viewModels()

    private lateinit var voteUpButton: ImageButton
    private lateinit var voteDownButton: ImageButton
    private lateinit var voteLayoutBinding: VoteLayoutBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCatCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(requireContext().themeColor(R.attr.colorSurface))
        }
        voteLayoutBinding = VoteLayoutBinding.inflate(LayoutInflater.from(context))
        binding.voteButtonsLayout.root.isVisible = viewModel.analysis.value == null

        voteUpButton = binding.voteButtonsLayout.btnLike
        voteDownButton = binding.voteButtonsLayout.btnDislike

        //Настраиваем


        viewModel.cat.observe(viewLifecycleOwner) { catDetail ->
            catDetail?.let {
                setViews(detail = it)
            }
        }

        viewModel.voteValue.observe(viewLifecycleOwner) { voteValue ->
            voteState = VoteStates.values().find {
                it.voteValue == voteValue
            }!!
            setVoteButtons(voteState)
        }

        viewModel.analysis.observe(viewLifecycleOwner) { analysis ->
            analysis?.let{
                setViews(analysis = it)
            }
        }
    }

    private fun setViews(detail: CatDetail? = null, analysis: Analysis? = null) {
//        val imageView = requireActivity().findViewById<ImageView>(R.id.toolbar_image)
        binding.catCardImage.apply {
            Glide.with(this@CatCardFragment)
                .load(detail?.imageUrl ?: analysis?.imageUrl)
                .error(R.drawable.ic_broken_image)
//                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean,
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean,
                    ): Boolean {


                        return false
                    }
                })
                .into(this)
        }
        binding.apply {
            tvCatCardText.isVisible = true
            tvCatCardHeader.isVisible = true
            when(null) {
                detail -> {
                    tvCatCardText.setAnalysisText(analysis)
                    tvCatCardHeader.setText(R.string.cat_analysis_header)
                }
                analysis -> {
                    tvCatCardText.setCardText(detail)
                    detail.breeds?.let { tvCatCardHeader.setText(R.string.cat_card_header) }
                }
            }

        }
    }

    override fun onResume() {
        super.onResume()
//        setVoteButtons(voteState)
    }

    private fun setVoteButtons(voteState: VoteStates) {
        val activity = requireActivity()
        voteUpButton.setOnClickListener {
            when (voteState) {
                NOT_VOTED, VOTE_DOWN -> {
                    viewModel.voteUp()
                }
                VOTE_UP -> {
                    viewModel.removeVote()
                }
            }
        }
        voteDownButton.setOnClickListener {
            when (voteState) {
                NOT_VOTED, VOTE_UP -> {
                    viewModel.voteDown()
                }
                VOTE_DOWN -> {
                    viewModel.removeVote()
                }
            }
        }
        when (voteState) {
            VOTE_UP -> {
                voteUpButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_checked, activity.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_up_filled,
                        activity.theme))
                }
                voteDownButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, activity.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_down,
                        activity.theme))
                }
            }
            VOTE_DOWN -> {
                voteUpButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, activity.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_up,
                        activity.theme))
                }
                voteDownButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_checked, activity.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_down_filled,
                        activity.theme))
                }
            }
            NOT_VOTED -> {
                voteUpButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, activity.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_up,
                        activity.theme))
                }
                voteDownButton.apply {
                    background =
                        getDrawable(resources, R.drawable.btn_vote_unchecked, activity.theme)
                    setImageDrawable(getDrawable(resources,
                        R.drawable.thumb_down,
                        activity.theme))
                }
            }
        }
    }
}