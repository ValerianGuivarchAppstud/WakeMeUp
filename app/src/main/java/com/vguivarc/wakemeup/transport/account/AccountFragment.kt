package com.vguivarc.wakemeup.transport.account

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.databinding.FragmentAccountBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.orbitmvi.orbit.viewmodel.observe
import timber.log.Timber


class AccountFragment : Fragment(R.layout.fragment_account) {

    private val viewModel by viewModel<AccountViewModel>()

    private var _binding: FragmentAccountBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Avoid TabBar to be sticky on the soft keyboard
        activity?.window?.setSoftInputMode(SOFT_INPUT_ADJUST_NOTHING)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logout.setOnClickListener {
            viewModel.logout()
        }

        binding.accountSubmit.setOnClickListener {
            val email = binding.accountEmailInput.text.toString().trim()
            val username = binding.accountUsernameInput.text.toString()
            viewModel.submitAccountEdit(email, username)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
    }

    private fun render(state: AccountState) {
        Timber.d("render $state")
        if(!state.isConnected) {
            this.findNavController().popBackStack()
        }
        binding.accountEmailInput.setText(state.userProfile?.email)
        binding.accountUsernameInput.setText(state.userProfile?.username)
    }

    private fun handleSideEffect(sideEffect: AccountSideEffect) {
        when (sideEffect) {
            is AccountSideEffect.Toast -> Toast.makeText(
                context,
                sideEffect.textResource,
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}
    /*
class  : BaseLceFragment() {

    private val accountViewModel: AccountViewModel by sharedViewModel()
    private var userInfo: UserProfile? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout., container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as BaseActivity).setSupportActionBar(toolbar)
        (activity as BaseActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)


        userInfo = accountViewModel.getUserInfo()
        fillFields(userInfo?.email, userInfo?.nickname)
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


    }

    override fun showError(throwable: Throwable?) {
        showContent()
        toast(R.string.error_default_message)
    }*/
