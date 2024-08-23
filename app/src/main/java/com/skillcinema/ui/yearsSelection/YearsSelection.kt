package com.skillcinema.ui.yearsSelection

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.skillcinema.R
import com.skillcinema.databinding.FragmentYearsSelectionBinding
import com.skillcinema.ui.search.SearchSettings
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class YearsSelection : Fragment() {
    private var _binding: FragmentYearsSelectionBinding? = null
    private val binding get() = _binding!!
    private var yearFrom = 1000
    private var yearTo = 3000
    private  var adapterFrom: YearSelectionAdapter? = null
    private var adapterTo: YearSelectionAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentYearsSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.goBack.setOnClickListener { findNavController().popBackStack() }
        adapterFrom = YearSelectionAdapter{onclickYear -> onClickYearFrom(onclickYear)}
        adapterTo = YearSelectionAdapter{onclickYear -> onClickYearTo(onclickYear)}
        binding.rvFrom.adapter = adapterFrom
        binding.rvTo.adapter = adapterTo
        val list = (1980..Calendar.getInstance().get(Calendar.YEAR)).toList()
        adapterFrom?.addData(list)
        adapterTo?.addData(list)
        binding.selectionButton.setOnClickListener {
            if (yearTo>=yearFrom) {
                SearchSettings.yearFrom = yearFrom
                SearchSettings.yearTo = yearTo
            }else{
                Toast.makeText(requireContext(), getString(R.string.toast_year_selection_error),Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun onClickYearFrom(onclickYear:Int){
        yearFrom = onclickYear
    }
    private fun onClickYearTo(onclickYear:Int){
        yearTo = onclickYear
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapterFrom = null
        adapterTo = null
    }
}