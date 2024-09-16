 package com.luis2576.dev.rickandmorty.after

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.luis2576.dev.rickandmorty.after.ui.navigation.Navigation
import com.luis2576.dev.rickandmorty.after.ui.theme.RickAndMortyTheme
import dagger.hilt.android.AndroidEntryPoint

 @AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RickAndMortyTheme {
                Navigation()
            }
        }
    }
}