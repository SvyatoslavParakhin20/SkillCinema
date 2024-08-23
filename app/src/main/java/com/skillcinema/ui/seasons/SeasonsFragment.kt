package com.skillcinema.ui.seasons

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skillcinema.R
import com.skillcinema.databinding.FragmentSeasonsBinding
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.Item
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SeasonsFragment : Fragment() {

    private var _binding: FragmentSeasonsBinding? = null
    private val binding get() = _binding!!
    var kinopoiskId = 0
    private var seriesName = ""
    private val viewModel: SeasonsViewModel by viewModels()
    private lateinit var chipGroup: ChipGroup
    private val chipList = mutableMapOf<Chip,String>()
    private var seasonsChippedAdapter: SeasonsChippedAdapter? = null
    private lateinit var seasonsInfo: FilmSeasonsDto
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonsBinding.inflate(inflater, container, false)
        kinopoiskId = arguments.let { it?.getInt(KINOPOISK_ID_KEY)?:5260016 }
        seriesName = arguments.let { it?.getString(SERIES_NAME_KEY)?:"" }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        chipGroup = binding.chipGroup
        seasonsChippedAdapter = SeasonsChippedAdapter()
        binding.chippedRecyclerView.adapter = seasonsChippedAdapter
        doObserveWork()
    }
    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.chippedRecyclerView .visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
                        }
                        else -> {
                            binding.chippedRecyclerView .visibility = View.VISIBLE
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        viewModel.seasonsInfo.onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun setupAndRenderView (allInfo: List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        seasonsInfo = allInfo[1] as FilmSeasonsDto
        binding.seriesTitleTextView.text = seriesName
        addChips(seasonsInfo)
        binding.chippedRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL,false)
        startListener(seasonsInfo)
    }
    @SuppressLint("ResourceType", "SuspiciousIndentation")
    private fun startListener(seasonsInfo: FilmSeasonsDto){
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip:Chip? = if (checkedIds.isNotEmpty()) group.findViewById(checkedIds[0]) else null
            if (checkedIds.isNotEmpty()) {
                for (i in chipList.keys) {
                    i.chipBackgroundColor = ColorStateList.valueOf(
                        requireContext().getColor(R.color.grey))
                }
                chip!!.chipBackgroundColor =
                    ColorStateList.valueOf(requireContext().getColor(R.color.teal_200))
                val season = seasonsInfo.items[checkedIds[0]-1]
                val seriesText = getSeriesText(season)
                val seasonTitle = "${season.number} сезон, ${season.episodes!!.size} $seriesText"
                binding.seasonTitleTextView.text = seasonTitle
                seasonsChippedAdapter?.addData(season.episodes)
            }
            else {
                for (i in chipList.keys)i.chipBackgroundColor = ColorStateList.valueOf(
                    requireContext().getColor(R.color.grey))
                binding.seasonTitleTextView.text = ""
                seasonsChippedAdapter?.removeData()
            }
        }
    }
    @SuppressLint("SetTextI18n", "ResourceType")
    private fun addChips(seasonsInfo: FilmSeasonsDto){
        if (chipList.isEmpty()) {
            var chipIdCounter = 1
            for (chipText in seasonsInfo.items) {
                val chip = Chip(requireContext())
                chip.text = chipText.number.toString()
                chip.id = chipIdCounter
                chip.isCheckable = true
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    requireContext().getColor(R.color.grey)
                )
                chipList[chip] = chipText.number.toString()
                chipGroup.addView(chip)
                chipIdCounter++
            }
        }
        if (chipGroup.size == 0) {
            chipList.keys.forEach { (it.parent as ViewGroup).removeView(it) }
            for (i in chipList.keys) chipGroup.addView(i)
        }
        if (chipGroup.checkedChipId == -1) {
            chipGroup.check(1)
            val chipInitial: Chip = chipGroup.findViewById(1)
            chipInitial.chipBackgroundColor =
                ColorStateList.valueOf(requireContext().getColor(R.color.teal_200))
            val seriesText = getSeriesText(seasonsInfo.items[0])
            val seasonTitle = "1 сезон, ${seasonsInfo.items[0].episodes!!.size} $seriesText"
            binding.seasonTitleTextView.text = seasonTitle
            seasonsChippedAdapter?.addData(seasonsInfo.items[0].episodes!!)
        } else {
            val season = seasonsInfo.items[chipGroup.checkedChipId-1]
            val seriesText = getSeriesText(season)
            val seasonTitle = "${season.number} сезон, ${season.episodes!!.size} $seriesText"
            binding.seasonTitleTextView.text = seasonTitle
            seasonsChippedAdapter?.addData(season.episodes)
        }
    }
    private fun getSeriesText(season:Item):String{
        return  when {
            season.episodes!!.size in 11..14 -> "серий"
            season.episodes.size % 10 == 1 -> "серия"
            season.episodes.size % 10 in 2..4 -> "серии"
            season.episodes.size % 10 in 5..9 -> "серий"
            else -> "серий"
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        seasonsChippedAdapter = null
    }
    companion object{
        private const val KINOPOISK_ID_KEY = "kinopoiskId"
        private const val SERIES_NAME_KEY = "seriesName"
    }
}