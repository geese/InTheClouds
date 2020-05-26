package com.example.intheclouds.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Database(entities = [(CaptionedCloud::class)], version = 1)
abstract class CloudsDatabase : RoomDatabase() {
    abstract fun cloudDao(): CloudDao

    // Singleton example from Google Samples -
    // https://github.com/googlesamples/android-architecture-components/blob/master/BasicRxJavaSampleKotlin/app/src/main/java/com/example/android/observability/persistence/UsersDatabase.kt
    // also Google Codelab "Room with a View"

    companion object {
        @Volatile
        private var INSTANCE: CloudsDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): CloudsDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CloudsDatabase::class.java,
                    "clouds-database"
                )
                    .addCallback(CloudsDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class CloudsDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        populateDatabase(database.cloudDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(cloudDao: CloudDao) {

            // create sample clouds
            var cloudOne = CaptionedCloud(
                url = "https://pixabay.com/get/5ee7d4444b5ab10ff3d8992cc62e3177143fd7e64e507440752b79dd944bc6_640.jpg",
                caption = "Sample Cloud One"
            )
            var cloudTwo = CaptionedCloud(
                url = "https://pixabay.com/get/54e9d5454f50a914f1dc8460962931781c3edeec504c704c7c2f7fd69e4ec35b_640.jpg",
                caption = "Sample Cloud Two"
            )
            var cloudThree = CaptionedCloud(
                url = "https://pixabay.com/get/54e0dc424253a514f1dc8460962931781c3edeec504c704c7c2f7fd69e4ec35b_640.jpg",
                caption = "Sample Cloud Three"
            )
            var cloudFour = CaptionedCloud(
                url = "https://pixabay.com/get/54e0dd464b53a814f1dc8460962931781c3edeec504c704c7c2f7fd69e4ec35b_640.jpg",
                caption = "Sample Cloud Four"
            )

            // first delete any content
            cloudDao.deleteAll()

            // add sample clouds
            /*cloudDao.insert(cloudOne)
            cloudDao.insert(cloudTwo)
            cloudDao.insert(cloudThree)
            cloudDao.insert(cloudFour)*/
        }
    }
}