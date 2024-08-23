package com.skillcinema.ui.galleryFull

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
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.skillcinema.R
import com.skillcinema.databinding.FragmentGalleryFullBinding
import com.skillcinema.entity.FilmGalleryItemDto
import com.skillcinema.ui.fullFilmList.MyLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GalleryFullFragment : Fragment() {
    private var _binding: FragmentGalleryFullBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryFullViewModel by viewModels()
    private var kinopoiskId = 0
    private lateinit var chipGroup: ChipGroup
    private val chipList = mutableMapOf<Chip,String>()
    private var galleryChippedAdapter: GalleryChippedAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryFullBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kinopoiskId = arguments.let { it?.getInt(KINOPOISK_ID_KEY)?:5260016 }
        viewModel.kinopoiskId = kinopoiskId
        chipGroup = binding.chipGroup
        galleryChippedAdapter = GalleryChippedAdapter{ item: FilmGalleryItemDto -> onItemClick(item) }
        binding.chippedRecyclerView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.chippedRecyclerView.adapter = galleryChippedAdapter?.withLoadStateFooter(MyLoadStateAdapter())
        galleryChippedAdapter?.addLoadStateListener {
            binding.chippedRecyclerView.visibility = if (it.refresh is LoadState.Error)  View.GONE else View.VISIBLE
            binding.goUpButton.visibility = if (it.refresh is LoadState.Error)  View.GONE else View.VISIBLE
            binding.chipGroup.visibility = if (it.refresh is LoadState.Error)  View.GONE else View.VISIBLE
            binding.loadingErrorPage.visibility = if (it.refresh is LoadState.Error)  View.VISIBLE else View.GONE
        }
        binding.goUpButton.setOnClickListener {
            binding.chippedRecyclerView.scrollToPosition(0)
        }
        doObserveWork(kinopoiskId)
    }
    private fun doObserveWork(staffId:Int){
        viewModel.loadChips(staffId)
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
        viewModel.chipInfo.onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    @Suppress("UNCHECKED_CAST")
    private fun setupAndRenderView (allInfo: List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        val chipInfo = allInfo[1] as Map<String, Int>
        if (chipInfo.isEmpty()){return}
        binding.header.text = requireContext().getString(R.string.gallery_header)
        addChips(chipInfo)
        startListener()
    }
    @SuppressLint("SetTextI18n", "ResourceType")
    private fun addChips(chipInfo:Map<String,Int>){
        if (chipList.isEmpty()) {
            if (chipInfo.isEmpty()){return}
            var chipIdCounter = 1
            for (chipText in chipInfo) {
                val translatedText = when (chipText.key) {
                    "STILL" -> requireContext().getString(R.string.STILL)
                    "SHOOTING" -> requireContext().getString(R.string.SHOOTING)
                    "POSTER" -> requireContext().getString(R.string.POSTER)
                    "FAN_ART" -> requireContext().getString(R.string.FAN_ART)
                    "PROMO" -> requireContext().getString(R.string.PROMO)
                    "CONCEPT" -> requireContext().getString(R.string.CONCEPT)
                    "WALLPAPER" -> requireContext().getString(R.string.WALLPAPER)
                    "COVER" -> requireContext().getString(R.string.COVER)
                    "SCREENSHOT" -> requireContext().getString(R.string.SCREENSHOT)
                    else -> requireContext().getString(R.string.UNKNOWN)
                }
                val chip = Chip(requireContext())
                chip.text = "$translatedText ${chipText.value}"
                chip.id = chipIdCounter
                chip.isCheckable = true
                chip.chipBackgroundColor = ColorStateList.valueOf(
                    requireContext().getColor(R.color.grey)
                )
                chipList[chip] = chipText.key
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
            viewModel.filterId = chipList.getValue(chipInitial)
            galleryChippedAdapter?.submitData(lifecycle, PagingData.empty())
            viewModel.pagedImages.onEach {
                galleryChippedAdapter?.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
        else {
            viewModel.filterId =
                chipList.getValue((chipGroup.findViewById(chipGroup.checkedChipId)))
            viewModel.pagedImages.onEach {
                galleryChippedAdapter?.submitData(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
    @SuppressLint("ResourceType", "SuspiciousIndentation")
    private fun startListener(){
        chipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            val chip:Chip? = if (checkedIds.isNotEmpty()) group.findViewById(checkedIds[0]) else null
            if (checkedIds.isNotEmpty()) {
                for (i in chipList.keys) {
                    i.chipBackgroundColor = ColorStateList.valueOf(
                        requireContext().getColor(R.color.grey))
                }
                chip!!.chipBackgroundColor =
                    ColorStateList.valueOf(requireContext().getColor(R.color.teal_200))
                viewModel.filterId = chipList.getValue((chipGroup.findViewById(checkedIds[0])))
                viewModel.pagedImages.onEach {
                    galleryChippedAdapter?.submitData(it)
                }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
            else {
                for (i in chipList.keys)i.chipBackgroundColor = ColorStateList.valueOf(
                    requireContext().getColor(R.color.grey))
                    galleryChippedAdapter?.submitData(lifecycle, PagingData.empty())
            }
        }
    }
    private fun onItemClick(item: FilmGalleryItemDto) {
        val bundle = Bundle()
        item.imageUrl?.let {bundle.putString(IMAGE_URL_KEY, it)  }
        findNavController().navigate(R.id.action_galleryFullFragment_to_galleryFullScreenFragment, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        galleryChippedAdapter = null
    }
    companion object{
        private const val IMAGE_URL_KEY = "imageUrl"
        private const val KINOPOISK_ID_KEY = "kinopoiskId"
    }
}