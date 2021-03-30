package com.enigma.application.presentation.profile_edit

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.enigma.application.R
import com.enigma.application.data.model.edit_profile.RequestProfile
import com.enigma.application.data.model.edit_profile.UserDetails
import com.enigma.application.databinding.FragmentChangeProfileBinding
import com.enigma.application.presentation.activity.ActivityViewModel
import com.enigma.application.utils.Constants
import com.enigma.application.utils.component.LoadingDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_register.*
import javax.inject.Inject

@AndroidEntryPoint
class ChangeProfileFragment : Fragment(), AdapterView.OnItemSelectedListener {
    lateinit var viewModel: ViewModelChangeProfile
    lateinit var activityViewModel: ActivityViewModel
    lateinit var binding: FragmentChangeProfileBinding
    lateinit var loadingDialog: AlertDialog

    private var identity: String = ""
    private var firstName: String = ""
    private var lastName: String = ""
    private var contactNumber: String = ""
    private var identificationNumber: String = ""
    private var username: String = ""
    private var email: String = ""

    @Inject
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        subscribe()
        loadingDialog = LoadingDialog.build(requireContext())
        binding = FragmentChangeProfileBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityViewModel.setBottomVisibility(false)

        binding.apply {

            val adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.identity,
                android.R.layout.simple_spinner_dropdown_item
            )

            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerIdentity.adapter = adapter
            spinnerIdentity.onItemSelectedListener = this@ChangeProfileFragment

            viewModel.getProfile().observe(viewLifecycleOwner) { res ->
                res?.code?.apply {
                    when (this) {
                        200 -> {
                            res?.data?.userDetails.apply {
                                etEmail.setText(res?.data?.email.toString())
                                etUsername.setText(res?.data?.username.toString())
                                etFirstName.setText(this?.firstName.toString())
                                etLastName.setText(this?.lastName.toString())
                                etIdentificationNumber.setText(this?.identificationNumber.toString())
                                etContact.setText(this?.contactNumber.toString())

                                email = res?.data?.email!!
                                username = res?.data?.username!!

                                when (this?.identityCategory) {
                                    "KTP" -> spinnerIdentity.setSelection(0)
                                    "SIM" -> spinnerIdentity.setSelection(1)
                                }
                            }
                        }
                        401 -> {
                            Toast.makeText(
                                requireContext(),
                                "Your token is invalid!",
                                Toast.LENGTH_SHORT
                            ).show()
                            findNavController().navigate(R.id.action_global_loginFragment)
                        }
                        else -> {
                            Toast.makeText(
                                requireContext(),
                                "Something wrong try again!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }

            saveButton.setOnClickListener {
                firstName = etFirstName.text.toString()
                lastName = etLastName.text.toString()
                contactNumber = etContact.text.toString()
                identificationNumber = etIdentificationNumber.text.toString()

                viewModel.checkValidation(
                    firstName = firstName,
                    lastName = lastName,
                    contactNumber = contactNumber,
                    identification = identity,
                    noIdentification = identificationNumber
                )
            }

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

        }
        return binding.root
    }

    fun initViewModel() {
        viewModel = ViewModelProvider(this).get(ViewModelChangeProfile::class.java)
        activityViewModel = ViewModelProvider(requireActivity()).get(ActivityViewModel::class.java)
    }

    fun subscribe() {
        viewModel.getValidation().observe(this) { res ->
            etFirstName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etLastName.background = resources.getDrawable(R.drawable.radius_edit_text)
            etIdentificationNumber.background = resources.getDrawable(R.drawable.radius_edit_text)
            etContact.background = resources.getDrawable(R.drawable.radius_edit_text)

            when (res.status) {
                Constants.VALIDATION_SUCCESS -> {
                    loadingDialog.show()

                    val request = RequestProfile(
                        email = email,
                        username = username,
                        userDetails = UserDetails(
                            identityCategory = identity,
                            identificationNumber = identificationNumber,
                            firstName = firstName,
                            lastName = lastName,
                            contactNumber = contactNumber
                        )
                    )

                    res?.let {
                        viewModel.updateProfile(request).observe(this) { update ->
                            when (update?.code) {
                                200 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Data has been saved!!",
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                    findNavController().navigate(R.id.action_global_profileFragment)
                                }
                                500 -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "Password or Username invalid!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                else -> {
                                    loadingDialog.hide()
                                    Toast.makeText(
                                        requireContext(),
                                        "$it",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        }
                    }
                }

                Constants.VALIDATION_FIRSTNAME -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etFirstName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constants.VALIDATION_LASTNAME -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etLastName.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constants.VALIDATION_IDENTIFICATION -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    spinnerIdentity.background = resources.getDrawable(R.drawable.error_edit_text)
                }
                Constants.VALIDATION_NO_IDENTIFICATION -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etIdentificationNumber.background =
                        resources.getDrawable(R.drawable.error_edit_text)
                }
                Constants.VALIDATION_ADDRESS -> {
                    Toast.makeText(requireContext(), res.message, Toast.LENGTH_SHORT).show()
                    etContact.background = resources.getDrawable(R.drawable.error_edit_text)
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ChangeProfileFragment()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        identity = parent?.getItemAtPosition(position).toString()
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }
}