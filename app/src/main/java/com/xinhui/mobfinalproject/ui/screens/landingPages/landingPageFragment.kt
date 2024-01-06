package com.xinhui.mobfinalproject.ui.screens.landingPages

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.xinhui.mobfinalproject.databinding.LandingPageBinding
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel

class landingPageFragment : Fragment() {

    private lateinit var binding: LandingPageBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LandingPageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            tvSkip.setOnClickListener {
                val action = landingPageFragmentDirections.toLogin()
                findNavController().navigate(action)
            }

            btnGet.setOnClickListener {
                val action = landingPageFragmentDirections.actionLandingPageToLandingPage2()
                findNavController().navigate(action)
            }
        }
    }
}