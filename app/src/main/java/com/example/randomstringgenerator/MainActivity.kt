package com.example.randomstringgenerator

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.randomstringgenerator.data.repository.RandomStringRepository
import com.example.randomstringgenerator.presentation.screens.RandomStringScreen
import com.example.randomstringgenerator.presentation.screens.ui.theme.RandomStringGeneratorTheme
import com.example.randomstringgenerator.presentation.viewmodel.RandomStringViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel by viewModels<RandomStringViewModel>()
        enableEdgeToEdge()
        setContent {
            RandomStringGeneratorTheme {
                RandomStringScreen(viewModel)
            }
        }
    }
}