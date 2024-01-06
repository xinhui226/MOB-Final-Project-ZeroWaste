package com.xinhui.mobfinalproject.ui.screens.landingPages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xinhui.mobfinalproject.databinding.LandingPage2Binding
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel

class landingPage2Fragment : Fragment() {

    private lateinit var binding: LandingPage2Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LandingPage2Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            tvSkip.setOnClickListener {
                val action = landingPageFragmentDirections.toLogin()
                findNavController().navigate(action)
            }

            btnNext.setOnClickListener {
                val action = landingPage2FragmentDirections.actionLandingPage2ToLandingPage3()
                findNavController().navigate(action)
            }
        }
    }
}