package com.xarhssta.artbookproject.view

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import com.google.common.truth.Truth.assertThat
import com.xarhssta.artbookproject.R
import com.xarhssta.artbookproject.adapter.ImageRecyclerAdapter
import com.xarhssta.artbookproject.getOrAwaitValueTest
import com.xarhssta.artbookproject.launchFragmentInHiltContainer
import com.xarhssta.artbookproject.repository.ArtRepositoryFake
import com.xarhssta.artbookproject.viewmodel.ArtViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito
import javax.inject.Inject

class ImageAPIFragmentTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var fragmentFactory : ArtFragmentFactory

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testSelectImage() {
        val navController = Mockito.mock(NavController::class.java)
        val selectedImageUrl = "atilsamancioglu.com"
        val testViewModel = ArtViewModel(ArtRepositoryFake())

        launchFragmentInHiltContainer<ImageAPIFragment>(
            factory = fragmentFactory,
        ) {
            Navigation.setViewNavController(requireView(),navController)
            imageRecyclerAdapter.images = listOf(selectedImageUrl)
            viewModel = testViewModel
        }

        Espresso.onView(ViewMatchers.withId(R.id.searchResultRecyclerView)).perform(
            RecyclerViewActions.actionOnItemAtPosition<ImageRecyclerAdapter.ImageViewHolder>(
                0,click()
            )

        )

        Mockito.verify(navController).popBackStack()

        assertThat(testViewModel.selectedImageURL.getOrAwaitValueTest()).isEqualTo(selectedImageUrl)

    }

}