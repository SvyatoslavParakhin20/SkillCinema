package com.skillcinema.ui.home

import com.skillcinema.domain.GetAllCollectionsUseCase
import com.skillcinema.domain.GetCollectionFilmIdsUseCase
import com.skillcinema.domain.GetPopularUseCase
import com.skillcinema.domain.GetPremiereUseCase
import com.skillcinema.domain.GetRandomGenreFilmsUseCase
import com.skillcinema.domain.GetSeriesUseCase
import com.skillcinema.domain.GetSharedPrefsUseCase
import com.skillcinema.domain.GetTop250UseCase
import com.skillcinema.domain.InsertCollectionUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.mockito.Mockito
import org.mockito.kotlin.mock

class HomeViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val getCollectionFilmIdsUseCaseTest = mock<GetCollectionFilmIdsUseCase>()
    private val getAllCollectionsUseCaseTest = mock<GetAllCollectionsUseCase>()
    private val insertCollectionUseCaseTest = mock<InsertCollectionUseCase>()
    private val getPremiereUseCaseTest = mock<GetPremiereUseCase>()
    private val getSharedPrefsUseCaseTest = mock<GetSharedPrefsUseCase>()
    private val getPopularUseCaseTest = mock<GetPopularUseCase>()
    private val getSeriesUseCaseTest = mock<GetSeriesUseCase>()
    private val getTop250UseCaseTest = mock<GetTop250UseCase>()
    private val getRandomGenreFilmsUseCaseTest = mock<GetRandomGenreFilmsUseCase>()
    private val homeViewModelTest by lazy {
        HomeViewModel(
            getCollectionFilmIdsUseCaseTest,
            getAllCollectionsUseCaseTest,
            insertCollectionUseCaseTest,
            getPremiereUseCaseTest,
            getSharedPrefsUseCaseTest,
            getPopularUseCaseTest,
            getSeriesUseCaseTest,
            getTop250UseCaseTest,
            getRandomGenreFilmsUseCaseTest
        )
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun resetData() {
        Dispatchers.resetMain()
    }
    @Test
    fun `onboardingShownFlag should be changed by function checkForOnboarding()`() {
        Mockito.`when`(getSharedPrefsUseCaseTest.execute()).thenReturn(3)
        val expected = 3
        homeViewModelTest.checkForOnboarding()
        val actual = homeViewModelTest.onboardingShownFlag
        Assertions.assertEquals(expected, actual)
    }
}
class MainDispatcherRule @OptIn(ExperimentalCoroutinesApi::class) constructor(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}