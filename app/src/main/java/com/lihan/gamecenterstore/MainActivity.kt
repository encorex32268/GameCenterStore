package com.lihan.gamecenterstore

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

import com.lihan.gamecenterstore.databinding.ActivityMainBinding
import com.lihan.gamecenterstore.model.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.EasyPermissions


@AndroidEntryPoint
class MainActivity : AppCompatActivity() , EasyPermissions.PermissionCallbacks{

    private lateinit var binding  : ActivityMainBinding
    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val viewModel: MainViewModel by viewModels()


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        binding.root.isVisible = false
        if(hasLocationPermission()){
            binding.root.isVisible = true
            showMap()
        }else{
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            this,
            "Need Location",
            101,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun showMap() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync {
            mMap = it
            mMap.apply {
                fusedLocationProviderClient.lastLocation.addOnSuccessListener { location->
                    if (location == null){
                        val request = LocationRequest.create().apply {
                            interval = 5000
                            fastestInterval = 3000
                            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                        }
                        fusedLocationProviderClient.requestLocationUpdates(
                            request,
                            object : LocationCallback(){
                                override fun onLocationResult(locationResult: LocationResult) {
                                    super.onLocationResult(locationResult)
                                    val latLng = LatLng(locationResult.lastLocation.latitude,locationResult.lastLocation.longitude)
                                    moveCamera(latLng)
                                    showStoreMark()
                                    fusedLocationProviderClient.removeLocationUpdates(this)

                                }
                            },
                            Looper.getMainLooper()
                        )

                    }else{
                        moveCamera(LatLng(location.latitude,location.longitude))
                        showStoreMark()
                    }
                }

                uiSettings.apply {
                    isZoomControlsEnabled = true
                    isMapToolbarEnabled = true
                    isMyLocationButtonEnabled = true
                    isMyLocationEnabled = true

                }
            }

        }
    }
    private fun moveCamera(latLng: LatLng){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
    }

    private fun showStoreMark(){

        lifecycleScope.launch {
            viewModel.dataStatus.collect {
                when(it){
                    is MainViewModel.Resource.Success->{
                        it.data.forEach { gameStore ->
                            val latLng = LatLng(gameStore.lat,gameStore.lng)
                            mMap.addMarker(
                                MarkerOptions()
                                    .title(gameStore.name)
                                    .position(latLng)
                            )
                        }
                    }
                    is MainViewModel.Resource.Fail->{
                        Toast.makeText(this@MainActivity,"Fail",Toast.LENGTH_SHORT).show()
                    }
                    is MainViewModel.Resource.Loading->{}

                }


            }


        }



    }


    private fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        binding.root.isVisible = true
        showMap()
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        requestLocationPermission()
    }

}