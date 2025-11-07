package com.example.mcqquiz.module

import android.content.Context
import androidx.room.Room
import com.example.mcqquiz.data.McqDatabase
import com.example.mcqquiz.data.QuizDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DbModule {
    @Provides
    @Singleton
    fun providesMcqDatabase(@ApplicationContext context: Context): McqDatabase {
        return Room.databaseBuilder(context, McqDatabase::class.java, "mcq_db").build()
    }

    @Provides
    @Singleton
    fun providesQuizDao(db: McqDatabase): QuizDao {
        return db.quizDao()
    }
}