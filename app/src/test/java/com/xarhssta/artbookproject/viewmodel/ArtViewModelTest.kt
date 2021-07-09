package com.xarhssta.artbookproject.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.xarhssta.artbookproject.MainCoroutineRule
import com.xarhssta.artbookproject.getOrAwaitValueTest
import com.xarhssta.artbookproject.repository.ArtRepositoryFake
import com.xarhssta.artbookproject.util.Status
import kotlinx.coroutines.ExperimentalCoroutinesApi
import net.bytebuddy.pool.TypePool
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ArtViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: ArtViewModel

    @Before
    fun setup(){
        viewModel = ArtViewModel(ArtRepositoryFake())
    }

    @Test
    fun `insert art without year returns error`(){
        viewModel.checkArt("Mona Lisa", "Da Vinci","")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without name returns error`(){
        viewModel.checkArt("", "DaVinci","1600")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

    @Test
    fun `insert art without artistName returns error`(){
        viewModel.checkArt("Mona Lisa", "","1600")
        val value = viewModel.insertArtMessage.getOrAwaitValueTest()
        assertThat(value.status).isEqualTo(Status.ERROR)
    }

}