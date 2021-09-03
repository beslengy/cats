package com.molchanov.cats.catcard

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.molchanov.cats.databinding.CatCardItemBinding
import com.molchanov.cats.databinding.FragmentCatCardBinding
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
    private lateinit var voteLayout: ConstraintLayout

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

        voteUpButton = requireActivity().findViewById(R.id.btn_like)
        voteDownButton = requireActivity().findViewById(R.id.btn_dislike)

        //Настраиваем видимость VoteLayout
        voteLayout = requireActivity().findViewById(R.id.vote_buttons_layout)
        voteLayout.isVisible = viewModel.analysis.value == null

        viewModel.cat.observe(viewLifecycleOwner) { catDetail ->
            catDetail?.let {
                setDetailView(it)
            }
        }
        viewModel.analysis.observe(viewLifecycleOwner) { analysis ->
            analysis?.let{
                setAnalysisView(it)
            }
        }
        viewModel.voteValue.observe(viewLifecycleOwner) { voteValue ->
            voteState = VoteStates.values().find {
                it.voteValue == voteValue
            }!!
            setVoteButtons(voteState)
        }
    }

    override fun onResume() {
        super.onResume()
        setVoteButtons(voteState)
    }

    private fun setImage(detail: CatDetail? = null, analysis: Analysis? = null) {
        binding.catCardImage.apply {
            Glide.with(this@CatCardFragment)
                .load(detail?.imageUrl ?: analysis?.imageUrl)
                .error(R.drawable.ic_broken_image)
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
            when(null) {
                detail -> {
                    tvCatCardHeader.setText(R.string.cat_analysis_header)
                }
                analysis -> {
                    detail.breeds?.let { tvCatCardHeader.setText(R.string.cat_card_header) }
                }
            }
        }
    }


    private fun setDetailView(detail: CatDetail) {
        setImage(detail = detail)
        var viewsCount = 1
        val data = detail.breeds?.get(0)
        data!!::class.members.forEach {
            val value = it.call(data)
            if (value != null || value != "") {
                if (!it.name.contains("component")) {
                    val itemBinding =
                        CatCardItemBinding.inflate(LayoutInflater.from(context)).apply {
                            tvPropertyName.text = it.name.formatVarName()
                            when (value) {
                                is String -> tvValue.apply {
                                    isVisible = true
                                    text = value
                                }
                                else -> ivGrade.apply {
                                    isVisible = true
                                    setImageDrawable(getDrawable(
                                        resources,
                                        when (value) {
                                            5 -> R.drawable.grade_5_img
                                            4 -> R.drawable.grade_4_img
                                            3 -> R.drawable.grade_3_img
                                            2 -> R.drawable.grade_2_img
                                            1 -> R.drawable.grade_1_img
                                            else -> R.drawable.grade_0_img
                                        },
                                        context.theme
                                    ))
                                }
                            }
                        }
                    when (value) {
                        is String -> {
                            binding.llCatInfo.addView(itemBinding.root, viewsCount)
                            viewsCount += 1
                        }
                        else -> binding.llCatInfo.addView(itemBinding.root)
                    }
                }
            }
        }
    }

    private fun setAnalysisView(analysis: Analysis) {
        setImage(analysis = analysis)
        binding.tvCatCardText.apply {
            isVisible = true
            setAnalysisText(analysis)
        }
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
    private fun String.formatVarName() : String {
        var result = ""
        this.forEachIndexed { index, c ->
            result = when (index) {
                0 -> "$result${c.uppercase()}"
                else -> if (c == c.uppercaseChar()) {
                    "$result ${c.lowercase()}"
                } else {
                    "$result$c"
                }
            }
        }
        return "$result:"
    }
}