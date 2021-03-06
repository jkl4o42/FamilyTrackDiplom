package com.jkl.familytrack.di

import com.jkl.familytrack.ui.login.LoginViewModel
import com.jkl.familytrack.ui.main.MainViewModel
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel(get()) }
}