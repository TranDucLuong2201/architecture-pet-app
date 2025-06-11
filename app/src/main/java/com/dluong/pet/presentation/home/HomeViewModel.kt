//package com.dluong.pet.presentation.home
//
//import android.content.ContentValues
//import android.content.Intent
//import android.graphics.Bitmap
//import android.graphics.BitmapFactory
//import android.graphics.drawable.BitmapDrawable
//import android.net.Uri
//import android.provider.MediaStore
//import androidx.lifecycle.SavedStateHandle
//import androidx.lifecycle.viewModelScope
//import androidx.room.processor.Context
//import coil.ImageLoader
//import coil.request.ImageRequest
//import coil.request.SuccessResult
//import com.dluong.core.domain.utils.NetworkError
//import com.dluong.core.domain.utils.onError
//import com.dluong.core.domain.utils.onSuccess
//import com.dluong.pet.domain.model.Pet
//import com.dluong.pet.domain.repository.FavoriteCatRepository
//import com.dluong.pet.domain.usecase.GetVoteCatsUseCase
//import com.dluong.pet.domain.usecase.VoteDownPetUseCase
//import com.dluong.pet.domain.usecase.VoteUpPetUseCase
//import com.dluong.pet.presentation.ui.NetworkStatusViewModel
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.channels.Channel
//import kotlinx.coroutines.flow.MutableStateFlow
//import kotlinx.coroutines.flow.StateFlow
//import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.coroutines.flow.combine
//import kotlinx.coroutines.launch
//import timber.log.Timber
//import javax.inject.Inject
//
//@HiltViewModel
//class HomeViewModel @Inject constructor(
//    private val voteUpPetUseCase: VoteUpPetUseCase,
//    private val voteDownPetUseCase: VoteDownPetUseCase,
//    private val getVoteCatsUseCase: GetVoteCatsUseCase,
//    private val savedStateHandle: SavedStateHandle,
//    private val petRepository: FavoriteCatRepository
//) : NetworkStatusViewModel() {
//
//    companion object {
//        private const val SORT_BY = "RANDOM"
//        private const val PAGE_SIZE = 10
//        private const val SAVED_PAGE_KEY = "currentPage"
//        private const val SAVED_PETS_KEY = "currentPets"
//        private const val SAVED_LOADING_STATE_KEY = "isDataLoaded"
//    }
//
//    // Restore saved pets list from SavedStateHandle
//    private val _voteState = MutableStateFlow<VoteUiState>(
//        if (savedStateHandle.get<Boolean>(SAVED_LOADING_STATE_KEY) == true) {
//            val savedPets = savedStateHandle.get<List<Pet>>(SAVED_PETS_KEY) ?: emptyList()
//            if (savedPets.isNotEmpty()) VoteUiState.Success(savedPets) else VoteUiState.Loading
//        } else {
//            VoteUiState.Loading
//        }
//    )
//
//    private val _singleEvent = Channel<VoteSingleEvent>(Channel.UNLIMITED)
//
//    private val _currentPage: StateFlow<Int> = savedStateHandle.getStateFlow(SAVED_PAGE_KEY, 0)
//    val currentPage: StateFlow<Int> = _currentPage
//
//    private val _isLoadingMore = MutableStateFlow(false)
//    val isLoadingMore = _isLoadingMore.asStateFlow()
//
//    private var isLoading = false
//
//    // Initialize currentList from saved state
//    private val currentList = mutableListOf<Pet>().apply {
//        val savedPets = savedStateHandle.get<List<Pet>>(SAVED_PETS_KEY)
//        if (!savedPets.isNullOrEmpty()) {
//            addAll(savedPets)
//        }
//    }
//
//    // Observe favorite pets and sync with current list
//    private val favoritePets = petRepository.observeFavoriteCats()
//
//    // Combine current pets with favorite status
//    val voteStateFlow = combine(_voteState, favoritePets) { voteState, favorites ->
//        when (voteState) {
//            is VoteUiState.Success -> {
//                val favoriteIds = favorites.map { it.id }.toSet()
//                val updatedPets = voteState.data.map { pet ->
//                    pet.copy(isLiked = favoriteIds.contains(pet.id))
//                }
//                VoteUiState.Success(updatedPets)
//            }
//
//            else -> voteState
//        }
//    }
//
//    init {
//        // Start observing favorite pets to keep UI in sync
//        viewModelScope.launch {
//            favoritePets.collect { favorites ->
//                updatePetsLikedStatus(favorites)
//            }
//        }
//    }
//
//    fun updateCurrentPage(index: Int) {
//        savedStateHandle[SAVED_PAGE_KEY] = index
//    }
//
//    fun toggleLikedPets(pet: Pet) {
//        viewModelScope.launch {
//            try {
//                if (pet.isLiked) {
//                    // Unlike: remove from favorites
//                    voteDownPetUseCase(pet).onSuccess {
//                        Timber.d("Successfully removed pet ${pet.id} from favorites")
//                    }.onError { error ->
//                        Timber.e("Failed to remove pet from favorites: $error")
//                        _singleEvent.trySend(VoteSingleEvent.VoteError)
//                    }
//                } else {
//                    // Like: add to favorites
//                    voteUpPetUseCase(pet).onSuccess {
//                        Timber.d("Successfully added pet ${pet.id} to favorites")
//                    }.onError { error ->
//                        Timber.e("Failed to add pet to favorites: $error")
//                        _singleEvent.trySend(VoteSingleEvent.VoteError)
//                    }
//                }
//            } catch (e: Exception) {
//                _singleEvent.trySend(VoteSingleEvent.VoteError)
//                Timber.tag("Exception").d(e)
//            }
//        }
//    }
//
//    private fun updatePetsLikedStatus(favoritePets: List<Pet>) {
//        val favoriteIds = favoritePets.map { it.id }.toSet()
//        val hasChanges = currentList.any { pet ->
//            pet.isLiked != favoriteIds.contains(pet.id)
//        }
//
//        if (hasChanges) {
//            val updatedList = currentList.map { pet ->
//                pet.copy(isLiked = favoriteIds.contains(pet.id))
//            }
//            currentList.clear()
//            currentList.addAll(updatedList)
//
//            val successState = VoteUiState.Success(currentList.toList())
//            _voteState.value = successState
//            savedStateHandle[SAVED_PETS_KEY] = currentList.toList()
//        }
//    }
//
//    fun getVoteCats() {
//        // Don't reload if data already exists
//        if (isLoading || currentList.isNotEmpty()) return
//        isLoading = true
//
//        viewModelScope.launch {
//            _voteState.value = VoteUiState.Loading
//            val result = getVoteCatsUseCase(sortBy = SORT_BY, limit = PAGE_SIZE)
//
//            result
//                .onSuccess { pets ->
//                    currentList.clear()
//                    currentList.addAll(pets)
//
//                    // Save data to SavedStateHandle
//                    val successState = VoteUiState.Success(currentList.toList())
//                    _voteState.value = successState
//                    savedStateHandle[SAVED_PETS_KEY] = currentList.toList()
//                    savedStateHandle[SAVED_LOADING_STATE_KEY] = true
//                }
//                .onError { error ->
//                    _voteState.value = VoteUiState.Error(error)
//                    _singleEvent.trySend(VoteSingleEvent.GetListError(error))
//                }
//
//            isLoading = false
//        }
//    }
//
//    fun loadMoreIfNeeded() {
//        viewModelScope.launch {
//            if (_isLoadingMore.value) return@launch
//            _isLoadingMore.value = true
//
//            val result = getVoteCatsUseCase(sortBy = SORT_BY, limit = PAGE_SIZE)
//            result
//                .onSuccess { newPets ->
//                    currentList.addAll(newPets)
//
//                    // Save updated list to SavedStateHandle
//                    val successState = VoteUiState.Success(currentList.toList())
//                    _voteState.value = successState
//                    savedStateHandle[SAVED_PETS_KEY] = currentList.toList()
//                }
//                .onError { error ->
//                    _singleEvent.trySend(VoteSingleEvent.GetListError(error))
//                }
//
//            _isLoadingMore.value = false
//        }
//    }
//
//    fun isDataLoaded(): Boolean {
//        return currentList.isNotEmpty() || savedStateHandle.get<Boolean>(SAVED_LOADING_STATE_KEY) == true
//    }
//
//    suspend fun loadBitmapFromUrl(context: android.content.Context, imageUrl: String): Bitmap? {
//        return try {
//            val loader = ImageLoader(context)
//            val request = ImageRequest.Builder(context)
//                .data(imageUrl)
//                .allowHardware(false)
//                .build()
//
//            val result = (loader.execute(request) as? SuccessResult)?.drawable
//            (result as? BitmapDrawable)?.bitmap
//        } catch (e: Exception) {
//            e.printStackTrace()
//            null
//        }
//    }
//
//    fun saveImageToMediaStore(
//        context: android.content.Context,
//        bitmap: Bitmap,
//        displayName: String
//    ): Uri? {
//        val contentValues = ContentValues().apply {
//            put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
//            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
//            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/MyApp") // Thư mục trong Pictures
//        }
//
//        val contentResolver = context.contentResolver
//        val uri =
//            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
//
//        uri?.let {
//            contentResolver.openOutputStream(it).use { outputStream ->
//                outputStream?.let { stream ->
//                    bitmap.compress(
//                        Bitmap.CompressFormat.JPEG,
//                        100,
//                        stream
//                    )
//                }
//            }
//        }
//
//        return uri
//    }
//
//    fun shareImage(context: android.content.Context, imageUri: Uri) {
//        val shareIntent = Intent(Intent.ACTION_SEND).apply {
//            type = "image/jpeg"
//            putExtra(Intent.EXTRA_STREAM, imageUri)
//            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        }
//
//        context.startActivity(Intent.createChooser(shareIntent, "Share image via"))
//    }
//
//
//    fun shareImageButton(context: android.content.Context, imageUrl: String) {
//        viewModelScope.launch {
//            val image = loadBitmapFromUrl(context, imageUrl)
//            image?.let {
//                val uri = saveImageToMediaStore(context, it, "shared_pet")
//                uri?.let {
//                    shareImage(context, it)
//                }
//            }
//        }
//    }
//}
//
//sealed interface VoteUiState {
//    data object Loading : VoteUiState
//    data class Success(val data: List<Pet>) : VoteUiState
//    data class Error(val error: NetworkError) : VoteUiState
//}
//
//sealed interface VoteSingleEvent {
//    data object VoteError : VoteSingleEvent
//    data class GetListError(val throwable: NetworkError) : VoteSingleEvent
//}