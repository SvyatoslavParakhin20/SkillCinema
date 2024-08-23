package com.skillcinema.ui.searchSettings

import android.annotation.SuppressLint
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.slider.RangeSlider
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.skillcinema.R
import com.skillcinema.data.FilmFilters
import com.skillcinema.databinding.FragmentSearchSettingsBinding
import com.skillcinema.ui.search.SearchSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchSettings : Fragment() {

    private var _binding: FragmentSearchSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        customizeTabs()
        customizeRangeSlider()
        with(binding) {
            goBack.setOnClickListener { findNavController().popBackStack() }
            countryItem.text = if (SearchSettings.countries.isNullOrEmpty())
                context?.getString(R.string.any_settings) else FilmFilters.getCountryNameById(
                SearchSettings.countries!![0]
            )
            countryItem.setOnClickListener { findNavController().navigate(R.id.action_searchSettings_to_countrySelection) }
            genreItem.text =
                if (SearchSettings.genres.isNullOrEmpty()) context?.getString(R.string.genre_settings_item) else FilmFilters.getGenreNameById(
                    SearchSettings.genres!![0]
                )
            genreItem.setOnClickListener { findNavController().navigate(R.id.action_searchSettings_to_genreSelection) }
            yearItem.setOnClickListener { findNavController().navigate(R.id.action_searchSettings_to_yearsSelection) }
            yearItem.text = "с ${SearchSettings.yearFrom} до ${SearchSettings.yearTo}"
            notWatchedCheckBox.isChecked = SearchSettings.notWatchedOnly
            notWatchedCheckBox.setOnCheckedChangeListener { _, p1 ->
                if (p1) {
                    notWatchedCheckBox.isChecked = true
                    SearchSettings.notWatchedOnly = true
                } else {
                    notWatchedCheckBox.isChecked = false
                    SearchSettings.notWatchedOnly = false
                }
            }
        }
    }

    private fun customizeTabs() {
        when (SearchSettings.type) {
            "ALL" -> binding.filmsSeriesTabLayout.selectTab(binding.filmsSeriesTabLayout.getTabAt(0))
            "FILM" -> binding.filmsSeriesTabLayout.selectTab(binding.filmsSeriesTabLayout.getTabAt(1))
            "TV_SERIES" -> binding.filmsSeriesTabLayout.selectTab(binding.filmsSeriesTabLayout.getTabAt(2))
        }
        val tabCategory = binding.filmsSeriesTabLayout.getChildAt(0)
        if (tabCategory is LinearLayout) {
            tabCategory.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawable = GradientDrawable()
            drawable.setColor(ContextCompat.getColor(requireContext(), R.color.grey))
            drawable.setSize(5, 5)
            tabCategory.dividerPadding = 10
            tabCategory.dividerDrawable = drawable
        }
        binding.filmsSeriesTabLayout.addOnTabSelectedListener(object: OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position){
                        0 -> SearchSettings.type = "ALL"
                        1 -> SearchSettings.type = "FILM"
                        2 -> SearchSettings.type = "TV_SERIES"
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
        when (SearchSettings.order) {
            "YEAR" -> binding.sortByTabLayout.selectTab(binding.sortByTabLayout.getTabAt(0))
            "NUM_VOTE" -> binding.sortByTabLayout.selectTab(binding.sortByTabLayout.getTabAt(1))
            "RATING" -> binding.sortByTabLayout.selectTab(binding.sortByTabLayout.getTabAt(2))
        }
        val tabSortOrder = binding.sortByTabLayout.getChildAt(0)
        if (tabSortOrder is LinearLayout) {
            tabSortOrder.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
            val drawableTwo = GradientDrawable()
            drawableTwo.setColor(ContextCompat.getColor(requireContext(), R.color.grey))
            drawableTwo.setSize(5, 5)
            tabSortOrder.dividerPadding = 10
            tabSortOrder.dividerDrawable = drawableTwo
        }
        binding.sortByTabLayout.addOnTabSelectedListener(object: OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null) {
                    when (tab.position){
                        0 -> SearchSettings.order = "YEAR"
                        1 -> SearchSettings.order = "NUM_VOTE"
                        2 -> SearchSettings.order = "RATING"
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
    @SuppressLint("SetTextI18n")
    private fun customizeRangeSlider() {
        Log.d("mytag","${SearchSettings.ratingFrom.toFloat()}, ${SearchSettings.ratingTo.toFloat()}")
        binding.ratingRangeSlider.setValues(SearchSettings.ratingFrom.toFloat(),SearchSettings.ratingTo.toFloat())
        binding.ratingFromTo.text = "${SearchSettings.ratingFrom} - ${SearchSettings.ratingTo}"
        binding.ratingRangeSlider.addOnChangeListener(
            RangeSlider.OnChangeListener { slider, _, _ ->
                SearchSettings.ratingFrom = slider.values[0].toInt()
                SearchSettings.ratingTo = slider.values[1].toInt()
                binding.ratingFromTo.text = "${slider.values[0].toInt()} - ${slider.values[1].toInt()}"
                Log.d("mytag",SearchSettings.ratingFrom.toString())
            })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}