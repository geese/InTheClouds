package com.example.intheclouds.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [(CaptionedCloud::class)], version = 1)
abstract class CloudsDatabase : RoomDatabase() {
    abstract fun cloudDao(): CloudDao

    // Singleton example from Google Samples -
    // https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/persistence/UsersDatabase.kt
    // also Google Codelab "Room with a View"

    companion object {
        @Volatile
        private var INSTANCE: CloudsDatabase? = null

        fun getInstance(
            context: Context,
            scope: CoroutineScope
        ): CloudsDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): CloudsDatabase =
            Room.databaseBuilder(
                context.applicationContext, CloudsDatabase::class.java, "app-database"
            ).build()
    }

    private class CloudsDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.cloudDao())
                }
            }
        }

        suspend fun populateDatabase(cloudDao: CloudDao) {
            // first delete any content
            cloudDao.deleteAll()

            // add sample clouds
            var cloud = CaptionedCloud(
                url = "https://pixabay.com/get/54e8d145425ab10ff3d8992cc62e3177143fd7e64e507440752e7cd3934cc4_640.jpg",
                caption = "Sample Cloud One"
            )
        }

    }
}