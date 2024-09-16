package com.luis2576.dev.rickandmorty.after.presentation.authentication.register

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.after.domain.models.responses.RegistrationResponse
import com.luis2576.dev.rickandmorty.after.presentation.authentication.components.ActionButton
import com.luis2576.dev.rickandmorty.after.presentation.authentication.components.EmailTextField
import com.luis2576.dev.rickandmorty.after.presentation.authentication.components.Greeting
import com.luis2576.dev.rickandmorty.after.presentation.authentication.components.NavigationTextButton
import com.luis2576.dev.rickandmorty.after.presentation.authentication.components.PasswordTextField
import com.luis2576.dev.rickandmorty.after.presentation.authentication.components.ConfirmPasswordTextField
import com.luis2576.dev.rickandmorty.after.presentation.authentication.components.NameTextField
import com.luis2576.dev.rickandmorty.after.ui.navigation.ChatsHomeScreen
import com.luis2576.dev.rickandmorty.after.ui.navigation.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    registerViewModel: RegisterViewModel = hiltViewModel(),
    navController: NavHostController
){
    val registrationStatus by registerViewModel.registrationStatus
    val focusManager =  LocalFocusManager.current
    val navigateToScreen by registerViewModel.navigateToScreen

    LaunchedEffect(navigateToScreen) {
        navigateToScreen?.let { screen ->
            when (screen) {
                is ChatsHomeScreen -> {
                    navController.navigate(ChatsHomeScreen) {
                        popUpTo(navController.graph.startDestinationId) { inclusive = true }
                    }
                }
            }
        }
    }

    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = {
            SnackbarHost(
                hostState = registerViewModel.snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
                        actionColor = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(20),
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.surface
                    )
                }
            )
        },
        topBar = {
            TopAppBar(
                title = {  },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if(registerViewModel.registerButtonEnabled){
                                navController.navigate(LoginScreen)
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back button", tint = MaterialTheme.colorScheme.onBackground)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Greeting(
                titleResId = R.string.create_account_title,
            )

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ){
                NameTextField(
                    name = registerViewModel.fullName,
                    onNameChange = {registerViewModel.onFullNameChange(it)},
                    isError =  when(registrationStatus){
                        is RegistrationResponse.NameError -> true
                        else -> {false}
                    }
                )

                Spacer(modifier = Modifier.padding(5.dp))
                EmailTextField(
                    email = registerViewModel.email,
                    onEmailChange = {registerViewModel.onEmailChange(it)},
                    isError = when(registrationStatus){
                        is RegistrationResponse.EmailAlreadyInUseError -> true
                        is RegistrationResponse.EmailError -> true
                        else -> {false}
                    }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                PasswordTextField(
                    password = registerViewModel.password,
                    onPasswordChange = {registerViewModel.onPasswordChange(it)},
                    isError = when(registrationStatus){
                        is RegistrationResponse.WeakPasswordError -> true
                        is RegistrationResponse.PasswordError -> true
                        else -> {false}
                    },
                    onDone = {  }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                ConfirmPasswordTextField(
                    password = registerViewModel.confirmPassword,
                    onPasswordChange = {registerViewModel.onConfirmPasswordChange(it)},
                    isError = when(registrationStatus){
                        is RegistrationResponse.ConfirmPasswordError -> true
                        else -> {false}
                    },
                    onDone = { registerViewModel.onRegisterClick() }
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ActionButton(
                    textId = R.string.create_account_button,
                    enabled = registerViewModel.registerButtonEnabled,
                    onClick = {
                        focusManager.clearFocus()
                        registerViewModel.onRegisterClick()
                    }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                NavigationTextButton(
                    primaryTextId = R.string.already_have_account,
                    secondaryTextId = R.string.login_button,
                    onClick = {
                        if(registerViewModel.registerButtonEnabled){
                            navController.navigate(LoginScreen)
                        }
                    }
                )
            }

            Spacer(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(3f)
            )
        }
    }
    BackHandler {
        if(registerViewModel.registerButtonEnabled){
            navController.navigate(LoginScreen)
        }
    }
}




