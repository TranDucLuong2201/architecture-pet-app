package com.dluong.pet.presentation.home

import android.annotation.SuppressLint
import android.os.Build.VERSION.SDK_INT
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.MoreVert
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import com.dluong.pet.domain.model.Pet
import com.dluong.pet.presentation.ui.components.HomeTopBar
import com.dluong.pet.presentation.ui.components.PetVideoPlayer

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val voteState by viewModel.voteStateFlow.collectAsState(initial = VoteUiState.Loading)
    val currentPage by viewModel.currentPage.collectAsState()
    val interactionSource = remember { MutableInteractionSource() }
    val isLoadingMore by viewModel.isLoadingMore.collectAsState()
    val context = LocalContext.current

    val pets = when (val state = voteState) {
        is VoteUiState.Success -> state.data
        else -> emptyList()
    }

    // Fix: Only create pager state when we have pets and preserve current page
    val pagerState = rememberPagerState(
        initialPage = if (pets.isNotEmpty()) currentPage.coerceAtMost(pets.size - 1) else 0,
        pageCount = { pets.size }
    )

    // Fix: Only load data if not already loaded
    LaunchedEffect(Unit) {
        if (voteState is VoteUiState.Loading && pets.isEmpty()) {
            viewModel.getVoteCats()
        }
    }

    // Fix: Sync pager state with saved current page after configuration change
    LaunchedEffect(pets.size, currentPage) {
        if (pets.isNotEmpty() && pagerState.currentPage != currentPage) {
            val targetPage = currentPage.coerceAtMost(pets.size - 1)
            if (targetPage != pagerState.currentPage) {
                pagerState.scrollToPage(targetPage)
            }
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                if (pets.isNotEmpty()) {
                    viewModel.updateCurrentPage(page)
                    if (page >= pets.lastIndex - 5) {
                        viewModel.loadMoreIfNeeded()
                    }
                }
            }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 28.dp),
            topBar = { HomeTopBar() },
        ) { paddingValues ->
            val contentModifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .navigationBarsPadding()
            when (voteState) {
                is VoteUiState.Loading -> {
                    Box(
                        modifier = contentModifier,
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is VoteUiState.Error -> {
                    Box(
                        modifier = contentModifier,
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Error loading data", style = MaterialTheme.typography.bodyLarge)
                    }
                }

                is VoteUiState.Success -> {
                    if (pets.isEmpty()) {
                        Box(
                            modifier = contentModifier,
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No pets available", style = MaterialTheme.typography.bodyLarge)
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(paddingValues)
                        ) {
                            VerticalPager(
                                modifier = Modifier
                                    .weight(1f)
                                    .combinedClickable(
                                        onDoubleClick = {
                                            if (pets.isNotEmpty() && pagerState.currentPage < pets.size) {
                                                viewModel.toggleLikedPets(pets[pagerState.currentPage])
                                            }
                                        },
                                        onClick = {},
                                        indication = null,
                                        interactionSource = interactionSource
                                    ),
                                state = pagerState
                            ) { page ->
                                if (page < pets.size) {
                                    val pet = pets[page]
                                    Card(
                                        modifier = Modifier.fillMaxSize()
                                    ) {
                                        PetItem(
                                            pet = pet,
                                            onLikeToggle = { viewModel.toggleLikedPets(it) }
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.height(52.dp))
                        }
                    }
                }
            }
        }

        // Fix: Add bounds checking for action buttons
        if (pets.isNotEmpty() && pagerState.currentPage < pets.size) {
            Column(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 20.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                IconButtonWithTitle(
                    onClick = { viewModel.toggleLikedPets(pets[pagerState.currentPage]) },
                    icon = Icons.Outlined.Favorite,
                    title = "Like",
                    isLiked = pets[pagerState.currentPage].isLiked
                )
                IconButtonWithTitle(onClick = {
                    viewModel.shareImageButton(
                        context = context,
                        pets[pagerState.currentPage].urlImage
                    )
                }, icon = Icons.Outlined.Share, title = "Share")
                IconButtonWithTitle(onClick = {}, icon = Icons.Outlined.MoreVert)
                Spacer(modifier = Modifier.height(52.dp))
            }
        }
    }
}

@Composable
fun IconButtonWithTitle(
    onClick: () -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    title: String? = null,
    isLiked: Boolean = false
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { onClick() },
            modifier = modifier
                .size(60.dp)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Another Action",
                modifier = Modifier.size(60.dp),
                tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.surfaceBright
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = title ?: "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiary
        )
    }
}

@Composable
fun PetItem(pet: Pet, onLikeToggle: (Pet) -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        PetMediaRenderer(pet = pet)
    }
}

@Composable
fun PetMediaRenderer(pet: Pet) {
    val context = LocalContext.current
    val imageLoader = remember(context) {
        ImageLoader.Builder(context).components {
            if (SDK_INT >= 28) add(ImageDecoderDecoder.Factory())
            else add(GifDecoder.Factory())
        }.build()
    }

    if (pet.isVideo) {
        PetVideoPlayer(url = pet.urlImage)
    } else {
        AsyncImage(
            model = pet.urlImage,
            imageLoader = imageLoader,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}