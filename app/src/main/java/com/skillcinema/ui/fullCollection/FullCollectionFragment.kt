package com.skillcinema.ui.fullCollection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.skillcinema.R
import com.skillcinema.databinding.FragmentFullCollectionBinding
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Film
import com.skillcinema.ui.home.RecyclerItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class FullCollectionFragment : Fragment() {
    private var _binding: FragmentFullCollectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FullCollectionViewModel by viewModels()
    private var collectionName = ""
    private var userCollectionAdapter: UserCollectionAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFullCollectionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        collectionName = arguments.let { it?.getString("collectionName")?:"" }
        setUpViews()
        doObserveWork()
        viewModel.getFilmsFromDb(collectionName)
    }
    private fun setUpViews() {
        binding.collectionTitle.text = collectionName
        binding.recyclerView.addItemDecoration(RecyclerItemDecoration(2, 5, includeEdge = true))
        userCollectionAdapter = UserCollectionAdapter(
            context = requireContext(),
            onClickFilm = {film -> onClickOpenFilm(film) }
        )
        binding.goUpButton.setOnClickListener {
            binding.recyclerView.scrollToPosition(0)
        }
    }
    private fun doObserveWork() {
        viewModel.films.onEach {
            renderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    private fun renderView(collection: List<CollectionWIthFilms>){
        if (collection.isEmpty())return
        userCollectionAdapter?.addData(collection)
        binding.recyclerView.adapter = userCollectionAdapter
        binding.cleanCollectionButton.setOnClickListener {
            viewModel.cleanCollectionHistory(collectionName = collectionName)
            userCollectionAdapter?.removeData()
        }
    }
    private fun onClickOpenFilm (film: Film){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY, film.kinopoiskId)
        findNavController().navigate(R.id.action_fullCollectionFragment_to_filmFragment, bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        userCollectionAdapter = null
    }
    companion object{
        private const val KINOPOISK_ID_KEY = "kinopoiskId"
    }
}