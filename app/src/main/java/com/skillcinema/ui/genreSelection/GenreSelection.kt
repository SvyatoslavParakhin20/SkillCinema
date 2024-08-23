package com.skillcinema.ui.genreSelection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skillcinema.data.FilmFilters
import com.skillcinema.data.FilterGenreDto
import com.skillcinema.databinding.FragmentGenreSelectionBinding
import com.skillcinema.ui.search.SearchSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GenreSelection : Fragment() {
    private var _binding: FragmentGenreSelectionBinding? = null
    private val binding get() = _binding!!
    private var currentCountries:List<FilterGenreDto> = listOf()
    private var adapter:GenreSelectionAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGenreSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = GenreSelectionAdapter{genreId -> onClickGenre(genreId = genreId)}
        binding.recyclerView.adapter = adapter
        val genres = FilmFilters.getAllGenres().filter { it.genre != "" }.sortedBy { it.genre }
        adapter?.addData(genres)
        binding.goBack.setOnClickListener { findNavController().popBackStack() }
        binding.editText.addTextChangedListener ( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                currentCountries = genres.filter { p0.toString().lowercase() in it.genre!!.lowercase() }.sortedBy { it.genre }
                adapter?.clearData()
                adapter?.addData(currentCountries)
            }
        } )
    }
    private fun onClickGenre(genreId:Int){
        SearchSettings.genres = listOf(genreId)
        findNavController().popBackStack()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}