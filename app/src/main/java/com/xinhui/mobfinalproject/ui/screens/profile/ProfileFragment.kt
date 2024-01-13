package com.xinhui.mobfinalproject.ui.screens.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.databinding.FragmentProfileBinding
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.profile.viewModel.ProfileViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.tabContainer.TabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModel: ProfileViewModelImpl by activityViewModels()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                viewModel.updateProfileUri(uri)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun setupUIComponents(view: View) {
        super.setupUIComponents(view)

        binding.run {
            icLogout.setOnClickListener {
                viewModel.logout()
            }

            ivAddImage.setOnClickListener {
                pickMedia.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }

            ivEdit.setOnClickListener {
                etName.setText(viewModel.user.value.name)
                showNameHideEditText(false)
            }

            ivTick.setOnClickListener {
                if (etName.text.toString() == viewModel.user.value.name)
                    showNameHideEditText()
                else viewModel.updateUsername(etName.text.toString())
            }

            ivCancel.setOnClickListener {
                showNameHideEditText()
            }
        }
    }

    override fun setupViewModelObserver() {
        super.setupViewModelObserver()

        lifecycleScope.launch {
            viewModel.profilePic.collect {
                Glide.with(requireView())
                    .load(it)
                    .placeholder(R.drawable.ic_image)
                    .into(binding.ivImage)
            }
        }
        lifecycleScope.launch {
            viewModel.user.collect {
                binding.run {
                    tvName.text = it.name
                    tvEmail.text = it.email
                    showNameHideEditText()
                }
            }
        }
        lifecycleScope.launch {
            viewModel.loggedOut.collect {
                val action = TabContainerFragmentDirections.actionLogout()
                navController.navigate(action)
            }
        }
    }

    private fun showNameHideEditText(show: Boolean = true) {
        binding.run {
            llName.isVisible = show
            llEditName.isVisible = !show
        }
    }
}