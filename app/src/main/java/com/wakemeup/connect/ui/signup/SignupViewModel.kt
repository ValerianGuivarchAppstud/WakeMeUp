package com.wakemeup.connect.ui.signup

import android.app.Activity
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.DatabaseReference
import com.wakemeup.AppWakeUp
import com.wakemeup.R
import com.wakemeup.connect.ui.ConnectResult

class SignupViewModel : ViewModel() {

    private val inutile : String = "test"

    private val _signupForm = MutableLiveData<SignupFormState>()
    val signupFormState: LiveData<SignupFormState> = _signupForm

    private val _signupResult = MutableLiveData<ConnectResult>()
    val signupResult: LiveData<ConnectResult> = _signupResult


    private lateinit var reference: DatabaseReference

    //todo ajouter connexion Google ? Facebook ?

    //TODO refactoring, imbriquer firebase dans une classe, des fois que je change

    //enregistre les données du nouveau compte utilisateur dans firebase
    fun signup(
        activitySignun: Activity,
        username: String,
        password: String,
        mail: String,
        phone: String
    ) {
        AppWakeUp.auth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener() { it ->
            if (it.isSuccessful) {
                val firebaseUser = AppWakeUp.auth.currentUser!!
                val userId = firebaseUser.uid
                reference = AppWakeUp.database.getReference("Users").child(userId)
                val hashmap = hashMapOf<String, String>()
                hashmap["id"] = userId
                hashmap["imageUrl"] = "default"
                hashmap["phone"] = phone
                hashmap["username"] = username
                hashmap["mail"] = mail
                //TODO hashmap["video_favori"] = "vide"
                reference.setValue(hashmap).addOnCompleteListener {
                    if (it.isSuccessful) {
                        _signupResult.value =
                            ConnectResult(AppWakeUp.auth.currentUser)
                    } else {
                        Log.e("fail connect", "2")
                        _signupResult.value =
                            ConnectResult(error = R.string.signup_failed)
                    }
                }
            } else {
                Log.e("fail connect", it.toString())
                Log.e("fail connect", it.exception.toString())
                _signupResult.value =
                    ConnectResult(error = R.string.signup_failed)
            }
        }
    }

    fun signupDataChanged(
        username: String,
        password1: String,
        password2: String,
        mail: String,
        phone: String
    ) {
        if (!isUserNameValid(username)) {
            _signupForm.value =
                SignupFormState(usernameSignupError = R.string.invalid_username)
        } else if (!isPasswordValid(password1)) {
            _signupForm.value =
                SignupFormState(passwordSignupError = R.string.invalid_password)
        } else if (!isPasswordsEgals(password1, password2)) {
            _signupForm.value =
                SignupFormState(password2SignupError = R.string.invalid_password_again_signup)
        } else if (!isMailValid(mail)) {
            _signupForm.value =
                SignupFormState(mailSignupError = R.string.invalid_mail_signup)
        } else {
            _signupForm.value = SignupFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return (username.length >= 5) && !username.contains(" ")
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length >= 6
    }


    // A placeholder password validation check
    private fun isPasswordsEgals(password1: String, password2: String): Boolean {
        return password1 == password2
    }

    // A placeholder password validation check
    private fun isMailValid(mail: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(mail).matches()
    }
}
