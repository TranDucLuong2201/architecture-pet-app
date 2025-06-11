//@file:OptIn(ExperimentalSharedTransitionApi::class)
//
//package com.dluong.pet.presentation.my_profile
//
//import androidx.compose.animation.*
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import coil.compose.AsyncImage
//import com.dluong.pet.domain.model.Pet
//import com.dluong.pet.presentation.favorite.FavoriteUiState
//import com.dluong.pet.presentation.favorite.FavoriteViewModel
//
//@Composable
//fun ProfileScreen() {
//    var showDetails by remember { mutableStateOf(false) }
//    var selectedPet by remember { mutableStateOf<Pet?>(null) }
//    val viewModel = hiltViewModel<FavoriteViewModel>()
//    val favorites = viewModel.favoriteState.collectAsState()
//
//    SharedTransitionLayout {
//        AnimatedContent(
//            targetState = showDetails,
//            transitionSpec = { fadeIn() togetherWith fadeOut() },
//            label = "profile_transition"
//        ) { targetState ->
//            if (!targetState) {
//                if (favorites.value is FavoriteUiState.Success) {
//                    val pets = (favorites.value as FavoriteUiState.Success).data
//                    MainContent(
//                        pets = pets,
//                        onShowDetails = {
//                            selectedPet = it
//                            showDetails = true
//                        },
//                        animatedVisibilityScope = this@AnimatedContent,
//                        sharedTransitionScope = this@SharedTransitionLayout
//                    )
//                }
//            } else {
//                selectedPet?.let {
//                    DetailsContent(
//                        pet = it,
//                        onBack = { showDetails = false },
//                        animatedVisibilityScope = this@AnimatedContent,
//                        sharedTransitionScope = this@SharedTransitionLayout
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun MainContent(
//    pets: List<Pet>,
//    onShowDetails: (Pet) -> Unit,
//    modifier: Modifier = Modifier,
//    sharedTransitionScope: SharedTransitionScope,
//    animatedVisibilityScope: AnimatedVisibilityScope
//) {
//    with(sharedTransitionScope) {
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(3),
//            modifier = modifier.fillMaxSize(),
//            contentPadding = PaddingValues(8.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            horizontalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            items(pets) { pet ->
//                Column(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .clickable { onShowDetails(pet) }
//                ) {
//                    AsyncImage(
//                        model = pet.urlImage,
//                        contentDescription = null,
//                        contentScale = ContentScale.Crop,
//                        modifier = Modifier
//                            .size(150.dp)
//                            .sharedBounds(
//                                sharedContentState = rememberSharedContentState(key = "pet_${pet.id}"),
//                                animatedVisibilityScope = animatedVisibilityScope,
//                                enter = fadeIn(),
//                                exit = fadeOut(),
//                                resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
//                            )
//                    )
//                }
//            }
//        }
//    }
//}
//
//@Composable
//private fun DetailsContent(
//    pet: Pet,
//    onBack: () -> Unit,
//    sharedTransitionScope: SharedTransitionScope,
//    animatedVisibilityScope: AnimatedVisibilityScope
//) {
//    with(sharedTransitionScope) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp)
//        ) {
//            AsyncImage(
//                model = pet.urlImage,
//                contentDescription = null,
//                contentScale = ContentScale.Crop,
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(300.dp)
//                    .sharedBounds(
//                        sharedContentState = rememberSharedContentState(key = "pet_${pet.id}"),
//                        animatedVisibilityScope = animatedVisibilityScope,
//                        enter = fadeIn(),
//                        exit = fadeOut(),
//                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
//                    )
//            )
//            Spacer(modifier = Modifier.height(24.dp))
//            Text(text = "Pet ID: ${pet.id}")
//            Text(text = "Size: ${pet.width}x${pet.height}")
//            Spacer(modifier = Modifier.height(16.dp))
//            Button(onClick = onBack) {
//                Text("Back")
//            }
//        }
//    }
//}
