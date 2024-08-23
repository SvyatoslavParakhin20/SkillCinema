package com.skillcinema.ui.profile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.skillcinema.domain.GetFullCollectionsUseCase
import com.skillcinema.domain.InsertCollectionUseCase
import com.skillcinema.room.Collection
import com.skillcinema.room.CollectionWIthFilms
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.rules.TestRule
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.verifyNoMoreInteractions

@OptIn(ExperimentalCoroutinesApi::class)
class ProfileViewModelTest {
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    @get:Rule
    val mainDispatcherRule: TestRule = InstantTaskExecutorRule()
    private lateinit var getFullCollectionsUseCaseMock: GetFullCollectionsUseCase
    private lateinit var insertCollectionUseCase: InsertCollectionUseCase
    private lateinit var viewModel: ProfileViewModel
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        getFullCollectionsUseCaseMock = mock()
        insertCollectionUseCase = mock()
        viewModel = ProfileViewModel(
            getFullCollectionsUseCaseMock,
            mock(),
            insertCollectionUseCase = insertCollectionUseCase,
            mock()
        )
        viewModel.createCollection("collection")
    }
    @AfterEach
    fun resetData() {
        Dispatchers.resetMain()
        Mockito.reset(getFullCollectionsUseCaseMock)
        Mockito.reset(insertCollectionUseCase)
    }
    @Test
    fun `function load all should return data class by state`() = runTest {
        val collectionMock = CollectionWIthFilms(
            collection = mockCollection,
            films = emptyList()
        )
        Mockito.`when`(getFullCollectionsUseCaseMock.execute()).thenReturn(listOf(collectionMock))
        viewModel.getCollectionsList()
        verify(insertCollectionUseCase).execute(mockCollection)
        verifyNoMoreInteractions(insertCollectionUseCase)
        val actual = viewModel.collections.value
        Assertions.assertEquals(listOf(collectionMock), actual)
    }
    companion object {
        val mockCollection = Collection("collection")
    }
}