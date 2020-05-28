package com.example.intheclouds.room

import android.content.Context
import android.graphics.Bitmap
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bumptech.glide.Glide
import com.example.intheclouds.ui.main.MainActivity
import com.example.intheclouds.ui.main.MainActivity.Companion.mainContext
import com.example.intheclouds.util.getSampleByteArrays
import com.example.intheclouds.util.toByteArray
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
            println("DEBUG:  ON DATABASE CREATE")
            INSTANCE?.let { database ->
                scope.launch {
                    withContext(Dispatchers.IO) {
                        println("DEBUG:: populate...")
                        populateDatabase(database.cloudDao())
                    }
                }
            }
        }

        suspend fun populateDatabase(cloudDao: CloudDao) {

            mainContext?.let {

                var samples = getSampleByteArrays(it)

                // create sample clouds
                var cloudOne = CaptionedCloud(
                    url = "",
                    byteArray = samples[0],
                    caption = "Above It All"
                )
                var cloudTwo = CaptionedCloud(
                    url = "",
                    byteArray = samples[1],
                    caption = "\"Graple\""
                )

                // add sample clouds
                println("DEBUG:: inserting sample clouds")
                cloudDao.insert(cloudOne)
                cloudDao.insert(cloudTwo)

                MainActivity.setIsSampleCloudInserted(true)
            }
        }
    }
}