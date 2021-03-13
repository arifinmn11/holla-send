package com.enigma.application.presentation.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.databinding.FragmentRegisterBinding
import com.enigma.application.presentation.login.LoginViewModel
import com.enigma.application.utils.Constans
import com.wajahatkarim3.easyvalidation.core.view_ktx.validEmail
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*

@AndroidEntryPoint
class RegisterFragment : Fragment() {
    lateinit var binding: FragmentRegisterBinding
    lateinit var viewModel: RegisterViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(layoutInflater)
        binding.apply {

            signUpButton.setOnClickListener {
                val email = etEmail.text.toString()
                val username = etUsername.text.toString()
                val password = etPassword.text.toString()
                val passwordConfirm = etPasswordConfirm.text.toString()

                viewModel.checkValidation(email, username, password, passwordConfirm)
            }
        }
        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
    }

    fun subscribe() {
        viewModel.getValidation().observe(this) {
            etUsername.background = resources.getDrawable(R.drawable.radius_edit_text)
            etEmail.background = resources.getDrawable(R.drawable.radius_edit_text)
            etPassword.background = resources.getDrawable(R.drawable.radius_edit_text)
            etPasswordConfirm.background = resources.getDrawable(R.drawable.radius_edit_text)

            when (it.status) {
                Constans.VALIDATION_SUCCESS -> {
                    findNavController().navigate(R.id.action_registerFragment_to_biodataFragment)
                }
                Constans.VALIDATION_CONFIRM_PASSWORD -> {
                    etPasswordConfirm.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_USERNAME -> {
                    etUsername.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_EMAIL -> {
                    etEmail.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_PASSWORD -> {
                    etPasswordConfirm.background = resources.getDrawable(R.drawable.error_edit_text)
                }
            }
        }
    }

    companion object {

        fun newInstance() = RegisterFragment()
    }
}