package com.vguivarc.wakemeup.transport.contact.addbyusername

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.vguivarc.wakemeup.R
import com.vguivarc.wakemeup.domain.external.entity.UserProfile
import com.vguivarc.wakemeup.transport.routeViewModel
import com.vguivarc.wakemeup.util.widget.LoadableButton


@Composable
fun AddByUsernameScreen(navController: NavController) {
    val addByUsernameViewModel: AddByUsernameViewModel =
        remember { navController.routeViewModel() }

    val state by addByUsernameViewModel.container.stateFlow.collectAsState()

    val side by addByUsernameViewModel.container.sideEffectFlow.collectAsState(initial = null)

    AddByUsernameContent(
        addByUsernameViewModel,
        state.searchText,
        state.user,
        state.showBeforeSearch,
        state.showEmptyResult,
        state.isContact,
        state.isSearchLoading,
        state.isContactLoading
    )
    side?.let {
        handleSideEffect(LocalContext.current, navController, it)
    }
    addByUsernameViewModel.ok()
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AddByUsernameContent(
    addByUsernameViewModel: AddByUsernameViewModel?,
    searchText: String,
    userResult: UserProfile?,
    showBeforeSearch: Boolean,
    showEmptyResult: Boolean,
    contact: Boolean,
    searchLoading: Boolean,
    contactLoading: Boolean
) {
    Scaffold(
        content = {
            Column (modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally){
                OutlinedTextField(
                    modifier = Modifier
                        .padding(32.dp, 8.dp)
                        .fillMaxWidth(),
                    value = searchText,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    onValueChange = { addByUsernameViewModel?.setSearchUsernameText(it) },
                    shape = RoundedCornerShape(8.dp),
                    trailingIcon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_baseline_search_24),
                            contentDescription = "Search",
                            colorFilter = ColorFilter.tint(
                                Color.Black
                            ),
                            modifier = Modifier.clickable(
                                onClick = { addByUsernameViewModel?.getSearchedUser() },
//                                indication = null
                            ),
                        )
                    },
                    placeholder = { Text("Search a username") },
                    colors = TextFieldDefaults.textFieldColors(
                        focusedIndicatorColor = Color.LightGray,
                        disabledIndicatorColor = Color.LightGray,
                        unfocusedIndicatorColor = Color.LightGray,
                        backgroundColor = Color.Transparent,
                    )
                )

                if (searchLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                        Column(
                            modifier = Modifier.align(Alignment.Center),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Loading")
                            CircularProgressIndicator()
                        }
                    }
                }
                Spacer(Modifier.size(16.dp))
                if(showBeforeSearch) {
                    Text(
                        "Enter username", textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                    Spacer(Modifier.size(16.dp))
                }
                if(showEmptyResult) {
                    Text(
                        "Pas de résultat", textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                    Spacer(Modifier.size(16.dp))
                }
                if(userResult !=null) {
                    Image(
                        painter = painterResource(R.drawable.main_logo),
                        contentDescription = "picture profile",
                        modifier = Modifier
                            .size(80.dp)
                    )
                    Spacer(Modifier.size(16.dp))
                    Text(
                        userResult.username, textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                    Spacer(Modifier.size(16.dp))
                        LoadableButton(
                            loading = contactLoading,
                            onClick = { addByUsernameViewModel?.changeStatusContact() },
                            backgroundColor = MaterialTheme.colors.primary,
                            text = {
                                if(!contact) {
                                    Text("Ajouter")
                                } else {
                                    Text("Supprimer")
                                }
                            },
                            modifier = Modifier
                                .padding(32.dp, 0.dp)
                                .fillMaxWidth(),
                        )
                }
            }
        }
    )
}


private fun handleSideEffect(
    context: Context,
    navController: NavController,
    sideEffect: AddByUsernameSideEffect
) {
    when (sideEffect) {
        is AddByUsernameSideEffect.Toast -> Toast.makeText(
            context,
            sideEffect.textResource,
            Toast.LENGTH_SHORT
        ).show()
    }
}

@Preview
@Composable
fun AddByUsernameContentPreview() {
    AddByUsernameContent(
        null,
        searchText = "search",
        userResult = null,
        showBeforeSearch = false,
        showEmptyResult = false,
        contact = false,
        searchLoading = false,
        contactLoading = false
    )
}

@ExperimentalAnimationApi
@Composable
fun SearchBar(value:String, viewModel: AddByUsernameViewModel?,  hint: String,
              endIcon: ImageVector? = Icons.Default.Cancel) {
    // we are creating a variable for
    // getting a value of our text field.

    Surface(
        shape = RoundedCornerShape(50),
      //  color = searchFieldColor
    ) {
        Box(
            modifier = Modifier
                .height(40.dp)
                .padding(start = 16.dp, end = 12.dp),
            contentAlignment = Alignment.CenterStart
        )
        {
            if (value.isEmpty()) {
                Text(
                    text = "Search...",
                    style = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface.copy(ContentAlpha.medium)),
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    modifier = Modifier.weight(1f),
                    value = value,
                    onValueChange = { viewModel?.setSearchUsernameText(it) },
                    singleLine = true,
                  //  cursorColor = YourColor,
                )
                endIcon?.let {
                    AnimatedVisibility(
                        visible = value.isNotEmpty(),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Image(
                            modifier = Modifier
                                .size(24.dp)
                                /*.clickable(
                                    onClick = { textValue = TextFieldValue() },
                                    indication = null
                                ),*/,
                            imageVector = endIcon,
                            contentDescription = ""
                            //colorFilter = iconColor
                        )
                    }
                }
            }
        }
    }
}
