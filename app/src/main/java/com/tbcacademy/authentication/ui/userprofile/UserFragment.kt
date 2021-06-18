package com.tbcacademy.authentication.ui.userprofile

import android.content.Context

import android.os.Bundle
import android.view.View
import com.tbcacademy.authentication.databinding.FragmentUserBinding
import com.tbcacademy.authentication.ui.base.BaseFragment

import com.tbcacademy.authentication.utils.Constants.AUTH_PREFERENCES
import com.tbcacademy.authentication.utils.Constants.LOGGED_IN_USERNAME


class UserFragment() : BaseFragment<FragmentUserBinding>(FragmentUserBinding::inflate) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        receiveData()

    }

    private fun receiveData() {
        val sharedPreferences =
            context?.getSharedPreferences(AUTH_PREFERENCES, Context.MODE_PRIVATE)
        val username = sharedPreferences?.getString(LOGGED_IN_USERNAME, "")
        binding.tvUsername.text = "Hello $username"
    }

}