package com.rahul.stocker.di

import com.rahul.stocker.data.remote.StockPriceService
import com.rahul.stocker.data.remote.StockPriceServiceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideStockPriceService(): StockPriceService = StockPriceServiceImpl()
}
