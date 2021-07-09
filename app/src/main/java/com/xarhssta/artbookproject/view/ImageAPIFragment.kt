package com.xarhssta.artbookproject.view

import android.os.Bundle
import android.view.View
import android.widget.GridLayout
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.RequestManager
import com.xarhssta.artbookproject.R
import com.xarhssta.artbookproject.adapter.ImageRecyclerAdapter
import com.xarhssta.artbookproject.databinding.FragmentImageApiBinding
import com.xarhssta.artbookproject.util.Status
import com.xarhssta.artbookproject.viewmodel.ArtViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class ImageAPIFragment @Inject constructor(
    val imageRecyclerAdapter: ImageRecyclerAdapter
) : Fragment (R.layout.fragment_image_api) {

    private var fragmentBinding: FragmentImageApiBinding? = null
    lateinit var viewModel: ArtViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        val binding = FragmentImageApiBinding.bind(view)
        fragmentBinding = binding

        var job: Job? = null
        binding.searchEditText.addTextChangedListener {
            job?.cancel()
            job = lifecycleScope.launch{
                delay(1000)
                it?.let {
                    if (it.toString().isNotEmpty()){
                        viewModel.searchForImage(it.toString())
                    }
                }
            }
        }

        subscribeToObservers()

        binding.searchResultRecyclerView.adapter = imageRecyclerAdapter
        binding.searchResultRecyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        imageRecyclerAdapter.setOnItemClickListener {
            findNavController().popBackStack()
            viewModel.setSelectedImage(it)
        }
    }

    private fun subscribeToObservers() {
        viewModel.imageList.observe(viewLifecycleOwner, Observer {
           when(it.status) {
               Status.SUCCESS -> {
                    val urls = it.data?.hits?.map { imageResult ->
                        imageResult.previewURL
                    }

                   imageRecyclerAdapter.images = urls ?: listOf()
                   fragmentBinding
               }
               Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message?:"Error",Toast.LENGTH_LONG).show()
               }
               Status.LOADING -> {
                   Toast.makeText(requireContext(), "Loading",Toast.LENGTH_SHORT).show()
               }
           }
        })
    }

}