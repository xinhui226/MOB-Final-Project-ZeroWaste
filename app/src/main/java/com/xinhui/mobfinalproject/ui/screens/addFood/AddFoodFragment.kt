package com.xinhui.mobfinalproject.ui.screens.addFood

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.databinding.FragmentAddFoodBinding
import com.xinhui.mobfinalproject.ui.screens.addFood.viewModel.AddFoodViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddFoodFragment : BaseFragment<FragmentAddFoodBinding>() {

    override val viewModel : AddFoodViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddFoodBinding.inflate(inflater,container,false)
        return binding.root
    }



}