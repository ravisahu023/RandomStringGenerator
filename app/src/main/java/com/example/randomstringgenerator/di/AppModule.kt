package com.example.randomstringgenerator.di

import android.content.Context
import com.example.randomstringgenerator.data.repository.RandomStringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideRandomStringRepository(
        @ApplicationContext context: Context
    ): RandomStringRepository {
        return RandomStringRepository(context)
    }
}
