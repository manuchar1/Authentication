package com.tbcacademy.authentication.ui.auth.login

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.tbcacademy.authentication.R
import com.tbcacademy.authentication.databinding.LoginFragmentBinding
import com.tbcacademy.authentication.firestore.FireStore
import com.tbcacademy.authentication.models.User
import com.tbcacademy.authentication.ui.base.BaseFragment

class LoginFragment() : BaseFragment<LoginFragmentBinding>(LoginFragmentBinding::inflate),View.OnClickListener {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSingUp.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)


    }

    override fun onClick(view: View?) {
        if (view !=null) {
            when(view.id) {
                R.id.btnSingUp -> {
                    findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
                }
                R.id.btnLogin -> {
                    logInRegisteredUser()

                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.loginEmail.text.toString().trim { it <= ' ' }) -> {
                view?.let { Snackbar.make(it,resources.getString(R.string.err_msg_enter_email), Snackbar.LENGTH_SHORT).show() }
                false
            }
            TextUtils.isEmpty(binding.loginPassword.text.toString().trim { it <= ' ' }) -> {
                view?.let { Snackbar.make(it,resources.getString(R.string.err_msg_enter_password), Snackbar.LENGTH_SHORT).show() }
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser() {

        if (validateLoginDetails()) {

            showProgressBar()

            val email = binding.loginEmail.text.toString().trim { it <= ' ' }
            val password = binding.loginPassword.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->

                    if (task.isSuccessful) {

                        FireStore().getUserDetails(this@LoginFragment)

                    } else {
                        view?.let { Snackbar.make(it,task.exception!!.message.toString(), Snackbar.LENGTH_SHORT).show() }
                    }
                }
        }
    }


    fun userLoggedInSuccess(user: User) {

        hideProgressBar()

        Log.i("First Name: ", user.firstName)
        Log.i("Last Name: ", user.lastName)
        Log.i("Email: ", user.email)


        findNavController().navigate(R.id.action_loginFragment_to_userFragment)
    }

     fun hideProgressBar() {
        binding.progressBar5.visibility = View.INVISIBLE

    }

     fun showProgressBar() {
        binding.progressBar5.visibility = View.VISIBLE

    }



}