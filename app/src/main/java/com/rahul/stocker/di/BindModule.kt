package com.rahul.stocker.di

import com.rahul.stocker.data.repository.StocksPriceRepositoryImpl
import com.rahul.stocker.domain.repository.StocksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class BindModule {
    @Binds
    @Singleton
    abstract fun bindRepository(impl: StocksPriceRepositoryImpl): StocksRepository
}
