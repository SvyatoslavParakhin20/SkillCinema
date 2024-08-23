package com.skillcinema.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import com.skillcinema.R
import com.skillcinema.databinding.FragmentProfileBinding
import com.skillcinema.room.Collection
import com.skillcinema.room.CollectionWIthFilms
import com.skillcinema.room.Collections
import com.skillcinema.room.Film
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val viewModel: ProfileViewModel by viewModels()
    private val binding get() = _binding!!
    private var watchedListParentAdapter: ParentFilmListAdapter? = null
    private var historyListParentAdapter: ParentFilmListAdapter? = null
    private var userCollectionsAdapter: UserCollectionsAdapter? = null
    private var concatAdapter: ConcatAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        doObserveWork()
        viewModel.getCollectionsList()
    }
    private fun setUpViews() {
        watchedListParentAdapter = ParentFilmListAdapter(
            onClickOpenFullCollection = {collection -> onClickOpenCollection(collection = collection)},
            onClickCLeanCollection = {cleanCollection -> onClickCleanCollection(cleanCollection)},
            onClickFilm = {film -> onClickOpenFilm(film) }
        )
        userCollectionsAdapter = UserCollectionsAdapter(
            requireContext(),
            onItemClick =  { collection -> onClickOpenCollection(collection = collection) },
            onClickCreateCollection = { onClickCreateCollection() },
            onClickDeleteCollection =  {deleteCollection -> onClickDeleteCollection(deleteCollection)}
        )

        historyListParentAdapter = ParentFilmListAdapter(
            onClickOpenFullCollection = {collection -> onClickOpenCollection(collection = collection)},
            onClickCLeanCollection = {cleanCollection -> onClickCleanCollection(cleanCollection)},
            onClickFilm = {film -> onClickOpenFilm(film) }
        )
    }
    private fun doObserveWork() {
        viewModel.collections .onEach {
            setupAndRenderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }

    private fun setupAndRenderView(collections: List<CollectionWIthFilms>){
        if (collections.isEmpty())return
        val watchedList = collections.filter { it.collection.name == Collections.WATCHED_LIST.rusName }
        val historyList = collections.filter { it.collection.name == Collections.HISTORY.rusName }
        watchedListParentAdapter?.addData(watchedList)
        userCollectionsAdapter?.addData(collections)
        historyListParentAdapter?.addData(historyList)

        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(true)
        }.build()
        concatAdapter = ConcatAdapter(config, watchedListParentAdapter,userCollectionsAdapter,historyListParentAdapter)
        binding.concatRecyclerView.adapter = concatAdapter
    }
    private fun onClickOpenCollection(collection: CollectionWIthFilms){
        val bundle = bundleOf()
        bundle.putString(COLLECTION_NAME_KEY,collection.collection.name)
        findNavController().navigate(R.id.action_profileFragment_to_fullCollectionFragment, bundle)
    }
    private fun onClickCleanCollection(collection:String){
        viewModel.cleanCollectionHistory (collection)
    }
    private fun onClickOpenFilm (film:Film){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY, film.kinopoiskId)
        findNavController().navigate(R.id.action_profileFragment_to_filmFragment, bundle)
    }
    private fun onClickCreateCollection(){
        binding.createCollectionLayout.visibility = View.VISIBLE
        binding.createCollectionCloseButton.setOnClickListener {
            binding.createCollectionLayout.visibility = View.GONE
            binding.editText.setText("")
        }
        binding.readyButton.setOnClickListener {
            val text = binding.editText.text.toString()
            binding.editText.setText("")
            viewModel.createCollection(text)
            userCollectionsAdapter?.addCollection(
                CollectionWIthFilms(
                    collection = Collection(text),
                    films = mutableListOf()
                )
            )
            binding.createCollectionLayout.visibility = View.GONE
        }
    }
    private fun onClickDeleteCollection (collectionName: String){
        viewModel.deleteCollection(collectionName = collectionName)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        watchedListParentAdapter = null
        historyListParentAdapter = null
        userCollectionsAdapter = null
        concatAdapter = null
    }
    companion object{
        private const val COLLECTION_NAME_KEY = "collectionName"
        private const val KINOPOISK_ID_KEY = "kinopoiskId"
    }
}