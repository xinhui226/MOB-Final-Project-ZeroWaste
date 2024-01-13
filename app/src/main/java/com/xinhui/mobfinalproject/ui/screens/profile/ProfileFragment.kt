package com.xinhui.mobfinalproject.ui.screens.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.xinhui.mobfinalproject.R
import com.xinhui.mobfinalproject.data.model.Notification
import com.xinhui.mobfinalproject.databinding.FragmentProfileBinding
import com.xinhui.mobfinalproject.ui.adapter.NotificationAdapter
import com.xinhui.mobfinalproject.ui.screens.base.BaseFragment
import com.xinhui.mobfinalproject.ui.screens.profile.viewModel.ProfileViewModelImpl
import com.xinhui.mobfinalproject.ui.screens.tabContainer.tabContainerFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override val viewModel: ProfileViewModelImpl by viewModels()

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
                viewModel.logout()
            }

            ivAddImage.setOnClickListener {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
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
            Log.d("debugging", "Image addedd123")
        }
        lifecycleScope.launch {
            viewModel.user.collect {
                binding.run {
                    tvName.text = it.name
                    tvEmail.text = it.email
                }
            }
        }
        lifecycleScope.launch {
            viewModel.loggedOut.collect {
                val action = tabContainerFragmentDirections.actionLogout()
                navController.navigate(action)
            }
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

        Log.d("debugging" ,"notification shown")

    }
}