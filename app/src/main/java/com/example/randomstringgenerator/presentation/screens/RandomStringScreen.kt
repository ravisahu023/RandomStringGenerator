package com.example.randomstringgenerator.presentation.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActionScope
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.randomstringgenerator.R
import com.example.randomstringgenerator.domain.model.RandomStringItem
import com.example.randomstringgenerator.presentation.viewmodel.RandomStringViewModel
import com.example.randomstringgenerator.utils.Utility

@Composable
fun RandomStringScreen(viewModel: RandomStringViewModel) {
    val strings by viewModel.randomStrings.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var inputLength by remember { mutableStateOf("") }
    var inputError by remember { mutableStateOf<String?>(null) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))

        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = inputLength,
                onValueChange = {
                    inputLength = it
                    inputError = Utility.getValidationError(it)
                },
                label = { Text(stringResource(R.string.string_length)) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number, imeAction = ImeAction.Done
                ),
                isError = inputError != null,
                modifier = Modifier.fillMaxWidth()
            )
            inputError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Button(
            onClick = {
                //validation on click
                inputError = Utility.getValidationError(inputLength)
                if (inputError != null) return@Button

                val length = inputLength.toInt()
                viewModel.generateString(length)
            }, enabled = !isLoading && inputLength.isNotBlank(), modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                Text(
                    stringResource(R.string.generating_random_string),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                Text(stringResource(R.string.generate))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.clearAll() },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.clear_all))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.filterByFav() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.only_favourite))
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(strings.size) { item ->
                RandomStringItemCard(item = strings.get(item), markFav = { viewModel.markFavourite(it) }, onDelete = {
                    viewModel.deleteItem(it)
                })
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
fun RandomStringItemCard(
    item: RandomStringItem,
    markFav: (RandomStringItem) -> Unit,
    onDelete: (RandomStringItem) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(stringResource(R.string.value, item.value))
            Text(stringResource(R.string.length, item.length))
            Text(stringResource(R.string.created, Utility.formatIsoDate(item.created)))
            Button(
                onClick = { markFav(item) }, modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(if(item.isFavourite) R.string.favorite else R.string.not_favorite))
            }
            Button(
                onClick = { onDelete(item) }, modifier = Modifier.padding(top = 8.dp)
            ) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}
