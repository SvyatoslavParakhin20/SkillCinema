package com.skillcinema.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.skillcinema.R
import com.skillcinema.databinding.FragmentSearchBinding
import com.skillcinema.entity.FilmDto
import com.skillcinema.ui.home.RecyclerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel:SearchViewModel by viewModels()
    private var pagedFilteredFilmAdapter: PagedFilteredFilmAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pagedFilteredFilmAdapter = PagedFilteredFilmAdapter { film -> onItemClick(film) }
        pagedFilteredFilmAdapter?.addLoadStateListener {
            if (it.source.refresh is LoadState.NotLoading && it.append.endOfPaginationReached){
                binding.notFound.visibility = if (pagedFilteredFilmAdapter?.itemCount!! <1) View.VISIBLE else View.GONE
            }else {binding.notFound.visibility = View.GONE}
            Log.d("mytag", "SEARCH ERROR LISTENER: ${it.refresh}")
            binding.recyclerView.visibility = if (it.refresh is LoadState.Error)  View.GONE else View.VISIBLE
            binding.goUpButton.visibility = if (it.refresh is LoadState.Error)  View.GONE else View.VISIBLE
            binding.loadingErrorPage.visibility = if (it.refresh is LoadState.Error)  View.VISIBLE else View.GONE
        }
        binding.recyclerView.adapter = pagedFilteredFilmAdapter?.withLoadStateFooter(MyLoadStateAdapter())
        binding.recyclerView.addItemDecoration(RecyclerItemDecoration(2, 5, includeEdge = true))
        binding.goUpButton.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
        }
        pagedFilteredFilmAdapter?.submitData(lifecycle,PagingData.empty())
        viewModel.pagedFilm.onEach {
            pagedFilteredFilmAdapter?.submitData(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
        binding.editText.setText(SearchSettings.keyword)
        binding.editText.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                    SearchSettings.keyword = p0.toString()
                    pagedFilteredFilmAdapter?.submitData(lifecycle,PagingData.empty())
                    viewModel.pagedFilm.onEach {
                        pagedFilteredFilmAdapter?.submitData(it)
                    }.launchIn(viewLifecycleOwner.lifecycleScope)
            }
        })
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_searchFragment_to_searchSettings)
        }
    }
    private fun onItemClick(item: FilmDto) {
        val kinopoiskId = bundleOf(KINOPOISK_ID_KEY to item.kinopoiskId)
        findNavController().navigate(R.id.action_searchFragment_to_filmFragment, kinopoiskId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        pagedFilteredFilmAdapter = null
    }
    companion object{
        private const val KINOPOISK_ID_KEY = "kinopoiskId"
    }
}