package com.example.android.politicalpreparedness.representative

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.*
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.android.politicalpreparedness.BuildConfig
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.databinding.FragmentRepresentativeBinding
import com.example.android.politicalpreparedness.network.models.Address
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter
import com.example.android.politicalpreparedness.representative.adapter.setNewValue
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import java.util.Locale


class DetailFragment : Fragment() {
    
    companion object {
        //completed: Add Constant for Location request
        private const val LOCATION_PERMISSION_INDEX = 0
        const val REQUEST_LOCATION_PERMISSION = 1
    }
    
    //completed: Declare ViewModel
    private val viewModel: RepresentativeViewModel by lazy {
        ViewModelProvider(this).get(RepresentativeViewModel::class.java)
    }
    
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
                             ): View? {
        
        //completed: Establish bindings
        val binding = FragmentRepresentativeBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        
        //completed: Define and assign Representative adapter
        val adapter = RepresentativeListAdapter()
        binding.representativesRecyclerView.adapter = adapter
        
        //completed: Populate Representative adapter
        viewModel.representatives.observe(viewLifecycleOwner, Observer {
            it.let {
                adapter.submitList(it)
            }
        })
    
        // Observe the address so that we can populate the spinner with the state
//        viewModel.address.observe(viewLifecycleOwner, Observer {
//            it?.let {
//                binding.state.setNewValue(it.state)
//            }
//        })
        
        //completed: Establish button listeners for field and location search
        binding.buttonLocation.setOnClickListener {
            getLocation()
        }
        
        binding.buttonSearch.setOnClickListener {
            viewModel.getRepresentatives(viewModel.address.value.toString())
        }
        
        return binding.root
    }
    
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>,
                                            grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        //completed: Handle location permission result to get location on permission granted
        if (grantResults.isNotEmpty()
            && requestCode == REQUEST_LOCATION_PERMISSION
            && grantResults[LOCATION_PERMISSION_INDEX] == PackageManager.PERMISSION_GRANTED) {
                //permission granted
                getLocation()
        } else {
            // permission denied
            showSnackBarExplanation()
        }
    }
    
    
    private fun showSnackBarExplanation() {
        Snackbar.make(requireView(),
                      R.string.permission_denied_explanation,
                      Snackbar.LENGTH_INDEFINITE)
            .setAction(R.string.settings) {
                startActivity(Intent().apply {
                    action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    data = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                })
            }.show()
    }
    
    private fun checkLocationPermissions(): Boolean {
        return if (isPermissionGranted()) {
            true
        } else {
            //completed: Request Location permissions
            requestPermissions()
            false
        }
    }
    
    private fun isPermissionGranted(): Boolean {
        //completed: Check if permission is already granted and return (true = granted, false = denied/other)
        return ContextCompat.checkSelfPermission(
            requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
    
    @SuppressLint("MissingPermission")
    private fun getLocation() {
        //completed: Get location from LocationServices
        if (checkLocationPermissions()) {
            LocationServices.getFusedLocationProviderClient(requireContext())
                .lastLocation.addOnSuccessListener { location ->
                    //completed: The geoCodeLocation method is a helper function to change the lat/long location to a human readable street address
                    viewModel.setAddressFromGeoLocation(geoCodeLocation(location))
                    viewModel.getRepresentatives(viewModel.address.value.toString())
                }
        }
    }
    
    private fun requestPermissions(){
        requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
    }
    
    private fun geoCodeLocation(location: Location): Address {
        val geocoder = Geocoder(context, Locale.getDefault())
        return geocoder.getFromLocation(location.latitude, location.longitude, 1)
            .map { address ->
                Address(address.thoroughfare, address.subThoroughfare, address.locality, address.adminArea, address.postalCode)
            }
            .first()
    }
    
    private fun hideKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view!!.windowToken, 0)
    }
    
}