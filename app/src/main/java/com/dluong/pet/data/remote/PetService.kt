package com.dluong.pet.data.remote

import com.dluong.pet.data.remote.dto.PetDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PetService {
    @GET("images/search")
    suspend fun getVoteList(
        @Query("size") size: String? = null,
        @Query("mime_types") mimeTypes: List<String>? = null,
        @Query("order") order: String? = null,
        @Query("limit") limit: Int? = null,
        @Query("page") page: Int? = null,
        @Query("category_ids") categoryIds: List<Int>? = null,
        @Query("breed_id") breedId: String? = null,
    ): Response<List<PetDto>>
}