package com.adevelopercompany.threadapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.adevelopercompany.threadapp.databinding.ActivityMainBinding
import com.adevelopercompany.threadapp.db.Subscriber
import com.adevelopercompany.threadapp.db.SubscriberDao
import com.adevelopercompany.threadapp.db.SubscriberDatabase
import com.adevelopercompany.threadapp.db.SubscriberRepository

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)


        val dao: SubscriberDao = SubscriberDatabase.getInstance(application).subscriberDao
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)


        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this

        displayRecyclerView()


        subscriberViewModel.message.observe(this, Observer {


            it.getContentIfNotHandled()?.let {

                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun displayRecyclerView() {
        binding.subscriberRecyclerView.layoutManager = LinearLayoutManager(this)
        displaySubscriberList()

    }

    fun displaySubscriberList() {
        subscriberViewModel.subscriber.observe(this, Observer {
            binding.subscriberRecyclerView.adapter = MyRecyclerViewAdapter(it,
                { selectedItem: Subscriber -> listItemClick(selectedItem) })
            Log.i("myLog", it.toString())

        })
    }


    private fun listItemClick(subscriber: Subscriber) {
        subscriberViewModel.initUpdateAndDelete(subscriber)

    }
}