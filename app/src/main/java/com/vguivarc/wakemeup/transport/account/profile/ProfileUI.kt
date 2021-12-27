package com.vguivarc.wakemeup.transport.account.profile

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.transport.routeViewModel


@Composable
fun AccountScreen(navController: NavController) {
    val profileViewModel: ProfileViewModel = remember { navController.routeViewModel() }

    val state by profileViewModel.container.stateFlow.collectAsState()

    val side by profileViewModel.container.sideEffectFlow.collectAsState(initial = null)

    AccountContent(profileViewModel, state.isLoading, state.userProfile)

    side?.let {
        handleSideEffect(profileViewModel, LocalContext.current, navController, it)
    }
}


@Composable
fun AccountContent(
    profileViewModel: ProfileViewModel?,
    loading: Boolean,
    userProfile: UserProfile?
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Ton compte")
        userProfile?.let {
            Text(text = it.username)
        }
        ExtendedFloatingActionButton(
            onClick = { profileViewModel?.logout() },
            backgroundColor = Color.Red,
            text = {
                Text("Deconnexion")
            },
            modifier = Modifier
                .padding(32.dp, 0.dp)
                .fillMaxWidth(),
        )
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (loading) {
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "Loading")
                CircularProgressIndicator()
            }
        }
    }
}


private fun handleSideEffect(
    profileViewModel: ProfileViewModel,
    context: Context,
    navController: NavController,
    sideEffect: AccountSideEffect
) {
    when (sideEffect) {
        is AccountSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
        is AccountSideEffect.Close -> {
            // activity?.onBackPressed()
        }
    }
}


@Preview
@Composable
fun AccountContentPreview() {
    AccountContent(null, true, null)
}

/*

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
}*/
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
