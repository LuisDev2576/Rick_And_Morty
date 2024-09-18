package com.luis2576.dev.rickandmorty.presentation.authentication.resetpassword

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.luis2576.dev.rickandmorty.R
import com.luis2576.dev.rickandmorty.domain.models.responses.ResetPasswordResponse
import com.luis2576.dev.rickandmorty.presentation.authentication.components.ActionButton
import com.luis2576.dev.rickandmorty.presentation.authentication.components.EmailTextField
import com.luis2576.dev.rickandmorty.presentation.authentication.components.Greeting
import com.luis2576.dev.rickandmorty.presentation.authentication.components.NavigationTextButton
import com.luis2576.dev.rickandmorty.ui.navigation.LoginScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResetPasswordScreen(
    resetPasswordViewModel: ResetPasswordViewModel = hiltViewModel(),
    navController: NavHostController
){
    val passwordResetStatus by resetPasswordViewModel.passwordResetStatus
    val focusManager =  LocalFocusManager.current


    Scaffold(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface),
        containerColor = MaterialTheme.colorScheme.surface,
        snackbarHost = {
            SnackbarHost(
                hostState = resetPasswordViewModel.snackbarHostState,
                snackbar = { data ->
                    Snackbar(
                        snackbarData = data,
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
                            if(resetPasswordViewModel.restartPasswordButtonEnabled){
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
                titleResId = R.string.recover_password_title,
                instructionsResId = R.string.recover_password_instructions
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
                    email = resetPasswordViewModel.restartPassworEmail,
                    onEmailChange = {resetPasswordViewModel.onRestartPassworEmailChange(it)},
                    isError = when(passwordResetStatus){
                        is ResetPasswordResponse.EmailError -> true
                        is ResetPasswordResponse.UserNotFoundError -> true
                        else -> {false}
                    },
                    onDone = {
                        focusManager.clearFocus()
                        resetPasswordViewModel.sendPasswordResetEmail()
                    }
                )
            }
            Spacer(modifier = Modifier
                .fillMaxWidth()
                .weight(1f))
            Column(
                modifier = Modifier
                    .widthIn(max = 600.dp)
                    .fillMaxWidth()
                    .weight(2f),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                ActionButton(
                    textId = R.string.send_button,
                    enabled = resetPasswordViewModel.restartPasswordButtonEnabled,
                    onClick = {
                        focusManager.clearFocus()
                        resetPasswordViewModel.sendPasswordResetEmail()
                    }
                )
                Spacer(modifier = Modifier.padding(20.dp))
                NavigationTextButton(
                    primaryTextId = R.string.remembered_password,
                    secondaryTextId = R.string.login_button,
                    onClick = {
                        if(resetPasswordViewModel.restartPasswordButtonEnabled){
                            navController.navigate(LoginScreen)
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
        if(resetPasswordViewModel.restartPasswordButtonEnabled){
            navController.navigate(LoginScreen)
        }
    }
}