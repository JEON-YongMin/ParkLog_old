package com.example.parklog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DeviceViewModel : ViewModel() {

    private val _devices = MutableLiveData<MutableList<String>>(mutableListOf())
    val devices: LiveData<MutableList<String>> = _devices

    fun addDevice(deviceName: String) {
        _devices.value?.apply {
            add("$deviceName 🚗")
            _devices.value = this
        }
    }

    fun removeDevice(index: Int) {
        _devices.value?.apply {
            if (index in indices) {
                removeAt(index)
                _devices.value = this
            }
        }
    }

    fun updateDeviceName(index: Int, newName: String) {
        _devices.value?.apply {
            if (index in indices) {
                this[index] = newName
                _devices.value = this
            }
        }
    }
}
