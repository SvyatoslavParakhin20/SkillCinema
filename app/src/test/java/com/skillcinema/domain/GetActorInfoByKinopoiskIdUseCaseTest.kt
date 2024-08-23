package com.skillcinema.domain

import com.skillcinema.data.RepositoryImpl
import com.skillcinema.entity.ActorGeneralInfoDto
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock

class GetActorInfoByKinopoiskIdUseCaseTest {
    @Test
    fun `should return equal data from entrance to exit`() = runTest{
        val repositoryImpl = mock<RepositoryImpl>()
        val expected = mock<ActorGeneralInfoDto>()
        Mockito.`when`(repositoryImpl.getActorInfoByKinopoiskId(Mockito.anyInt())).thenReturn(expected)
        val useCase = GetActorInfoByKinopoiskIdUseCase(repository = repositoryImpl)
        val actual = useCase.execute(Mockito.anyInt())
        Assertions.assertEquals(expected, actual)
    }
}