package com.luis2576.dev.rickandmorty.presentation.authentication.login

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.luis2576.dev.rickandmorty.domain.models.responses.LoginResponse
import com.luis2576.dev.rickandmorty.presentation.authentication.components.ActionButton
import com.luis2576.dev.rickandmorty.presentation.authentication.components.EmailTextField
import com.luis2576.dev.rickandmorty.presentation.authentication.components.Greeting
import com.luis2576.dev.rickandmorty.presentation.authentication.components.NavToRestartPasswordButton
import com.luis2576.dev.rickandmorty.presentation.authentication.components.NavigationTextButton
import com.luis2576.dev.rickandmorty.presentation.authentication.components.PasswordTextField
import com.luis2576.dev.rickandmorty.ui.navigation.ChatsHomeScreen
import com.luis2576.dev.rickandmorty.ui.navigation.RegisterScreen
import com.luis2576.dev.rickandmorty.ui.navigation.RestartPasswordScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    navController: NavHostController
){
    val loginStatus by loginViewModel.loginStatus
    val focusManager =  LocalFocusManager.current
    val navigateToScreen by loginViewModel.navigateToScreen

    LaunchedEffect(navigateToScreen) {
        navigateToScreen?.let { screen ->
            when (screen) {
                //TODO
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
                hostState = loginViewModel.snackbarHostState,
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
                )
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
                titleResId = R.string.greeting_message,
                instructionsResId = R.string.login_prompt
            )

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.End
            ){
                EmailTextField(
                    email = loginViewModel.email,
                    onEmailChange = {loginViewModel.onEmailChange(it)},
                    isError = when(loginStatus){
                        is LoginResponse.EmailError -> true
                        is LoginResponse.UserNotFoundError -> true
                        else -> {false}
                    }
                )
                Spacer(modifier = Modifier.padding(5.dp))
                PasswordTextField(
                    password = loginViewModel.password,
                    onPasswordChange = {loginViewModel.onPasswordChange(it)},
                    isError = when(loginStatus){
                        is LoginResponse.PasswordError -> true
                        is LoginResponse.TooManyRequestsError -> true
                        else -> {false}
                    },
                    onDone = { loginViewModel.onLoginClick() },
                    clearFocusOnDone = true
                )
                NavToRestartPasswordButton(
                    onClick = {
                        if(loginViewModel.loginButtonEnabled){
                            navController.navigate(RestartPasswordScreen)
                        }
                    }
                )
            }

            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))

            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ActionButton(
                    textId = R.string.login_button,
                    enabled = loginViewModel.loginButtonEnabled,
                    onClick = {
                        focusManager.clearFocus()
                        loginViewModel.onLoginClick()
                    }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                NavigationTextButton(
                    primaryTextId = R.string.no_account,
                    secondaryTextId = R.string.create_account,
                    onClick = {
                        if(loginViewModel.loginButtonEnabled){
                            navController.navigate(RegisterScreen)
                        }
                    }
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(3f))
        }
    }
    BackHandler {

    }
}

