package com.xarhssta.artbookproject.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.RequestManager
import com.xarhssta.artbookproject.R
import com.xarhssta.artbookproject.databinding.FragmentArtDetailsBinding
import com.xarhssta.artbookproject.util.Status
import com.xarhssta.artbookproject.viewmodel.ArtViewModel
import javax.inject.Inject

class ArtDetailsFragment @Inject constructor(
    val glide: RequestManager
) : Fragment (R.layout.fragment_art_details) {

    private var fragmentBinding : FragmentArtDetailsBinding? = null
    lateinit var viewModel: ArtViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentArtDetailsBinding.bind(view)
        fragmentBinding = binding

        viewModel = ViewModelProvider(requireActivity()).get(ArtViewModel::class.java)

        binding.detailsArtImageView.setOnClickListener {
            findNavController().navigate(ArtDetailsFragmentDirections.actionArtDetailsFragmentToImageAPIFragment())
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().popBackStack()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        subscribeToObservers()

        binding.saveButton.setOnClickListener{
            viewModel.checkArt(binding.artNameEditText.text.toString(),
                binding.artArtistEditText.text.toString(),
                binding.artYearEditText.text.toString())
        }

    }

    private fun subscribeToObservers() {
        viewModel.selectedImageURL.observe(viewLifecycleOwner, Observer { url->
            fragmentBinding?.let {
                glide.load(url).into(it.detailsArtImageView)
            }
        })

        viewModel.insertArtMessage.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    Toast.makeText(requireContext(), "Success", Toast.LENGTH_LONG).show()
                    findNavController().popBackStack()
                    viewModel.resetInsertArtMsg()
                }

                Status.ERROR -> {
                    Toast.makeText(requireContext(), it.message ?:"Error", Toast.LENGTH_LONG).show()
                }

                Status.LOADING -> {

                }
            }
        })
    }

    override fun onDestroy() {
        fragmentBinding = null
        super.onDestroy()
    }
}