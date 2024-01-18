package com.xinhui.mobfinalproject.ui.screens.base

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.viewbinding.ViewBinding
import com.google.android.material.snackbar.Snackbar
import com.xinhui.mobfinalproject.ui.screens.base.viewModel.BaseViewModel
import com.xinhui.mobfinalproject.ui.screens.loading.LoadingFragment
import kotlinx.coroutines.launch

abstract class BaseFragment<T : ViewBinding> : Fragment() {
    abstract val viewModel: BaseViewModel
    lateinit var binding: T
    protected lateinit var navController: NavController

    private val loadingFragment by lazy { LoadingFragment() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.onCreateView()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = NavHostFragment.findNavController(this)

        setupUIComponents(view)
        setupViewModelObserver()
    }

    open fun setupUIComponents(view: View) {

    }

    open fun setupViewModelObserver() {
        lifecycleScope.launch {
            viewModel.error.collect {
                showSnackbar(it, true)
            }
        }

        lifecycleScope.launch {
            viewModel.success.collect {
               showSnackbar(it)
            }
        }

        lifecycleScope.launch {
            viewModel.isLoading.collect {
                val fragmentManager = requireActivity().supportFragmentManager
                if(it && !loadingFragment.isAdded)
                    fragmentManager.beginTransaction()
                        .add(loadingFragment, "loader")
                        .commitNowAllowingStateLoss()
                else if(!it && loadingFragment.isAdded)
                    fragmentManager.beginTransaction()
                        .remove(loadingFragment)
                        .commitNowAllowingStateLoss()
            }
        }
    }

    fun showSnackbar(msg: String, error: Boolean = false){
        val snackbar = Snackbar.make(binding.root, msg, Snackbar.LENGTH_LONG)
        if(error){
            snackbar.setBackgroundTint(
                ContextCompat.getColor(
                    requireContext(),
                    com.google.android.material.R.color.design_default_color_error
                )
            )
        }
        snackbar.show()
    }
}