package com.skillcinema.data

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.skillcinema.entity.FilmInfo
import com.skillcinema.room.CollectionDao
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class RepositoryImplTest {
    private val apiMock = mock<Api>()
    private val contextMock = mock<Context>()
    private val daoMock = mock<CollectionDao>()
    private val repositoryImpl =
        RepositoryImpl(api = apiMock, context = contextMock, collectionDao = daoMock)
    private val sharedPrefsMock = mock<SharedPreferences>()
    @AfterEach
    fun resetData() {
        Mockito.reset(apiMock)
        Mockito.reset(contextMock)
        Mockito.reset(daoMock)
        Mockito.reset(sharedPrefsMock)
    }
    @Test
    fun `getFilmByKinopoiskId should return the same object as received`() = runTest {
        val filmInfo = mock<FilmInfo>()
        Mockito.`when`(repositoryImpl.getFilmByKinopoiskId(1)).thenReturn(filmInfo)
        val actual = repositoryImpl.getFilmByKinopoiskId(1)
        Assertions.assertEquals(filmInfo, actual)
    }
    @Test
    fun `getDataFromSharedPreference() should return 1 which means data is got from sharedPrefs`() {
        Mockito.`when`(contextMock.getSharedPreferences(Mockito.anyString(), Mockito.anyInt()))
            .thenReturn(
                sharedPrefsMock
            )
        Mockito.`when`(repositoryImpl.getDataFromSharedPreference()).thenReturn(1)
        val actual = repositoryImpl.getOnBoardingFlag()
        val expected = 1
        Assertions.assertEquals(expected, actual)
    }
    @SuppressLint("CheckResult")
    @Test
    fun `saveOnboardingFlag() works`() {
        Mockito.`when`(contextMock.getSharedPreferences(Mockito.anyString(), Mockito.anyInt()))
            .thenReturn(
                sharedPrefsMock
            )
        Mockito.`when`(repositoryImpl.getDataFromSharedPreference()).thenReturn(0)
        val editorMock = mock<SharedPreferences.Editor>()
        Mockito.`when`(sharedPrefsMock.edit()).thenReturn(editorMock)
        val actual = repositoryImpl.saveOnboardingFlag()
        Assertions.assertEquals(true, actual)
    }
}