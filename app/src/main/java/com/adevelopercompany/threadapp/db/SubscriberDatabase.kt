package com.adevelopercompany.threadapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Subscriber::class], version = 1)
abstract class SubscriberDatabase : RoomDatabase() {


    abstract val subscriberDao: SubscriberDao


    companion object {

        private val INSTANCE: SubscriberDatabase? = null

        fun getInstance(context: Context): SubscriberDatabase {

            synchronized(this) {
                var instance: SubscriberDatabase? = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        SubscriberDatabase::class.java,
                        "subscriber_data_databse"
                    ).build()

                }
                return instance
            }


        }

    }


}