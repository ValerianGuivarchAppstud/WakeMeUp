package com.vguivarc.wakemeup.transport.account.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.FacebookSdk
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.transport.routeViewModel
import timber.log.Timber


@Composable
fun AuthScreen(navController: NavController) {
    val authViewModel: AuthViewModel = remember { navController.routeViewModel() }
   // val activity = LocalContext.current as Activity
    val state by authViewModel.container.stateFlow.collectAsState()

    val side by authViewModel.container.sideEffectFlow.collectAsState(initial = null)

    AuthContent(
        authViewModel, state.mail, state.password, state.passwordVisibility, state.isLoading
    )

    side?.let {
        handleSideEffect(authViewModel, LocalContext.current, navController, it)
    }
    authViewModel.ok()
}


@Composable
fun AuthContent(
    authViewModel: AuthViewModel?,
    mail: String,
    password: String,
    passwordVisibility: Boolean,
    loading: Boolean
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(Color.White),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.End
        ) {
            Image(
                painter = painterResource(R.drawable.close),
                contentDescription = "Close",
                modifier = Modifier
                    .size(40.dp)
                    .padding(all = 8.dp)
                    .clickable { authViewModel?.close() },
                colorFilter = ColorFilter.tint(
                    Color.Black
                )
            )
        }
        Text(
            "Connexion", textAlign = TextAlign.Center,
            fontSize = 30.sp
        )
        Spacer(Modifier.size(32.dp))
        Image(
            painter = painterResource(R.drawable.main_logo),
            contentDescription = "Connexion",
            modifier = Modifier
                .size(80.dp)
        )
        Spacer(Modifier.size(32.dp))
        OutlinedTextField(
            modifier = Modifier
                .padding(32.dp, 0.dp)
                .fillMaxWidth(),
            value = mail,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { authViewModel?.editMail(it) },
            //label = { Text(text = "Mail") },
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_mail_outline_24),
                    contentDescription = "Mail",
                    colorFilter = ColorFilter.tint(
                        Color.Black
                    )
                )
            },
            placeholder = { Text("Enter Email") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.LightGray,
                disabledIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray,
                backgroundColor = Color.Transparent,
            )
        )
        Spacer(Modifier.size(20.dp))
        OutlinedTextField(
            modifier = Modifier
                .padding(32.dp, 0.dp)
                .fillMaxWidth(),
            value = password,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            onValueChange = { authViewModel?.editPassword(it) },
            //label = { Text(text = "Password") },
            shape = RoundedCornerShape(8.dp),
            leadingIcon = {
                Image(
                    painter = painterResource(id = R.drawable.ic_baseline_lock_open_24),
                    contentDescription = "Password",
                    colorFilter = ColorFilter.tint(
                        Color.Black
                    )
                )
            },
            placeholder = { Text("Enter password") },
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.LightGray,
                disabledIndicatorColor = Color.LightGray,
                unfocusedIndicatorColor = Color.LightGray,
                backgroundColor = Color.Transparent,
            ),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    authViewModel?.setPasswordVisibility(!passwordVisibility)
                }) {
                    Icon(imageVector = image, "Password Visibility")
                }
            }
        )
        Spacer(Modifier.size(32.dp))
        ExtendedFloatingActionButton(
            onClick = { authViewModel?.clickOnLogin() },
            backgroundColor = MaterialTheme.colors.primary,
            text = {
                Text("Se connecter")
            },
            modifier = Modifier
                .padding(32.dp, 0.dp)
                .fillMaxWidth(),
        )
        Spacer(Modifier.size(20.dp))
        ExtendedFloatingActionButton(
            onClick = { authViewModel?.clickOnRegister() },
            backgroundColor = MaterialTheme.colors.primary,
            text = {
                Text("Créer un compte")
            },
            modifier = Modifier
                .padding(32.dp, 0.dp)
                .fillMaxWidth(),
        )
        Spacer(Modifier.size(20.dp))
        TextButton(onClick = { authViewModel?.forgotPassword() }) {
            Text("Mot de passe oublié")
        }
      /*  Spacer(Modifier.size(2.dp))
        Row(
            modifier = Modifier
                .background(Color.White),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                painter = painterResource(R.drawable.logo_fb),
                contentDescription = "Connexion",
                modifier = Modifier
                    .size(164.dp)
                    .clickable { authViewModel?.clickOnFacebookLogin() }
            )
        }*/
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
    authViewModel: AuthViewModel,
    context: Context,
    navController: NavController,
    sideEffect: AuthSideEffect
) {
    when (sideEffect) {
        is AuthSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
        /*is AuthSideEffect.LoginFacebook -> {
            Timber.e("post6")

            if (AccessToken.getCurrentAccessToken() != null) {
                LoginManager.getInstance().logOut()
            }
            val loginManager = LoginManager.getInstance()

            setupFacebookLogic(authViewModel)
            loginManager.logInWithReadPermissions(
                activity,
                listOf("email")
            )
        }*/
        is AuthSideEffect.NavigationToRegister -> {
        } // TODO
        is AuthSideEffect.Close -> {
            navController.popBackStack()
        }
    }
}


