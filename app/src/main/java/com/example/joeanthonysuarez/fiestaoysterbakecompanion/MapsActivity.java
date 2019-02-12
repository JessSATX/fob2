/*
 * Copyright (C) 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.util.TimerTask;

/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * requires forwarding all the important lifecycle methods onto MapView.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    Button refreshButton;
    public static String day;
    private GoogleMap mMap;
    private static final String TAG = MapsActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        addListenerOnButton();

        if (bundle != null) {
            day = (String) bundle.get("day");
        }

    }


    public void addListenerOnButton() {

        refreshButton = (Button) findViewById(R.id.button3);

        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Do things in here if you want to utilize that sick refresh byuton
                System.out.println("Map Refresh Button Tapped");
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyles));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
        LatLng stmu = new LatLng(29.45249260178782, -98.56478047528071);
        mMap.setInfoWindowAdapter(new newinfoAdapter(MapsActivity.this));
        mMap.addMarker(new MarkerOptions()
                .position(stmu)
                .title("Fiesta Oyster Bake")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.food))
                .snippet("Example Booth\n" +
                        "Product: Chicken on a stick\n" +
                        "Price: 6 tickets\n" +
                        "Status: Open"));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(stmu));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Move the camera instantly to stmu with a zoom of 15

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stmu, 15));

        if (day.equals("1")) {
            GroundOverlayOptions fridayMap = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.fobcafri))
                    .position(stmu, 725f, 700f);

            // Add an overlay to the map, retaining a handle to the GroundOverlay object.
            GroundOverlay imageOverlay = mMap.addGroundOverlay(fridayMap);

            mMap.setMinZoomPreference(16);
            // Create a LatLngBounds that includes STMU.
            LatLngBounds STMU = new LatLngBounds(
                    new LatLng(29.449547605603758, -98.5687959744298), new LatLng(29.454892949715997, -98.56029317457967));
            // Constrain the camera target to STMU
            mMap.setLatLngBoundsForCameraTarget(STMU);

        } else {
            GroundOverlayOptions satMap = new GroundOverlayOptions()
                    .image(BitmapDescriptorFactory.fromResource(R.drawable.fobcasatur))
                    .position(stmu, 725f, 700f);
            // Add an overlay to the map, retaining a handle to the GroundOverlay object.
            GroundOverlay imageOverlay = mMap.addGroundOverlay(satMap);
        }
        mMap.setMinZoomPreference(16);
        // Create a LatLngBounds that includes STMU
        LatLngBounds STMU = new LatLngBounds(
                new LatLng(29.449547605603758, -98.5687959744298), new LatLng(29.454892949715997, -98.56029317457967));
        // Constrain the camera target to STMU
        mMap.setLatLngBoundsForCameraTarget(STMU);
    }
//This is gonna be pseudocode for the txt decoder
    /*
    {
        While (


        }
        */
}




