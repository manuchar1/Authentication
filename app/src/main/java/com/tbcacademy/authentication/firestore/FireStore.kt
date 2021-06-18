package com.tbcacademy.authentication.firestore

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.tbcacademy.authentication.models.User
import com.tbcacademy.authentication.ui.auth.login.LoginFragment
import com.tbcacademy.authentication.ui.auth.register.RegisterFragment
import com.tbcacademy.authentication.utils.Constants.AUTH_PREFERENCES
import com.tbcacademy.authentication.utils.Constants.LOGGED_IN_USERNAME
import com.tbcacademy.authentication.utils.Constants.USERS


class FireStore {

    private val fireStore = FirebaseFirestore.getInstance()

    fun registerUser(fragment: RegisterFragment, userInfo: User) {

        fireStore.collection(USERS)

            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {


                fragment.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while registering the user.",
                    e
                )
            }
    }

    fun getCurrentUserID(): String {

        val currentUser = FirebaseAuth.getInstance().currentUser

        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid

        }
        return currentUserID
    }

    @SuppressLint("CommitPrefEdits")
    fun getUserDetails(fragment: Fragment) {

        fireStore.collection(USERS)

            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->

                Log.i(fragment.javaClass.simpleName, document.toString())
                val user = document.toObject(User::class.java)!!

                val sharePreferences = fragment.context?.getSharedPreferences(
                    AUTH_PREFERENCES,Context.MODE_PRIVATE)

                val editor: SharedPreferences.Editor? = sharePreferences?.edit()
                editor?.putString(
                    LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor?.apply()

                when (fragment) {
                    is LoginFragment -> {

                        fragment.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener { e ->
                when (fragment) {
                    is LoginFragment -> {
                        fragment.hideProgressBar()
                    }
                }

                Log.e(
                    fragment.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }
}