@Preview
@Composable
fun AuthContentPreview() {
    AuthContent(authViewModel = null,mail =  "test@mail.Fr", password = "password", passwordVisibility = false, loading = true)
}


/*override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel.observe(this, state = ::render, sideEffect = ::handleSideEffect)
}

private fun render(state: AuthState) {
    Timber.d("render $state")
    binding.loader.isVisible = state.isLoading
    if(state.isConnected) {
        activity?.onBackPressed()
    }
}

private fun handleSideEffect(sideEffect: AuthSideEffect) {
    when (sideEffect) {
        is AuthSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
    }
}*/



    private fun setupFacebookLogic(authViewModel: AuthViewModel) {
        val callbackManager = CallbackManager.Factory.create()


        if (!FacebookSdk.isInitialized()) {
            FacebookSdk.fullyInitialize()
        }
        // Callback registration
        LoginManager.getInstance()
            .registerCallback(
                callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(loginResult: LoginResult) {
                        Timber.e("post1")
                        authViewModel.loginWithFacebook(loginResult.accessToken.token)
                    }

                    override fun onCancel() {
                        Timber.e("post1")
                        authViewModel.error(R.string.login_facebook_cancelled)
                    }

                    override fun onError(exception: FacebookException) {
                        Timber.e("post1")
                        authViewModel.error(R.string.login_facebook_error)
                    }
                }
            )
    }
/*
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
/*
    private fun onLoginFacebookSuccessful() {
        // TODO use google smart lock to save password

        accountViewModel.getAndUpdateUserInfo()
            .observe(
                viewLifecycleOwner,
                Observer { resource ->
                    when (resource) {
                        is Success -> {
                            activity?.setResult(Activity.RESULT_OK)
                            activity?.finish()
                        }
                        is Loading -> showLoading()
                        is Fail -> {
                            showContent()
                            toast(R.string.error_default_message)
                        }
                    }
                }
            )
    }

    override fun showError(throwable: Throwable?) {
        if (throwable is FacebookMissingMailError) {
            val dialogView =
                layoutInflater.inflate(R.layout.facebook_login_error_dialog, null, false)
            val dialog = alert {
                customView = dialogView
            }.show()
            dialogView.find<View>(R.id.facebookErrorDialogCTA)
                .setOnClickListener { dialog.dismiss() }
        } else {
            toast(R.string.login_facebook_error)
        }

        showContent()
    }
*/
}*/

/*
class AuthFragment : BaseLceFragment() {
    private val callbackManager = CallbackManager.Factory.create()
    private val authViewModel: AuthViewModel by sharedViewModel()
    private val accountViewModel: AccountViewModel by sharedViewModel()

    private var clickOnLogin: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_auth, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginClose.setOnClickListener {
            activity?.onBackPressed()
        }
        loginEmail.setOnClickListener {
            clickOnLogin = true
            findNavController().navigate(R.id.action_login_email)
        }
        setupFacebookLogic()
        val loginLegals = view.findViewById<TextView>(R.id.loginLegals)
        loginLegals.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.legals_link)))
            startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        clickOnLogin = false
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}
*/