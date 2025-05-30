//package com.dluong.pet.presentation.ui.delete
//
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.Menu
//import androidx.compose.material3.Card
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.compose.ui.unit.dp
//import com.dluong.pet.domain.model.Pet
//import com.dluong.pet.presentation.home.HomeTopBar
//import com.dluong.pet.presentation.home.PetItem
//
//@Preview(showSystemUi = true)
//@Composable
//fun HomeScreenDesignOnly() {
//    val dummyPets = List(5) { index ->
//        Pet(
//            id = "$index",
//            urlImage = "https://placekitten.com/200/300?image=$index",
//            width = 200,
//            height = 300
//        )
//    }
//
//    val pagerState = rememberPagerState(initialPage = 0, pageCount = { dummyPets.size })
//
//    Scaffold(
//        modifier = Modifier.fillMaxSize(),
//        topBar = { HomeTopBar() },
//        floatingActionButton = {
//            Column(
//                modifier = Modifier.padding(bottom = 60.dp, end = 16.dp),
//            ) {
//                IconButton(
//                    onClick = { /* Handle favorite click */ },
//                    modifier = Modifier.padding(8.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Favorite,
//                        contentDescription = "Favorite",
//                        tint = MaterialTheme.colorScheme.onPrimary,
//                        modifier = Modifier.size(60.dp)
//                    )
//                }
//                IconButton(
//                    onClick = { /* Handle another action */ },
//                    modifier = Modifier.padding(8.dp)
//                ) {
//                    Icon(
//                        imageVector = Icons.Default.Menu,
//                        contentDescription = "Another Action",
//                        tint = MaterialTheme.colorScheme.onPrimary,
//                        modifier = Modifier.size(60.dp)
//                    )
//                }
//
//            }
//        }
//    ) { paddingValues ->
//        HorizontalPager(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(paddingValues),
//            state = pagerState
//        ) { page ->
//            val pet = dummyPets[page]
//            Card(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(16.dp)
//            ) {
//                PetItem(pet, )
//            }
//
//        }
//    }
//}
