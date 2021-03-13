package com.enigma.application.presentation.register

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
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
class RegisterFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var binding: FragmentRegisterBinding
    lateinit var viewModel: RegisterViewModel

    private var identity: String = "KTP"


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
                val firstName = etFirstName.text.toString()
                val lastName = etLastName.text.toString()
                val address = etAddress.text.toString()
                val noIdentification = etIdentificationNumber.text.toString()

                viewModel.checkValidation(
                    email,
                    username,
                    password,
                    passwordConfirm,
                    firstName,
                    lastName,
                    identity,
                    noIdentification,
                    address
                )
            }

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.identity,
                android.R.layout.simple_spinner_dropdown_item
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerIdentity.adapter = adapter
            spinnerIdentity.onItemSelectedListener = this@RegisterFragment

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
            etFirstName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etLastName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etIdentificationNumber.background = resources.getDrawable(R.drawable.radius_edit_text)
            etAddress.background = resources.getDrawable(R.drawable.radius_edit_text)

            when (it.status) {
                Constans.VALIDATION_SUCCESS -> {

                }
                Constans.VALIDATION_USERNAME -> {
                    etUsername.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_EMAIL -> {
                    etEmail.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_PASSWORD -> {
                    etPassword.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_CONFIRM_PASSWORD -> {
                    etPasswordConfirm.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_FIRSTNAME -> {
                    etFirstName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_LASTNAME -> {
                    etLastName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_IDENTIFICATION -> {
                    spinnerIdentity.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_NO_IDENTIFICATION -> {
                    etIdentificationNumber.background =
                        resources.getDrawable(R.drawable.error_edit_text)
                }
                Constans.VALIDATION_ADDRESS -> {
                    etAddress.background = resources.getDrawable(R.drawable.error_edit_text)
                }
            }

        }
    }

    companion object {

        fun newInstance() = RegisterFragment()
    }
    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        identity = parent?.getItemAtPosition(position).toString()

    }

    override fun onNothingSelected(p0: AdapterView<*>?) {

    }
}