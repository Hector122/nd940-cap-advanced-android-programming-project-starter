package com.example.android.politicalpreparedness.representative


import android.os.Message
import android.text.TextUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android.politicalpreparedness.network.CivicsApi
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.model.Representative
import kotlinx.coroutines.launch

class RepresentativeViewModel : ViewModel() {
    
    //completed: Establish live data for representatives and address
    private val _representatives = MutableLiveData<List<Representative>>()
    val representatives: LiveData<List<Representative>>
        get() = _representatives
    
    private val _address = MutableLiveData<Address>()
    val address: LiveData<Address>
        get() = _address
    
//    init {
//        //Init address for two way binding
//        _address.value = Address("", "", "", "Alabama", "")
//    }
    
    fun setAddress(address: Address) {
        _address.value = address
        getRepresentatives(address.toString())
    }
    
    //completed: Create function to fetch representatives from API from a provided address
    fun getRepresentatives(address: String) {
        viewModelScope.launch {
            val (offices, officials) = CivicsApi.retrofitService.getRepresentativesAsync(address = address)
                .await()
            _representatives.value =
                offices.flatMap { office -> office.getRepresentatives(officials) }
        }
    }
    
    /**
     *  The following code will prove helpful in constructing a representative from the API. This code
     *  combines the two nodes of the RepresentativeResponse into a single official :
    
    val (offices, officials) = getRepresentativesDeferred.await()
    _representatives.value = offices.flatMap { office -> office.getRepresentatives(officials) }
    
    Note: getRepresentatives in the above code represents the method used to fetch data from the API
    Note: _representatives in the above code represents the established mutable live data housing representatives
     
     */
    
    fun setAddressFromGeoLocation(address: Address) {
        _address.value = address
    }
    
    fun areAllAddressFieldsValid(): Boolean {
        val address = _address.value
        
        return !TextUtils.isEmpty(address?.city) && !TextUtils.isEmpty(address?.zip)
                && !TextUtils.isEmpty(address?.state)
    }
    //not need: Create function get address from geo location
    
    // completed: Create function to get address from individual fields
    // implement two way binding in fragment_representative.
}
