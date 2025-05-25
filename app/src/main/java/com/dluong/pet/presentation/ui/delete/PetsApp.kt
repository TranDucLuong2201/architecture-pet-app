package com.dluong.pet.presentation.ui.delete//package com.dluong.pet.presentation
//
//import android.annotation.SuppressLint
//import android.widget.Toast
//import androidx.compose.material3.Scaffold
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.ui.platform.LocalContext
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import com.dluong.pet.presentation.favorite.FavoriteScreen
//import com.dluong.pet.presentation.home.HomeScreen
//import com.dluong.pet.presentation.search.SearchScreen
//import com.dluong.pet.presentation.ui.ext.MainDestinations
//import com.dluong.pet.presentation.ui.ext.rememberPetsNavController
//
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
//@Composable
//fun PetApp(
//) {
//    val viewModel: BaseViewModel = hiltViewModel()
//    val context = LocalContext.current
//    // Observe network connection
//    // Gửi Toast mỗi khi thay đổi trạng thái mạng
//    LaunchedEffect(true) {
//        viewModel.events.collect {
//            Toast.makeText(context, "Event: $it", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//
//}