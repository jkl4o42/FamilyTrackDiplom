package com.jkl.familytrack.di

import com.jkl.familytrack.data.remote.firebase.FirebaseOperationSource
import org.koin.dsl.module.module

val remoteModule = module {
    factory { FirebaseOperationSource() }
}