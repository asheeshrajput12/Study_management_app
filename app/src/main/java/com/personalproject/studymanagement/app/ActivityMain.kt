package com.personalproject.studymanagement.app

import android.Manifest
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.tabs.TabLayout
import com.personalproject.studymanagement.R
import com.personalproject.studymanagement.common.CommonFunctions
import com.personalproject.studymanagement.databinding.ActivityMainBinding
import com.personalproject.studymanagement.fragment.FragmentMyTask
import timber.log.Timber


class ActivityMain : AppCompatActivity(),View.OnClickListener {
    private lateinit var activityMainBinding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.statusBarColor=ContextCompat.getColor(this,R.color.black)
        activityMainBinding=ActivityMainBinding.inflate(layoutInflater)
        activityMainBinding.tvAddProjects.setOnClickListener(this)
        activityMainBinding.tvAddTasks.setOnClickListener(this)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        getUserLastLocation()
        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorSecondary)
        }

        // Set dark text/icons if the status bar color is light
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
//        enableEdgeToEdge()
        setContentView(activityMainBinding.root)
        toggle = ActionBarDrawerToggle(
            this,
            activityMainBinding.drawerLayout,
            activityMainBinding.toolbarMain,
            R.string.app_name,
            R.string.app_name
        )
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, FragmentMyTask())
            .commit()
        activityMainBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
      //  DbUtils.createProjectTable(this)
        // Create ContentValues to insert a new project
        val values = ContentValues().apply {
            put("txt_name", "Project 1")
            put("description", "Description of Project 1")
            put("txt_created_date", "2024-07-20")
            put("txt_status", "Active")
            put("txt_priority", "High")
            put("txt_type", "Development")
            put("txt_due_date", "2024-12-31")
        }
        CommonFunctions.insertData(this,"tbl_project",values)
        /*DbUtils.insertProjectData(this, values)
        val provider:MyContentProvider =MyContentProvider()*/
        // Set up TabLayout with tabs
        activityMainBinding.tabLayout.addTab(activityMainBinding.tabLayout.newTab().setText("Home").setIcon(R.drawable.ic_home))
        activityMainBinding.tabLayout.addTab(activityMainBinding.tabLayout.newTab().setText("Profile").setIcon(R.drawable.ic_person))
        activityMainBinding.tabLayout.addTab(activityMainBinding.tabLayout.newTab().setText("Settings").setIcon(R.drawable.ic_setting))
        activityMainBinding.tabLayout.addTab(activityMainBinding.tabLayout.newTab().setText("Notes").setIcon(R.drawable.ic_note_add))

        // Handle tab selection
        activityMainBinding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                activityMainBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this@ActivityMain, android.R.color.white))
                activityMainBinding.tabLayout.setTabIconTintResource(R.color.white)
                activityMainBinding.tabLayout.setTabTextColors(
                    ContextCompat.getColor(this@ActivityMain, R.color.black),  // Normal text color
                    ContextCompat.getColor(this@ActivityMain, android.R.color.white)        // Selected text color
                )
               /* val selectedFragment: Fragment = when (tab.position) {
                    0 -> {

                        HomeFragment()
                    }
                    1 -> {
                        ProfileFragment()
                    }
                    2 -> {
                        SettingsFragment()
                    }
                    else -> HomeFragment()
                }*/

//                supportFragmentManager.beginTransaction()
//                    .replace(R.id.fragment_container, selectedFragment)
//                    .commit()
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // Optionally handle tab unselection
                activityMainBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this@ActivityMain, android.R.color.black))
                activityMainBinding.tabLayout.setTabIconTintResource(R.color.black)
                activityMainBinding.tabLayout.setTabTextColors(
                    ContextCompat.getColor(this@ActivityMain, R.color.black),  // Normal text color
                    ContextCompat.getColor(this@ActivityMain, android.R.color.white)        // Selected text color
                )

            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // Optionally handle tab reselection
                activityMainBinding.tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this@ActivityMain, android.R.color.black))
                activityMainBinding.tabLayout.setTabIconTintResource(R.color.black)
                activityMainBinding.tabLayout.setTabTextColors(
                    ContextCompat.getColor(this@ActivityMain, R.color.black),  // Normal text color
                    ContextCompat.getColor(this@ActivityMain, android.R.color.white)        // Selected text color
                )
            }
        })

    }

    private fun getUserLastLocation() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this, arrayOf<String>(
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION), 112
                )
            }else{
                // work here for location
                if(isLocationEnabled()){


                    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                        if (location != null) {
                            // Get latitude and longitude
                            val latitude = location.latitude
                            val longitude = location.longitude

                            // Use the location
                            Timber.tag("Location")
                                .d("Latitude: " + latitude + ", Longitude: " + longitude)
                            // Show the dialog asking the user if they want to open the map
                           // showLocationDialog(latitude, longitude)
                            requestLocationUpdates()
                        } else {
                            // Handle the case when location is null
                            Timber.tag("Location").e("Location is null")
                        }
                    }.addOnFailureListener { exception ->
                        // Handle the failure to retrieve location
                        Timber.tag("Location").e(exception, "Failed to get location")
                    }

                }else{
                    Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG)
                        .show()
                    val intent: Intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }


    // method to check
    // if location is enabled
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        if (requestCode == 112) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getUserLastLocation()
            } else {
                // Handle permission denied
                Toast.makeText(this@ActivityMain,"Not permission",Toast.LENGTH_SHORT).show()

            }
        }
    }

    override fun onResume() {
        super.onResume()
        /*if(isLocationEnabled()){
            getUserLastLocation()
        }*/
    }


    // Optional: Request location updates for more accurate or continuous results
    fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // 10 seconds interval
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(p0: LocationResult) {
                        p0 ?: return
                        var latitude = 0.0
                        var longitude =0.0
                        for (location in p0.locations) {
                            // Get latitude and longitude
                            latitude = location.latitude
                            longitude = location.longitude
                            // Show the dialog asking the user if they want to open the map
                            Log.d("Location Update", "Latitude: $latitude, Longitude: $longitude")
                        }
                        showLocationDialog(latitude, longitude)
                    }
                },
                Looper.getMainLooper()
            )
        }
    }
    // Function to show AlertDialog and ask the user if they want to open Google Maps
    fun showLocationDialog(latitude: Double, longitude: Double) {
        // Build the AlertDialog
        AlertDialog.Builder(this)
            .setTitle("Open Map")
            .setMessage("Do you want to pin your current location on Google Maps?")
            .setPositiveButton("Yes") { dialog, _ ->
                // If user says "Yes", open Google Maps with the pinned location
                openMapWithLocation(latitude, longitude)
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                // If user says "No", just dismiss the dialog
                dialog.dismiss()
            }
            .create()
            .show()
    }

    // Function to open Google Maps with the location pinned
    fun openMapWithLocation(latitude: Double, longitude: Double) {
        val uri = "geo:$latitude,$longitude?q=$latitude,$longitude"
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))

        // Optional: Restrict to open Google Maps specifically
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
        // Check if a map app is available and then launch it
       /* if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "No map application found.", Toast.LENGTH_SHORT).show()
        }*/
    }

    override fun onClick(v: View?) {
        try {
            when(v?.id){
                R.id.tvAddProjects->{
                    startActivity(Intent(this,ActivityAddProject::class.java))
                }
                R.id.tvAddTasks->{
                    startActivity(Intent(this,ActivityAddTask::class.java))
                }
            }

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

}