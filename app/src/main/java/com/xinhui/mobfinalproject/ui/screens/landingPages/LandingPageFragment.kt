package com.xinhui.mobfinalproject.ui.screens.landingPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xinhui.mobfinalproject.databinding.LandingPageBinding

class LandingPageFragment : Fragment() {

    private lateinit var binding: LandingPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            tvSkip.setOnClickListener {
                val action = LandingPageFragmentDirections.toLogin()
                findNavController().navigate(action)
            }

            btnGet.setOnClickListener {
                val action = LandingPageFragmentDirections.actionLandingPageToLandingPage2()
                findNavController().navigate(action)
            }
        }
    }
}