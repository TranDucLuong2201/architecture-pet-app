package com.dluong.pet.presentation.home

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.dluong.pet.domain.model.Pet
import timber.log.Timber

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@Preview(showSystemUi = true)
fun HomeScreen() {
    val viewModel: HomeViewModel = hiltViewModel()
    val state = viewModel.voteStateFlow.collectAsState()
    Timber.tag("HomeScreen: ${state.value}").d("${state.value}")

    val voteState = viewModel.voteStateFlow.collectAsState(initial = VoteUiState.Loading)
    val pets = when (voteState.value) {
        is VoteUiState.Loading -> emptyList()
        is VoteUiState.Success -> (voteState.value as VoteUiState.Success).data
        is VoteUiState.Error -> emptyList() // Handle error state appropriately
    }

    // Trigger fetching data

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 24.dp, start = 16.dp, end = 16.dp),
        topBar = { HomeTopBar() },
    ) {
        if (voteState.value is VoteUiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (pets.isNotEmpty()) {
                val pagerState = rememberPagerState(
                    pageCount = { pets.size }
                )

                HorizontalPager(
                    modifier = Modifier.fillMaxSize(),
                    userScrollEnabled = true,
                    state = pagerState,
                ) { page ->
                    val pet = pets[page]
                    Card(modifier = Modifier.fillMaxSize()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(pet.urlImage)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Pet Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No pets available", style = MaterialTheme.typography.bodyLarge)
                }
            }
            Timber.tag("HomeScreen: ${state.value}").d("${state.value}")

        }
    }
    LaunchedEffect(Unit) {
        viewModel.getVoteCats()
    }
}
@Composable
@Preview(showSystemUi = true)
fun HomeTopBar() {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        shadowElevation = 3.dp
    ) {
        Column {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text("Search") },
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {

                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .background(MaterialTheme.colorScheme.secondary)
                ) {
                    Text(
                        "Filter", modifier = Modifier
                            .wrapContentSize()
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Spacer(Modifier.width(8.dp))

                LazyRow(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    item {
                    }
                    items(10) {
                        Box(
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(8.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = MaterialTheme.shapes.small
                                )
                                .background(MaterialTheme.colorScheme.surface)
                                .padding(8.dp)

                        ) {
                            Text("Breed $it")
                        }
                    }
                }
            }

        }
    }
}
@Composable
fun CatItem(cat: Pet) {
    AsyncImage(
        model = cat.urlImage,
        contentDescription = "Cat Image",
        modifier = Modifier.fillMaxSize(),
        contentScale = ContentScale.Fit,
    )
}