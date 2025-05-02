package com.rudiridho.catalogapps.di

import android.content.Context
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import com.rudiridho.catalogapps.data.local.ProductDatabase
import com.rudiridho.catalogapps.data.local.ProductLocalDataSource
import com.rudiridho.catalogapps.data.local.ProductLocalDataSourceImpl
import com.rudiridho.catalogapps.data.remote.ProductRemoteDataSource
import com.rudiridho.catalogapps.data.remote.ProductRemoteDataSourceImpl
import com.rudiridho.catalogapps.data.repository.ProductRepositoryImpl
import com.rudiridho.catalogapps.data.service.ProductApiService
import com.rudiridho.catalogapps.domain.repository.ProductRepository
import com.rudiridho.catalogapps.domain.usecase.GetFavoriteProductsUseCase
import com.rudiridho.catalogapps.domain.usecase.GetProductsUseCase
import com.rudiridho.catalogapps.domain.usecase.SearchProductsUseCase
import com.rudiridho.catalogapps.domain.usecase.UpdateProductUseCase
import com.rudiridho.catalogapps.presentation.viewmodel.ProductViewModel
import com.rudiridho.catalogapps.utils.CoroutinesDispatcherProvider
import com.rudiridho.catalogapps.utils.CoroutinesDispatcherProviderImpl
import com.rudiridho.catalogapps.utils.ProductMapper
import com.rudiridho.catalogapps.utils.ProductMapperImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val appModule = module {

    // Network
    single {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(provideChuckerInterceptor(androidContext()))
            .build()
    }

    single {
        Retrofit.Builder()
            .baseUrl("http://127.0.0.1:3001") // Use 10.0.2.2 for emulator to access localhost
            .addConverterFactory(GsonConverterFactory.create())
            .client(get())
            .build()
            .create(ProductApiService::class.java)
    }

    single<ProductRemoteDataSource> { ProductRemoteDataSourceImpl(get()) }

    // Database
    single {
        Room.databaseBuilder(
            androidContext(),
            ProductDatabase::class.java,
            "product_database"
        ).build()
    }

    single { get<ProductDatabase>().productDao() }

    single<ProductLocalDataSource> { ProductLocalDataSourceImpl(get()) }

    // Mapper
    single<ProductMapper> { ProductMapperImpl(get()) }

    // Dispatcher
    single<CoroutinesDispatcherProvider> { CoroutinesDispatcherProviderImpl() }

    // Repository
    single<ProductRepository> { ProductRepositoryImpl(get(), get(), get()) }

    // Use Cases
    factory { GetProductsUseCase(get()) }
    factory { GetFavoriteProductsUseCase(get()) }
    factory { SearchProductsUseCase(get()) }
    factory { UpdateProductUseCase(get()) }

    // ViewModel
    viewModel { ProductViewModel(get(), get(), get(), get(), get()) }
}

fun provideChuckerInterceptor(context: Context): ChuckerInterceptor {
    val chuckerCollector = ChuckerCollector(
        context = context,
        showNotification = true,
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )
    return ChuckerInterceptor.Builder(context)
        .collector(chuckerCollector)
        .maxContentLength(250_000L)
        .redactHeaders("Auth-Token", "Bearer")
        .alwaysReadResponseBody(true)
        .build()
}