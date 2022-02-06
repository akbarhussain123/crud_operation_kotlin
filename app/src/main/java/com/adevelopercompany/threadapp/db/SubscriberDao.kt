package com.adevelopercompany.threadapp.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface SubscriberDao {

    @Insert
    suspend fun insertSubscriber(subscriber: Subscriber): Long

    @Update
    suspend fun updateSubscriber(subscriber: Subscriber):Int

    @Delete
    suspend fun daleteSubscriber(subscriber: Subscriber):Int


    @Query("Delete From subscriber_data_table")
    suspend fun daleteAllSubscriber():Int

    @Query("Select * From subscriber_data_table")
    fun getAllSubscriber(): LiveData<List<Subscriber>>


}