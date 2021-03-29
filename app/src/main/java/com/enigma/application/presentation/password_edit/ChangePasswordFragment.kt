package com.enigma.application.presentation.password_edit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.data.model.edit_password.RequestChangePassword
import com.enigma.application.databinding.FragmentChangePasswordBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constants
import com.enigma.application.utils.component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChangePasswordFragment : Fragment() {
    lateinit var binding: FragmentChangePasswordBinding
    lateinit var viewModel: ViewModelChangePassword
    lateinit var activityViewModel: ActivityViewModel
    lateinit var loadingDialog: AlertDialog

    private var old_password = ""
    private var new_password = ""
    private var confirm_new_passwor = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        binding = FragmentChangePasswordBinding.inflate(layoutInflater)
        loadingDialog = LoadingDialog.build(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityViewModel.setBottomVisibility(false)
        binding.apply {

            cancelButton.setOnClickListener {
                activityViewModel.setBottomVisibility(true)
                findNavController().navigate(R.id.action_global_profileFragment)
            }

            buttonBack.setOnClickListener {
                activityViewModel.setBottomVisibility(true)
                findNavController().navigate(R.id.action_global_profileFragment)
            }

            val callback: OnBackPressedCallback =
                object : OnBackPressedCallback(true /* enabled by default */) {
                    override fun handleOnBackPressed() {
                        activityViewModel.setBottomVisibility(true)
                        findNavController().navigate(R.id.action_global_profileFragment)
                    }
                }
            requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

            saveButton.setOnClickListener {
                old_password = etOldPassword.text.toString()
                new_password = etPassword.text.toString()
                confirm_new_passwor = etPasswordConfirm.text.toString()

                viewModel.checkValidation(
                    oldPassword = old_password,
                    newPassword = new_password,
                    confirmPassword = confirm_new_passwor
                )
            }

        }

        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ViewModelChangePassword::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    fun subscribe() {
        viewModel.getValidation().observe(this) { res ->

            binding.apply {
                etPasswordConfirm.background = resources.getDrawable(R.drawable.radius_edit_text)
                etPassword.background = resources.getDrawable(R.drawable.radius_edit_text)
                etOldPassword.background = resources.getDrawable(R.drawable.radius_edit_text)

                when (res.status) {
                    Constants.VALIDATION_SUCCESS -> {
                        loadingDialog.show()
                        val request = RequestChangePassword(
                            oldPassword = old_password,
                            newPassword = new_password
                        )
                        viewModel.updatePasswordApi(request).observe(requireActivity()) { res ->
                            when (res?.code) {
                                200 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Password has been saved!!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    findNavController().navigate(R.id.action_global_profileFragment)
                                }
                                400 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Someting wrong try again!!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }
                                else -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Someting wrong try again!!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            }

                        }
                    }
                    Constants.VALIDATION_CONFIRM_PASSWORD -> {
                        Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                        etPasswordConfirm.background =
                            resources.getDrawable(R.drawable.error_edit_text)
                    }
                    Constants.VALIDATION_NEW_PASSWORD -> {
                        Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                        etPassword.background = resources.getDrawable(R.drawable.error_edit_text)
                    }
                    Constants.VALIDATION_PASSWORD -> {
                        Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                        etOldPassword.background = resources.getDrawable(R.drawable.error_edit_text)
                    }
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChangePasswordFragment()
    }
}