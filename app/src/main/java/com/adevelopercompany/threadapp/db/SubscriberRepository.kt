package com.adevelopercompany.threadapp.db

class SubscriberRepository(private val dao: SubscriberDao) {


    val subscriber = dao.getAllSubscriber()


    suspend fun insert(subscriber: Subscriber) :Long{
       return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber):Int {
      return  dao.updateSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber):Int {
      return  dao.daleteSubscriber(subscriber)
    }

    suspend fun deleteAll():Int {
     return   dao.daleteAllSubscriber()
    }
}