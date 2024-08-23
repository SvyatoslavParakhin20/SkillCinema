package com.skillcinema.ui.similarFull

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skillcinema.R
import com.skillcinema.databinding.FragmentSimilarFullBinding
import com.skillcinema.entity.FilmSimilarsDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SimilarFullFragment : Fragment() {
    private var _binding: FragmentSimilarFullBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SimilarFullViewModel by viewModels()
    private var kinopoiskId = 0
    private var similarFullAdapter: SimilarFullAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarFullBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        kinopoiskId = arguments.let { it?.getInt(KINOPOISK_ID)?:5260016 }
        binding.header.text = requireContext().getString(R.string.similar_header)
        doObserveWork()
    }
    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.recyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
                        }
                        else -> {
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        viewModel.movieInfo.onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    @SuppressLint("SetTextI18n")
    private fun setupAndRenderView(allInfo:List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        val similar: FilmSimilarsDto = allInfo[1] as FilmSimilarsDto
        similarFullAdapter = SimilarFullAdapter { onclickItem -> onClickSimilar(onclickItem)}
        binding.recyclerView.adapter = similarFullAdapter
        similarFullAdapter?.addData(similar)
        binding.goUpButton.setOnClickListener{
            binding.recyclerView.scrollToPosition(0)
        }
    }
    private fun onClickSimilar(filmId:Int){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID,filmId)
        findNavController().navigate(R.id.action_similarFullFragment_to_filmFragment,bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        similarFullAdapter = null
    }
    companion object{
        private const val KINOPOISK_ID = "kinopoiskId"
    }
}