package com.adevelopercompany.threadapp

import android.provider.ContactsContract.DisplayNameSources.EMAIL
import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adevelopercompany.threadapp.db.Subscriber
import com.adevelopercompany.threadapp.db.SubscriberRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.regex.Pattern

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {


    val subscriber = repository.subscriber
    var isUpdateOrdelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber


    @Bindable
    var inputName = MutableLiveData<String>()

    @Bindable
    var inputEmail = MutableLiveData<String>()

    @Bindable
    var saveOrUpdateButtonText = MutableLiveData<String>()

    @Bindable
    var clearAllOrDeleteButtonText = MutableLiveData<String>()

    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage


    init {
        saveOrUpdateButtonText.value = "Save"
        clearAllOrDeleteButtonText.value = "Clear All"
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputName.value = subscriber.name
        inputEmail.value = subscriber.email

        isUpdateOrdelete = true
        subscriberToUpdateOrDelete = subscriber
        saveOrUpdateButtonText.value = "Update"
        clearAllOrDeleteButtonText.value = "Delete"
    }

    fun saveOrUpdate() {
        if (inputName.value == null) {
            statusMessage.value = Event(" Please Enter Name")

        } else if (inputEmail.value == null) {
            statusMessage.value = Event(" Please Enter Email")

        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event(" Please Enter Valid Email")

        } else {
            if (isUpdateOrdelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!

                update(subscriberToUpdateOrDelete)
            } else {
                val name: String = inputName.value!!
                val email: String = inputEmail.value!!
                insert(Subscriber(0, name, email))

                inputName.value = null
                inputEmail.value = null
            }

        }
    }

    fun clearAllOrDelete() {

        if (isUpdateOrdelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()

        }
    }

    fun insert(subscriber: Subscriber): Job = viewModelScope.launch {
        val newRowId: Long = repository.insert(subscriber)
        if (newRowId > -1) {
            statusMessage.value = Event(" Subscriber Inserted Successfully $newRowId")

        } else {
            statusMessage.value = Event(" Error Occurred")

        }

    }

    fun update(subscriber: Subscriber): Job = viewModelScope.launch {
        val NoOfRow: Int = repository.update(subscriber)
        if (NoOfRow > -1) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrdelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"
            statusMessage.value = Event("$NoOfRow Updated Successfully")

        } else {
            statusMessage.value = Event("Error Occurred")

        }


    }

    fun delete(subscriber: Subscriber): Job = viewModelScope.launch {
        val noOfRowsDeleted: Int = repository.delete(subscriber)
        if (noOfRowsDeleted > -1) {
            inputName.value = null
            inputEmail.value = null
            isUpdateOrdelete = false
            saveOrUpdateButtonText.value = "Save"
            clearAllOrDeleteButtonText.value = "Clear All"

            statusMessage.value = Event(" Subscriber Deleted Successfully")

        } else {
            statusMessage.value = Event(" Error Occurred")

        }
    }

    fun clearAll(): Job = viewModelScope.launch {
        val noOfRowsDeleted: Int = repository.deleteAll()
        if (noOfRowsDeleted > -1) {
            statusMessage.value = Event("$noOfRowsDeleted Subscriber Successfully")

        } else {
            statusMessage.value = Event("Error Occurred ")

        }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {
    }

}