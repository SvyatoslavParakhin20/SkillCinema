package com.skillcinema.di

import android.content.Context
import com.skillcinema.data.Api
import com.skillcinema.data.FakeRepositoryImpl
import com.skillcinema.domain.RepositoryInterface
import com.skillcinema.room.CollectionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
@Module
class FakeRepositoryModule {

    @Provides
    fun provideRepositoryImpl(
        api: Api,
        @ApplicationContext
        context: Context,
        collectionDao: CollectionDao
    ): RepositoryInterface = FakeRepositoryImpl(api,context,collectionDao)

}