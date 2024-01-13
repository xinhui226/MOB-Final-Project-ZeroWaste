package com.xinhui.mobfinalproject.ui.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.core.utils.ShowDialog
import com.xinhui.mobfinalproject.databinding.FragmentLoginBinding
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.login.viewModel.LoginViewModelImpl
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginFragment: BaseFragment<FragmentLoginBinding>(){
    override val viewModel: LoginViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        setFragmentResultListener("register_to_login") { _, bundle ->
            val result = bundle.getBoolean("registerSuccessful")
            if (result) {
                ShowDialog.showEmailVerificationDialog(requireContext(), layoutInflater)
                binding.etEmail.setText(bundle.getString("email"))
                binding.etPassword.setText(bundle.getString("password"))
            }
        }

        binding.run {
            btnLogin.setOnClickListener {
                viewModel.login(etEmail.text.toString(), etPassword.text.toString())
            }
            tvRegisterNow.setOnClickListener {
                val action = LoginFragmentDirections.actionLoginToRegister()
                navController.navigate(action)
            }
            tvForgetPass.setOnClickListener {
                ShowDialog.showForgetEmailDialog(requireContext(), layoutInflater) { email ->
                    viewModel.sendResetPasswordLink(email)
                }
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.loggedIn.collect {
                navController.navigate(R.id.toHome)
            }
        }
        lifecycleScope.launch {
            viewModel.emailNotVerified.collect {
                ShowDialog.showEmailVerificationDialog(requireContext(), layoutInflater)
                viewModel.loggedIn.collect {
                    navController.navigate(R.id.toHome)
                }
            }
        }
    }
}
