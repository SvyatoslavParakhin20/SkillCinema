package com.skillcinema.ui.home

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
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skillcinema.R
import com.skillcinema.databinding.FragmentHomeBinding
import com.skillcinema.entity.FilmsDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding:FragmentHomeBinding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private var parentFilmAdapter: ParentFilmAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.checkForOnboarding()
        if (viewModel.onboardingShownFlag == 0) findNavController().navigate(R.id.action_navigation_home_to_numberFragment)
        setUpViews()
        doObserveWork()
    }
    private fun setUpViews() {
        binding.parentRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        parentFilmAdapter = ParentFilmAdapter (
            onChildItemClick = {kinopoiskId -> onClickChildItem(kinopoiskId = kinopoiskId)},
            onParentItemClick = { filterId,description -> onClickParentItem(
                filterId = filterId,category=description)}
        )
        binding.parentRecyclerView.adapter = parentFilmAdapter
    }
    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.swipeRefresh.isRefreshing = false
                            binding.parentRecyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
                        }
                        else -> {
                            binding.parentRecyclerView.visibility = View.VISIBLE
                            binding.swipeRefresh.isRefreshing = false
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
                        }
                    }
                }
            }
            }
        viewModel.movies.onEach {
            renderFilmsList(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        viewModel.isError.onEach {
            if (it) {
                binding.parentRecyclerView.visibility = View.GONE
                binding.loadingErrorPage.visibility = View.VISIBLE
                binding.swipeRefresh.isRefreshing = false
                binding.button.setOnClickListener { requireActivity().finish() }
            }else {
                binding.loadingErrorPage.visibility = View.GONE
                binding.parentRecyclerView.visibility = View.VISIBLE
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            binding.loadingProgress.visibility = View.GONE
            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
            viewModel.movies.onEach {
                renderFilmsList(it)
            }.launchIn(viewLifecycleOwner.lifecycleScope)
        }
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun renderFilmsList(films: List<FilmsDto>) {
        if (films.isEmpty())return
        viewModel.checkWatched()
        parentFilmAdapter?.addData(films)
        parentFilmAdapter?.notifyDataSetChanged()
    }
    private fun onClickParentItem(filterId:Int, category:String ){
        val bundle = bundleOf()
        bundle.putInt(FILTER_ID_KEY,filterId)
        bundle.putString(CATEGORY_DESCRIPTION_KEY,category)
        findNavController().navigate(R.id.action_navigation_home_to_fullFilmList, bundle)
    }
    private fun onClickChildItem(kinopoiskId: Int) {
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY, kinopoiskId)
        findNavController().navigate(R.id.action_navigation_home_to_filmFragment, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        parentFilmAdapter = null
    }
    companion object{
        private const val FILTER_ID_KEY = "filterId"
        private const val CATEGORY_DESCRIPTION_KEY = "description"
        private const val KINOPOISK_ID_KEY = "kinopoiskId"
    }
}