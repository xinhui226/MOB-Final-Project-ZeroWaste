package com.xinhui.mobfinalproject.ui.screens.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.snackbar.Snackbar
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.databinding.FragmentRegisterBinding
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import com.xinhui.mobfinalproject.ui.screens.register.viewModel.RegisterViewModelImpl
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : BaseFragment<FragmentRegisterBinding>() {
    override val viewModel: RegisterViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

                if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || conPass.isEmpty()) {
                    Snackbar.make(view, "Please fill in all fields", Snackbar.LENGTH_LONG).show()
                } else if (pass != conPass) {
                    Snackbar.make(view, "Password and Confirm Password is not the same", Snackbar.LENGTH_LONG).show()
                } else {
                    viewModel.register(name, email, pass, conPass)
                }
            }

            tvLoginNow.setOnClickListener {
                navController.popBackStack()
            }
        }
    }
}