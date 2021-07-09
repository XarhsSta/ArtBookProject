package com.xarhssta.artbookproject.repository

import androidx.lifecycle.LiveData
import com.xarhssta.artbookproject.model.ImageResponse
import com.xarhssta.artbookproject.roomdb.Art
import com.xarhssta.artbookproject.util.Resource

interface ArtRepositoryInterface {

    suspend fun insertArt(art: Art)

    suspend fun deleteArt(art: Art)

    fun getArt(): LiveData<List<Art>>

    suspend fun searchImage(imageString: String): Resource<ImageResponse>

}