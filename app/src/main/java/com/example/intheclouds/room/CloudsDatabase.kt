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

            //https://stackoverflow.com/questions/45750020/getting-bitmap-from-image-using-glide-in-android

            var bitmapOne: Bitmap? = mainContext?.run {
                Glide.with(this)
                    .asBitmap()
                    .load("https://pixabay.com/get/5fe1dd454f56b10ff3d8992cc62e3177143fd7e64e507440752a78d79e4ec7_640.jpg")
                    .submit()
                    .get()
            }

            var bitmapTwo: Bitmap? = mainContext?.run {
                Glide.with(this)
                    .asBitmap()
                    .load("https://pixabay.com/get/54e8d4464a50ac14f1dc8460962931781c3edeec504c704c7c2f7ed09749c15d_640.jpg")
                    .submit()
                    .get()
            }

            // create sample clouds
            var cloudOne = CaptionedCloud(
                url = "https://pixabay.com/get/5fe1dd454f56b10ff3d8992cc62e3177143fd7e64e507440752a78d79e4ec7_640.jpg",
                byteArray = bitmapOne?.toByteArray(),
                caption = "Above It All"
            )
            var cloudTwo = CaptionedCloud(
                url = "https://pixabay.com/get/54e8d4464a50ac14f1dc8460962931781c3edeec504c704c7c2f7ed09749c15d_640.jpg",
                byteArray = bitmapTwo?.toByteArray(),
                caption = "\"Graple\""
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
            //cloudDao.deleteAll()

            // add sample clouds
            println("DEBUG:: inserting a sample cloud")
            cloudDao.insert(cloudOne)
            cloudDao.insert(cloudTwo)
            MainActivity.setIsSampleCloudInserted(true)
            /*cloudDao.insert(cloudTwo)
            cloudDao.insert(cloudThree)
            cloudDao.insert(cloudFour)*/
        }
    }
}