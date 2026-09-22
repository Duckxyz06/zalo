package com.duckxyz.zgm.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        GroupEntity::class,
        GroupTaskEntity::class,
        ZaloConversationSignalEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class ZgmDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun taskDao(): TaskDao
    abstract fun zaloSignalDao(): ZaloSignalDao

    companion object {
        @Volatile
        private var instance: ZgmDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS zalo_conversation_signals (
                        conversationKey TEXT NOT NULL PRIMARY KEY,
                        conversationTitle TEXT NOT NULL,
                        lastMessage TEXT NOT NULL,
                        lastPostedAt INTEGER NOT NULL,
                        unreadEstimate INTEGER NOT NULL,
                        notificationKey TEXT,
                        notificationActive INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getInstance(context: Context): ZgmDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    ZgmDatabase::class.java,
                    "zgm.db"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                    .also { instance = it }
            }
    }
}
