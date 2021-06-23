package com.vguivarc.wakemeup.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.base.*
import com.vguivarc.wakemeup.domain.entity.UserProfile
import com.vguivarc.wakemeup.util.isValidEmail
import com.vguivarc.wakemeup.util.isValidNickname
import com.vguivarc.wakemeup.viewmodel.AccountViewModel
import kotlinx.android.synthetic.main.fragment_account.*
import org.jetbrains.anko.support.v4.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class AccountFragment : BaseLceFragment() {

    private val accountViewModel: AccountViewModel by sharedViewModel()
    private var userInfo: UserProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_account, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Avoid TabBar to be sticky on the soft keyboard
        activity?.window?.setSoftInputMode(SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).setSupportActionBar(toolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)

        logout.setOnClickListener {
            accountViewModel.logout()
            findNavController().popBackStack()
        }

        accountSubmit.setOnClickListener {
            submitAccountEdit()
        }

        userInfo = accountViewModel.getUserInfo()
        fillFields(userInfo?.email, userInfo?.username)
    }

    private fun fillFields(email: String?, nickname: String?) {
        accountEmailInput.setText(email)
        accountNicknameInput.setText(nickname)
    }

    private fun resetErrors() {
        accountEmailLayout.error = null
        accountNicknameLayout.error = null
    }

    private fun submitAccountEdit() {
        resetErrors()
        val email = accountEmailInput.text.toString().trim()
        val nickname = accountNicknameInput.text.toString()

        if (fieldsAreValid(email, nickname)) {
            accountViewModel.editAccount(
                nickname = nickname,
                email = email
            ).observe(
                viewLifecycleOwner,
                Observer {
                    when (it) {
                        is Success -> {
                            showContent()
                            toast(R.string.account_edit_success)
                        }
                        is Loading -> showLoading()
                        is Fail -> {
                            showError()
                        }
                    }
                }
            )
        }
    }

    private fun fieldsAreValid(email: String, nickname: String): Boolean {
        if (!email.isValidEmail())
            accountEmailLayout.error = getString(R.string.form_email_format_error)

        if (!nickname.isValidNickname())
            accountNicknameLayout.error = getString(R.string.form_nickname_format_error)

        return email.isValidEmail() && nickname.isValidNickname()
    }

    override fun showError(throwable: Throwable?) {
        showContent()
        toast(R.string.error_default_message)
    }
}
