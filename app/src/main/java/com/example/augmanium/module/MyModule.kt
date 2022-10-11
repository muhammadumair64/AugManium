//package com.example.augmanium.module
//
//import android.content.Context
//import com.example.augmanium.utils.TinyDB
//import dagger.Module
//
//import dagger.hilt.android.qualifiers.ApplicationContext
//import javax.inject.Singleton
//import dagger.Provides
//import dagger.hilt.InstallIn
//import dagger.hilt.components.SingletonComponent
//
//@Module
//@InstallIn(SingletonComponent::class)
//class MyModule {
//
//    @Provides
//    @Singleton
//    fun provideTinyDB(@ApplicationContext appContext: Context): TinyDB {
//        return TinyDB(appContext)
//    }
//}