package com.duckxyz.zgm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [GroupEntity::class, GroupTaskEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ZgmDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var instance: ZgmDatabase? = null

        fun getInstance(context: Context): ZgmDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ZgmDatabase::class.java,
                    "zgm.db"
                ).build().also { instance = it }
            }
    }
}
