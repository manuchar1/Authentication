package com.tbcacademy.authentication.ui.auth.register

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.tbcacademy.authentication.R
import com.tbcacademy.authentication.databinding.RegisterFragmentBinding
import com.tbcacademy.authentication.firestore.FireStore
import com.tbcacademy.authentication.models.User
import com.tbcacademy.authentication.ui.base.BaseFragment

class RegisterFragment() : BaseFragment<RegisterFragmentBinding>(RegisterFragmentBinding::inflate),
    View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.btnBack -> {
                    findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
                }
                R.id.btnRegister -> {
                    registerUser()

                }
            }
        }
    }

    private fun validateRegisterDetails(): Boolean {

        return when {

            TextUtils.isEmpty(binding.etFirstName.text.toString().trim { it <= ' ' }) -> {

                Toast.makeText(requireContext(), "Enter Name!", Toast.LENGTH_LONG).show()

                /*showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)*/
                false
            }

            TextUtils.isEmpty(binding.etLastName.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(requireContext(), "Enter Last Name!", Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(requireContext(), "Enter Email!", Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(requireContext(), "Enter Password!", Toast.LENGTH_LONG).show()
                false
            }

            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(requireContext(), "Enter Name!", Toast.LENGTH_LONG).show()
                false
            }

            binding.etPassword.text.toString()
                .trim { it <= ' ' } != binding.etConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {

                Toast.makeText(
                    requireContext(),
                    "password and confirm password mismatch!",
                    Toast.LENGTH_LONG
                ).show()
                false
            }

            else -> {

                true
            }
        }
    }

    private fun registerUser() {

        if (validateRegisterDetails()) {

            showProgressBar()

            val email: String = binding.etEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim { it <= ' ' }


            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        hideProgressBar()
                        if (task.isSuccessful) {
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            val user = User(
                                firebaseUser.uid,
                                binding.etFirstName.text.toString().trim { it <= ' ' },
                                binding.etLastName.text.toString().trim { it <= ' ' },
                                binding.etEmail.text.toString().trim { it <= ' ' }

                            )
                            FireStore().registerUser(this@RegisterFragment,user)

                            FirebaseAuth.getInstance().signOut()

                            view?.let {
                                Snackbar.make(
                                    it,
                                    "You are registered successfully. Your user id is ${firebaseUser.uid}",
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }

                            FirebaseAuth.getInstance().signOut()
                            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)


                        } else {
                            hideProgressBar()
                            view?.let {
                                Snackbar.make(
                                    it,
                                    task.exception!!.message.toString(),
                                    Snackbar.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
        }
    }


    fun userRegistrationSuccess() {
//        hideProgressBar()

        Toast.makeText(
            requireActivity(),
            resources.getString(R.string.register_success),
            Toast.LENGTH_SHORT
        ).show()

        FirebaseAuth.getInstance().signOut()

    }

    private fun hideProgressBar() {
        binding.progressBar3.visibility = View.INVISIBLE

    }

    private fun showProgressBar() {
        binding.progressBar3.visibility = View.VISIBLE

    }

}