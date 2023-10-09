package com.mn.flights.di

import com.mn.flights.data.repositories.SearchRepositoryImpl
import com.mn.flights.domain.repositories.SearchRepository
import org.koin.core.module.Module
import org.koin.dsl.module

//By screen
private val homeRepositoryModule = module {
    single<SearchRepository> { SearchRepositoryImpl(get(), get(), get()) }
}

val repositoriesModule = listOf<Module>(
    homeRepositoryModule
)
