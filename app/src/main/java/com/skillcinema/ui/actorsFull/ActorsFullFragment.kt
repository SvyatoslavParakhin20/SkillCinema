package com.skillcinema.ui.actorsFull

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
import com.skillcinema.databinding.FragmentActorsFullBinding
import com.skillcinema.entity.ActorDto
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlin.properties.Delegates

@AndroidEntryPoint
class ActorsFullFragment : Fragment() {
    private var _binding: FragmentActorsFullBinding? = null
    private val binding get() = _binding!!
    private val viewModel: ActorsFullViewModel by viewModels()
    private var actorsAdapter: ActorsAdapter? = null
    private var isActor by Delegates.notNull<Boolean>()
    private var isSeries by Delegates.notNull<Boolean>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActorsFullBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isActor = arguments.let { it!!.getBoolean("isActor") }
        isSeries = arguments.let { it!!.getBoolean("isSeries") }
        setUpViews()
        doObserveWork()
    }
    private fun setUpViews() {
        actorsAdapter = ActorsAdapter(
            onItemClick = {id -> onItemClickActor(actorId = id)}
        )
        binding.recyclerView.adapter = actorsAdapter
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
        viewModel.actorInfo.onEach {
            renderView(it)
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    @Suppress("UNCHECKED_CAST")
    @SuppressLint("SetTextI18n")
    private fun renderView(allInfo:List<Any>){
        if (allInfo.isEmpty())return
        val isLoaded = allInfo[0] as Boolean
        if (!isLoaded) {
            binding.loadingErrorPage.visibility = View.VISIBLE
            binding.button.setOnClickListener { findNavController().popBackStack() }
            return
        }
        else binding.loadingErrorPage.visibility = View.GONE
        val title = when {
            isSeries && isActor -> getString(R.string.actors_full_title_1)
            isSeries && !isActor -> getString(R.string.actors_full_title_2)
            !isSeries && isActor -> getString(R.string.actors_full_title_3)
            else -> getString(R.string.actors_full_title_4)
        }
        binding.header.text = title
        val actorInfo: List<ActorDto> = allInfo[1] as List<ActorDto>
        val otherStaff: List<ActorDto> = allInfo[2] as List<ActorDto>
        val staff = if (isActor) actorInfo else otherStaff
        actorsAdapter?.addData(staff)
    }
    private fun  onItemClickActor(actorId: Int){
        val bundle = bundleOf()
        bundle.putInt(STAFF_ID_KEY,actorId)
        findNavController().navigate(R.id.action_actorsFullFragment_to_actorFragment,bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        actorsAdapter = null
    }
    companion object{
        private const val STAFF_ID_KEY = "staffId"
    }
}