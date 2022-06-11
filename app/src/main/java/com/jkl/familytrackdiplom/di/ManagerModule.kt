package com.jkl.familytrack.di

import com.jkl.familytrack.data.remote.RemoteDataManager
import com.jkl.familytrack.data.repository.DataManager
import org.koin.dsl.module.module

val managerModule = module {
    single { DataManager(get())}
    single { RemoteDataManager(get())}
}