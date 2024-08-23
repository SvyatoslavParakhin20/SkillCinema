package com.skillcinema.ui.galleryFullScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.skillcinema.R
import com.skillcinema.databinding.FragmentGalleryFullScreenBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFullScreenFragment : Fragment() {
    private var _binding: FragmentGalleryFullScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: GalleryFullScreenViewModel by viewModels()
    private var imageUrl = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGalleryFullScreenBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageUrl = arguments.let { it?.getString("imageUrl")?:"" }
        Glide.with(requireContext())
            .load(imageUrl)
            .centerInside()
            .placeholder(AppCompatResources.getDrawable(requireContext(),R.drawable.ic_launcher_foreground))
            .into(binding.fullScreenImageView)
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}