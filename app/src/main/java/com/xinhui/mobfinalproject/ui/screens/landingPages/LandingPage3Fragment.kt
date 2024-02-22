package com.xinhui.mobfinalproject.ui.screens.landingPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.xinhui.mobfinalproject.databinding.LandingPage3Binding

class LandingPage3Fragment: Fragment() {

    private lateinit var binding: LandingPage3Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LandingPage3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            tvSkip.setOnClickListener {
                val action = LandingPage3FragmentDirections.toLogin()
                findNavController().navigate(action)
            }

            btnLogin.setOnClickListener {
                val action = LandingPage3FragmentDirections.toLogin()
                findNavController().navigate(action)
            }
        }

    }
}