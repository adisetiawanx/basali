package com.capstone.basaliproject.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignupViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isSignupSuccessful = MutableLiveData<Boolean>()
    val isSignupSuccessful: LiveData<Boolean> = _isSignupSuccessful

    fun signup(userName: String, email: String, password: String) {
        _isLoading.value = true
        FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
            .addOnCompleteListener { task ->
                val isNewUser = task.result?.signInMethods?.isEmpty() ?: true
                if (isNewUser) {
                    // The email does not exist in the database, continue with the signup process
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val user = FirebaseAuth.getInstance().currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(userName)
                                    .build()

                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { task ->
                                        _isSignupSuccessful.value = task.isSuccessful
                                    }
                                _isLoading.value = false
                            } else {
                                _isSignupSuccessful.value = false
                            }
                        }
                } else {
                    // The email already exists in the database
                    _isSignupSuccessful.value = false
                }
            }
    }
}

