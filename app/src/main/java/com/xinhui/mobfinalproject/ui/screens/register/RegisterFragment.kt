package com.xinhui.mobfinalproject.ui.screens.register

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xinhui.mobfinalproject.core.utils.Constants
import com.xinhui.mobfinalproject.databinding.FragmentRegisterBinding
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.register.viewModel.RegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: RegisterViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.run {
            btnRegister.setOnClickListener {
                val name = etName.text.toString()
                val email = etEmail.text.toString()
                val pass = etPass.text.toString()
                val conPass = etConfirmPass.text.toString()
                viewModel.register(name, email, pass, conPass)
            }

            tvLoginNow.setOnClickListener {
                navController.popBackStack()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()


        lifecycleScope.launch {
            viewModel.success.collect {
                val bundle = bundleOf(
                    Constants.bundleRegisterSuccess to true,
                    Constants.bundleEmail to binding.etEmail.text.toString(),
                    Constants.bundlePassword to binding.etPass.text.toString())
                setFragmentResult(Constants.bundleRegToLogin, bundle)
                navController.popBackStack()
            }
        }
    }
}