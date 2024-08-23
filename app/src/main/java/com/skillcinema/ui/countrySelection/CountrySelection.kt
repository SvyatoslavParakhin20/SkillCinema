package com.skillcinema.ui.countrySelection

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skillcinema.data.FilmFilters
import com.skillcinema.data.FilterCountryDto
import com.skillcinema.databinding.FragmentCountrySelectionBinding
import com.skillcinema.ui.search.SearchSettings
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountrySelection : Fragment() {
    private var _binding: FragmentCountrySelectionBinding? = null
    private val binding get() = _binding!!
    private var countries:List<FilterCountryDto> = listOf()
    private var currentCountries:List<FilterCountryDto> = listOf()
    private var adapter: CountrySelectionAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCountrySelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = CountrySelectionAdapter{countryId -> onClickCountry(countryId = countryId)}
        binding.recyclerView.adapter = adapter
        countries = FilmFilters.getAllCountries().filter { it.country != "" }.sortedBy { it.country }
        adapter?.addData(countries)
        binding.goBack.setOnClickListener { findNavController().popBackStack() }
        binding.editText.addTextChangedListener ( object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                currentCountries = countries.filter { p0.toString().lowercase() in it.country!!.lowercase() }.sortedBy { it.country }
                adapter?.clearData()
                adapter?.addData(currentCountries)
            }
        } )
    }
    private fun onClickCountry(countryId:Int){
        SearchSettings.countries = listOf(countryId)
        findNavController().popBackStack()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }
}