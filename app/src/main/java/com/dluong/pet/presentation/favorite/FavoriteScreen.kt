package com.dluong.pet.presentation.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.dluong.pet.presentation.ui.ext.PetsNavController

@Composable
fun FavoriteScreen(
    navController: PetsNavController,
    viewModel: FavoriteViewModel = hiltViewModel(),
) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Favorite")
    }
}