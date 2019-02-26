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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Date;
import java.util.TimerTask;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * requires forwarding all the important lifecycle methods onto MapView.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

        Button refreshButton;
        public static String day;
        private GoogleMap mMap;
        private ClusterManager<MyItem> mCM;
    private static final String TAG = MapsActivity.class.getSimpleName();

        //ArrayList of Marker class, for collecting the references to marker objects. -- Lynntonio
        List<Marker> markers = new ArrayList<Marker>();





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


            ImageButton fbutton = findViewById(R.id.filterbutton);
            // This is the basis to the whole Filter System. ITEMS WILL NOT BE FILTERED IF THEY ARE NOT TAGGED
            // PROPERLY AND IF THEY ARE NOT STORED IN THE LIST "markers"! -- Lynntonio
            fbutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alertFilter = new AlertDialog.Builder(MapsActivity.this);

                    //String array for Alert Dialogue multichoice items. MARKER TAGS MUST MATCH ONE OF THE CONTENTS OF THE ARRAY! -- Lynntonio
                    final String[]  filterArray = new String[] {"Chicken", "Beef", "Seafood", "Veggies", "Sweets", "Beverages", "Alcoholic Beverages", "Other"};
                    //Boolean Array for selected items. Index contents are meant to correspond with those "filterArray". -- Lynntonio
                    final boolean[] filterB = new boolean[]    {     true,   true,      true,     true,     true,        true,                  true,     true};

                    alertFilter.setTitle("Select Categories to Filter");
                    alertFilter.setMultiChoiceItems(filterArray, filterB, new DialogInterface.OnMultiChoiceClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            filterB[which] = isChecked;
                        }
                    });

                    //set positive/Ok button click listener
                    alertFilter.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // this will check every marker in the markers list and set their visibility
                            // to match the corresponding filterB's value based on whether the marker's tag
                            // matches filterArray's current index location. -- Lynntonio
                            for (int i = 0; i < markers.size(); i++)
                            {
                                for (int x = 0; x < filterArray.length; x++)
                                {
                                    if (markers.get(i).getTag() == filterArray[x])
                                    {
                                            markers.get(i).setVisible(filterB[x]);
                                    }
                                }
                            }
                        }
                    });

                    // Set neutral/Cancel button click listener
                    alertFilter.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

                    AlertDialog dialog = alertFilter.create();
                    //show dialog
                    dialog.show();


                }
            });
        }


    public void addListenerOnButton() {

        refreshButton = (Button) findViewById(R.id.button3);

        refreshButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //Do
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
            mMap.setInfoWindowAdapter(new newinfoAdapter(MapsActivity.this));

            // Most of this is for testing and example purposes, This should be commented out for the final build. -- Lynntonio
            LatLng stmu = new LatLng(29.45249260178782, -98.56478047528071);
            LatLng c1 = new LatLng(29.45088651549366, -98.56610764311034);
            LatLng c2 = new LatLng(29.450446486121635, -98.56594992974414);
            LatLng c3 = new LatLng(29.450868764791878, -98.56581152815187);
            LatLng c4 = new LatLng(29.45087717295755, -98.56593812861382);
            Marker test1 = mMap.addMarker(new MarkerOptions()
                    .position(stmu)
                    .title("Fiesta Oyster Bake")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.food))
                    .snippet("Green Section\n" +
                            "Product: N/A\n" +
                            "Price: N/A\n" +
                            "Status: Open"));
            test1.setTag("Other");
            markers.add(test1); // this is one way to store marker references, but requires you to manually initiallize the process. Don't do this.
            markers.add(mMap.addMarker(new MarkerOptions() // thi is probably the most Ideal way to store markers.
                    .position(c1)
                    .title("Green Section")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.sec1_green_flag))
                    .snippet("This section is located below the carnival and hosts the Rock Stage.")));
            markers.get(1).setTag("Other");
            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(c2)
                    .title("Alcohol")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.drink_section1_multi_booth))
                    .snippet("Alcohol Booth\n" +
                            "Product: Alcohol\n" +
                            "Price: 16 tickets\n" +
                            "Status: Open")));
            markers.get(2).setTag("Alcoholic Beverages");
            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(c3)
                    .title("ATM")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.chicken_on_a_stick_all_sec))
                    .snippet("ATM - Green Section")));
            markers.get(3).setTag("Other");
            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(c4)
                    .title("Coupon Booth")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.coupon_booth))
                    .snippet("Here you can purchase coupons to use at booths in Oyster Bake." +
                            "Price: 50 cents per ticket\n")));
            markers.get(4).setTag("Other");

            mMap.moveCamera(CameraUpdateFactory.newLatLng(stmu));
            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Move the camera instantly to stmu with a zoom of 15

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stmu, 15));


            // end test/example -- Lynntonio
            // Note: Most likely won't be an issue, however Tags need to be released by setting them to NULL, in order to prevent memory leaks.
            // Deleting the marker does not release the tag and instead loses the reference to the tag. Should't see negative consequences
            // unless we start making thousands of tags, in which case we need to start releasing tags. -- Lynntonio

            //A statement like this will work to hide markers in the[]:  markers.get(0).setVisible(false);
            // get tag will also work
            mMap.moveCamera(CameraUpdateFactory.newLatLng(stmu));

            //setup for the Cluster Manager
            mCM = new ClusterManager<>(MapsActivity.this, mMap);
            // Points the maps listeners at the listeners implemented by the cluster manager
            mMap.setOnCameraIdleListener(mCM);
            mMap.setOnMarkerClickListener(mCM); //Never this.
            // add cluster items (markers) to the cluster manager.
            addItems();

            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Move the camera instantly to stmu with a zoom of 15.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stmu, 15));
            if(day.equals("1")) {
                //this is all shaky and the map is retarded...fix it
                GroundOverlayOptions fridayMap = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.fobcafri))
                        .position(stmu, 650f, 625f);

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



        private void addItems()
        {
            //starting coordinates I guess...
            double lat = 29.45449260178782;
            double lng = -98.56678047528071;

            //Add ten cluster Items in close proximity, for now...
            for(int i = 0; i < 10; i++)
            {
                double offset = i / 60000d;
                lat = lat + offset;
                lng = lng + offset;
                MyItem offsetItem = new MyItem(lat, lng);
                mCM.addItem(offsetItem);
            }

        }
    }

    /*class OwnIconRendered  extends DefaultClusterRenderer<MyItem>
    {
        public OwnIconRendered(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager)
        {
            super(context, map, clusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions)
        {
            markerOptions.icon(item.getIcon)
        }
    }*/ //Was just testing some stuff.

