package com.skillcinema.ui.film

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.skillcinema.R
import com.skillcinema.databinding.FragmentFilmBinding
import com.skillcinema.entity.ActorDto
import com.skillcinema.entity.FilmGalleryDto
import com.skillcinema.entity.FilmInfo
import com.skillcinema.entity.FilmSeasonsDto
import com.skillcinema.entity.FilmSimilarsDto
import com.skillcinema.room.Collections
import com.skillcinema.room.Film
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilmFragment : Fragment() {
    private var _binding: FragmentFilmBinding? = null
    private val binding get() = _binding!!
    private var kinopoiskId = 0
    private val viewModel: FilmViewModel by viewModels()
    private var generalInfoAdapter: FilmGeneralInfoAdapter? = null
    private var filmSeasonsAdapter: FilmSeasonsAdapter? = null
    private var filmActorsParentAdapter: FilmActorsParentAdapter? = null
    private var filmGalleryParentAdapter: FilmGalleryParentAdapter? = null
    private var filmSimilarParentAdapter: FilmSimilarParentAdapter? = null
    private var concatAdapter: ConcatAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFilmBinding.inflate(inflater, container, false)
        kinopoiskId = arguments.let { it?.getInt(KINOPOISK_ID_KEY)?:5260016 }
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        doObserveWork()
    }
    private fun setUpViews(){
        generalInfoAdapter = FilmGeneralInfoAdapter(
            {film -> onClickWatched(film = film)},
            {film -> onClickLiked(film = film)},
            {film -> onClickToWatch(film = film)},
            {film -> addToHistory(film = film) },
            {film -> addToCollection(filmInfo = film) }
        )
        filmSeasonsAdapter = FilmSeasonsAdapter(
            onItemClick = {filmId:Int,seriesName:String->
                onItemClickSeason(filmId = filmId, seriesName = seriesName)}
        )
        filmActorsParentAdapter = FilmActorsParentAdapter(
            onItemClickChildActor = {staffId:Int -> onItemClickActorChild(staffId = staffId)},
            onItemClickActorParent = {filmId:Int,isActor:Boolean,isSeries:Boolean ->
                onItemClickActorParent(kinopoiskId = filmId, isActor = isActor, isSeries = isSeries)}
        )
        filmGalleryParentAdapter = FilmGalleryParentAdapter(
            onItemClickGalleryParent = {kinopoiskId ->
                onItemClickGalleryParent(kinopoiskId = kinopoiskId)},
            onItemClickGalleryChild = {url -> onItemClickGalleryChild(url = url)}
        )
        filmSimilarParentAdapter = FilmSimilarParentAdapter(
            onItemClickSimilarParent = {filmId -> onItemClickSimilarParent(kinopoiskId = filmId)},
            onItemClickSimilarChild = {filmId -> onItemClickSimilarChild(kinopoiskId = filmId)}
        )
    }
    private fun doObserveWork() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isLoading.collect {
                    when (it) {
                        true -> {
                            binding.concatRecyclerView.visibility = View.GONE
                            binding.loadingProgress.visibility = View.VISIBLE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.GONE
                        }
                        else -> {
                            binding.concatRecyclerView.visibility = View.VISIBLE
                            binding.loadingProgress.visibility = View.GONE
                            requireActivity().findViewById<BottomNavigationView>(R.id.nav_view).visibility = View.VISIBLE
                        }
                    }
                }
            }
        }
        viewModel.movieInfo.onEach {
            setupAndRenderView(it)
            setFragmentResultListener(UPDATE_KEY){ _, _ ->
                viewModel.checkLiked()
                viewModel.checkWatched()
                viewModel.checkToWatch()
                setupAndRenderView(it)
            }
        }.launchIn(viewLifecycleOwner.lifecycleScope)
    }
    @Suppress("UNCHECKED_CAST")
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
        viewModel.checkWatched()
        viewModel.checkLiked()
        viewModel.checkToWatch()
        val generalInfo: FilmInfo = allInfo[1] as FilmInfo
        val isSeries = generalInfo.serial
        generalInfoAdapter?.addData(generalInfo)
        val seasons: FilmSeasonsDto = allInfo[2] as FilmSeasonsDto
        filmSeasonsAdapter?.addData(seasons, kinopoiskId = kinopoiskId, filmName = (generalInfo.nameRu?:generalInfo.nameEn?:"") as String)
        val actorInfo: List<ActorDto> = allInfo[3] as List<ActorDto>
        val otherStaff: List<ActorDto> = allInfo[4] as List<ActorDto>
        filmActorsParentAdapter?.addData(actorInfo,otherStaff, isSeries!!,generalInfo.kinopoiskId!!)
        val gallery: FilmGalleryDto = allInfo[5] as FilmGalleryDto
        generalInfo.kinopoiskId?.let { filmGalleryParentAdapter?.addData(gallery, it) }
        val similar: FilmSimilarsDto = allInfo[6] as FilmSimilarsDto
        generalInfo.kinopoiskId?.let { filmSimilarParentAdapter?.addData(similar, it) }
        val config = ConcatAdapter.Config.Builder().apply {
            setIsolateViewTypes(true)
        }.build()
        concatAdapter = ConcatAdapter(config, generalInfoAdapter,filmSeasonsAdapter,filmActorsParentAdapter,filmGalleryParentAdapter,filmSimilarParentAdapter)
        binding.concatRecyclerView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.concatRecyclerView.adapter = concatAdapter
    }
    private fun onClickWatched (film:Film) {
        if (film.isWatched == false) {
            viewModel.deleteFilmFromCollection(filmId = film.kinopoiskId, collection = Collections.WATCHED_LIST.rusName)
        }else{
            viewModel.addFilmToCollection(collection = Collections.WATCHED_LIST.rusName, film = film )
        }
    }
    private fun onClickLiked (film:Film) {
        if (film.isLiked == false) {
            viewModel.deleteFilmFromCollection(filmId = film.kinopoiskId, collection = Collections.LIKED.rusName)
        }else {
            viewModel.addFilmToCollection(collection = Collections.LIKED.rusName, film = film)
        }
    }
    private fun onClickToWatch(film: Film){
        if (film.toWatch == false) {
            viewModel.deleteFilmFromCollection(filmId = film.kinopoiskId, collection = Collections.TO_WATCH.rusName)
        }else {
            viewModel.addFilmToCollection(collection = Collections.TO_WATCH.rusName, film = film)
        }
    }
    private fun addToHistory(film: Film){
        viewModel.addFilmToCollection(Collections.HISTORY.rusName,film = film)
    }
    private fun addToCollection(filmInfo: FilmInfo) {
        val bundle = bundleOf()
        bundle.putParcelable(FILM_INFO_KEY,filmInfo)
        childFragmentManager.setFragmentResult(BOTTOM_SHEET_KEY,bundle)
        findNavController().navigate(R.id.action_filmFragment_to_collectionDialogFragment, bundle)
    }
    private fun onItemClickSeason(filmId:Int,seriesName:String){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY,filmId)
        bundle.putString(SERIES_NAME_KEY,seriesName)
        findNavController().navigate(R.id.action_filmFragment_to_seasonsFragment,bundle)
    }
    private fun onItemClickActorChild(staffId:Int){
        val bundle = bundleOf()
        bundle.putInt(STAFF_ID_KEY,staffId)
        findNavController().navigate(R.id.action_filmFragment_to_actorFragment,bundle)
    }
    private fun onItemClickActorParent(kinopoiskId:Int,isActor:Boolean,isSeries: Boolean){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY, kinopoiskId)
        bundle.putBoolean(IS_ACTOR_KEY, isActor)
        bundle.putBoolean(IS_SERIES_KEY, isSeries)
        findNavController().navigate(R.id.action_filmFragment_to_actorsFullFragment, bundle)
    }
    private fun onItemClickGalleryParent(kinopoiskId: Int){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY,kinopoiskId)
        findNavController().navigate(R.id.action_filmFragment_to_galleryFullFragment, bundle)
    }
    private fun onItemClickGalleryChild(url: String){
        val bundle = bundleOf()
        bundle.putString(IMAGE_URL_KEY, url)
        findNavController().navigate(R.id.action_filmFragment_to_galleryFullScreenFragment,bundle)
    }
    private fun onItemClickSimilarParent(kinopoiskId: Int){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY,kinopoiskId)
        findNavController().navigate(R.id.action_filmFragment_to_similarFullFragment, bundle)
    }
    private fun onItemClickSimilarChild(kinopoiskId: Int){
        val bundle = bundleOf()
        bundle.putInt(KINOPOISK_ID_KEY,kinopoiskId)
        findNavController().navigate(R.id.action_filmFragment_self,bundle)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        generalInfoAdapter = null
        filmSeasonsAdapter = null
        filmActorsParentAdapter = null
        filmGalleryParentAdapter = null
        filmSimilarParentAdapter = null
        concatAdapter = null
    }
    companion object{
        private const val KINOPOISK_ID_KEY = "kinopoiskId"
        private const val SERIES_NAME_KEY = "seriesName"
        private const val FILM_INFO_KEY = "filmInfo"
        private const val BOTTOM_SHEET_KEY = "key"
        private const val UPDATE_KEY = "update"
        private const val STAFF_ID_KEY = "staffId"
        private const val IS_ACTOR_KEY = "isActor"
        private const val IS_SERIES_KEY = "isSeries"
        private const val IMAGE_URL_KEY = "imageUrl"
    }
}