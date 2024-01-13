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
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.data.model.Notification
import com.xinhui.mobfinalproject.databinding.FragmentProfileBinding
import com.xinhui.mobfinalproject.ui.adapter.NotificationAdapter
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.profile.viewModel.ProfileViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.tabContainer.TabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModel: ProfileViewModelImpl by activityViewModels()

    private lateinit var pickMedia: ActivityResultLauncher<PickVisualMediaRequest>

    private lateinit var adapter: NotificationAdapter

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

        setupAdapter()

        binding.run {
            icLogout.setOnClickListener {
                val action = TabContainerFragmentDirections.actionLogout()
                navController.navigate(action)
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
            viewModel.user.collect {
                binding.run {
                    tvName.text = it.name
                    tvEmail.text = it.email
                    Glide.with(requireView())
                        .load(it.profileUrl)
                        .placeholder(R.drawable.ic_image)
                        .into(binding.ivImage)
                    showNameHideEditText()
                }
            }
        }
    }

    private fun showNameHideEditText(show: Boolean = true) {
        binding.run {
            llName.isVisible = show
            llEditName.isVisible = !show
        }
        lifecycleScope.launch {
            viewModel.notifications.collect{
                adapter.showNotification(it)
            }
        }
    }

    private fun setupAdapter() {
        adapter = NotificationAdapter(emptyList())

        adapter.listener = object: NotificationAdapter.Listener {
            override fun onDelete(notification: Notification) {
                viewModel.delete(notification)
            }
        }

        val layoutManager = LinearLayoutManager(requireContext())
        binding.run {
            rvNotification.adapter = adapter
            rvNotification.layoutManager = layoutManager
        }
    }
}