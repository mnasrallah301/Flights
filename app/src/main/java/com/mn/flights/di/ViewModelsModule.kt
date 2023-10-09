package com.mn.flights.di

import com.mn.flights.data.repositories.SearchRepositoryImpl
import com.mn.flights.presentation.home.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

//By screen
private val HomeViewModelModule = module {
    viewModel { MainViewModel(get()) }
}

val viewModelsModule = listOf<Module>(
    HomeViewModelModule
)