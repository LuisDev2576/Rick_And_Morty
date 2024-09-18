 package com.luis2576.dev.rickandmorty

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.luis2576.dev.rickandmorty.ui.navigation.Navigation
import com.luis2576.dev.rickandmorty.ui.theme.RickAndMortyTheme
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
     override fun attachBaseContext(newBase: Context?) {
         val newOverride = Configuration(newBase?.resources?.configuration)
         if (newOverride.fontScale != 1f)
             newOverride.fontScale = 1f
         applyOverrideConfiguration(newOverride)
         super.attachBaseContext(newBase)
     }
}