package com.xarhssta.artbookproject.repository

import androidx.lifecycle.LiveData
import com.xarhssta.artbookproject.api.RetrofitAPI
import com.xarhssta.artbookproject.model.ImageResponse
import com.xarhssta.artbookproject.roomdb.Art
import com.xarhssta.artbookproject.roomdb.ArtDao
import com.xarhssta.artbookproject.util.Resource
import java.lang.Exception
import javax.inject.Inject

class ArtRepository @Inject constructor(
    private val artDao: ArtDao,
    private val retrofitAPI: RetrofitAPI
): ArtRepositoryInterface {
    override suspend fun insertArt(art: Art) {
        artDao.insertArt(art)
    }

    override suspend fun deleteArt(art: Art) {
        artDao.deleteArt(art)
    }

    override fun getArt(): LiveData<List<Art>> {
        return artDao.observeArts()
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
        return try {

            val response = retrofitAPI.imageSearch(imageString)
            if (response.isSuccessful) {
                response.body()?.let {
                    return@let Resource.success(it)
                } ?: Resource.error("Error", null)
            } else {
                Resource.error("Error", null)
            }

        } catch (e: Exception) {
            Resource.error("No data found", null)
        }
    }
}