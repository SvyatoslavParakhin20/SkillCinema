package com.skillcinema.ui.film

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.skillcinema.databinding.FragmentCollectionDialogListDialogBinding
import com.skillcinema.entity.FilmInfo
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach


@AndroidEntryPoint
class CollectionDialogFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentCollectionDialogListDialogBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DialogViewModel by viewModels()
    private lateinit var filmInfo: FilmInfo
    private var adapter: ItemAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCollectionDialogListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }
    @RequiresApi(Build.VERSION_CODES.N)
    @Suppress("DEPRECATION")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        filmInfo = arguments?.getParcelable(ARG_FILM_INFO)!!
        binding.list.layoutManager = GridLayoutManager(requireContext(), 1)
        adapter = ItemAdapter(
            filmInfo = filmInfo,
            onClickAddToCollection = { add -> onClickAddToCollections(add) },
            onClickCreateCollection = { onClickCreateCollection() }
        )
        binding.list.adapter = adapter
        doObserveWork()
        binding.closeButton.setOnClickListener { findNavController().popBackStack() }
        binding.createCollectionCloseButton.setOnClickListener {
            binding.createCollectionLayout.visibility = View.GONE
            binding.editText.setText("")
        }
        binding.readyButton.setOnClickListener {
            val text = binding.editText.text.toString()
            binding.editText.setText("")
            viewModel.createCollection(text)
            viewModel.addToCollections(checkedCollection = text,filmInfo = filmInfo)
            binding.createCollectionLayout.visibility = View.GONE
            viewModel.getCollectionsList()
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun doObserveWork() {
        viewModel.collection.onEach {
            renderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
     }
    private fun renderView(collections: List<CollectionData>){
        if (collections.isEmpty()) return
        else {
            adapter?.removeData()
            adapter?.addData(collections)
        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun onClickAddToCollections(checked: Pair<String, Boolean>) {
        if (checked.second) {
            viewModel.addToCollections(checkedCollection = checked.first, filmInfo = filmInfo)
        } else {
            viewModel.deleteFromCollection(
                collection = checked.first,
                filmId = filmInfo.kinopoiskId!!
            )
        }
    }
    private fun onClickCreateCollection () {
        binding.createCollectionLayout.visibility = View.VISIBLE
    }
    override fun onStop() {
        super.onStop()
        parentFragmentManager.setFragmentResult("update", bundleOf())
    }
    companion object{
        const val ARG_FILM_INFO = "filmInfo"
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}