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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * requires forwarding all the important lifecycle methods onto MapView.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                ClusterManager.OnClusterClickListener<MyItem>,
                                                                ClusterManager.OnClusterItemClickListener<MyItem>,
                                                                ClusterManager.OnClusterInfoWindowClickListener<MyItem>{

        public static String day;
        private GoogleMap mMap;
        private ClusterManager<MyItem> mCM;
        private MyItem clickedItem;

        //ArrayList of MiItem class, for collecting the references to marker objects. -- Lynntonio
        List<MyItem> markers = new ArrayList<MyItem>();
        //used in filtering purposes.
        List<MyItem> removedMarkers = new ArrayList<>();



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
                                            if (filterB[x] == false)
                                            {
                                                removedMarkers.add(markers.get(i));
                                                mCM.removeItem(markers.get(i));
                                            }
                                    }
                                }


                            }

                            for (int i = 0; i < removedMarkers.size(); i++)
                            {
                                for (int x = 0; x < filterArray.length; x++)
                                {
                                    if (markers.get(i).getTag() == filterArray[x])
                                    {
                                        if (filterB[x] == true)
                                        {
                                            mCM.addItem(markers.get(i));
                                            removedMarkers.remove(markers.get(i));

                                        }
                                    }
                                }
                            }

                            mCM.cluster();
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
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            mMap.setInfoWindowAdapter(new newinfoAdapter(MapsActivity.this));

            /* Most of this is for testing and example purposes, This should be commented/deleted out for the final build. -- Lynntonio
            || Also everything here is deprecated don't expect to be able to use it as an example.
            LatLng stmu = new LatLng(29.45249260178782, -98.56478047528071);
            LatLng c1 = new LatLng(29.45149260178782, -98.56478047528071);
            LatLng c2 = new LatLng(29.45249260178782, -98.56278047528071);
            LatLng c3 = new LatLng(29.45349260178782, -98.56278047528071);
            Marker test1 = mMap.addMarker(new MarkerOptions()
                    .position(stmu)
                    .title("Fiesta Oyster Bake")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.food))
                    .snippet("Example Booth\n" +
                            "Product: Chicken on a stick\n" +
                            "Price: 6 tickets\n" +
                            "Status: Open"));
            test1.setTag("Chicken");
            markers.add(test1); // this is one way to store marker references, but requires you to manually initiallize the process. Don't do this.
            markers.add(mMap.addMarker(new MarkerOptions() // thi is probably the most Ideal way to store markers.
                    .position(c1)
                    .title("Test1")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.food))
                    .snippet("Testing Filter: Tag = 'Beef'")));
            markers.get(1).setTag("Beef");
            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(c2)
                    .title("Test2")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.food))
                    .snippet("Testing Filter: Tag = 'Seafood'")));
            markers.get(2).setTag("Seafood");
            markers.add(mMap.addMarker(new MarkerOptions()
                    .position(c3)
                    .title("Test2")
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.food))
                    .snippet("Testing Filter: Tag = 'Seafood'")));
            markers.get(3).setTag("Seafood");
            */

            // end test/example -- Lynntonio
            // Note: Most likely won't be an issue, however Tags need to be released by setting them to NULL, in order to prevent memory leaks.
            // Deleting the marker does not release the tag and instead loses the reference to the tag. Should't see negative consequences
            // unless we start making thousands of tags, in which case we need to start releasing tags. -- Lynntonio

            //A statement like this will work to hide markers in the[]:  markers.get(0).setVisible(false);
            // get tag will also work
            LatLng stmu = new LatLng(29.45249260178782, -98.56478047528071);
            mMap.moveCamera(CameraUpdateFactory.newLatLng(stmu));

            //setup for the Cluster Manager -- Lynntonio
            mCM = new ClusterManager<>(MapsActivity.this, mMap);
            MyMarkerRender renderer = new MyMarkerRender(MapsActivity.this, mMap, mCM);
            mCM.setRenderer(renderer);
            // Points the maps listeners at the listeners implemented by the cluster manager
            mMap.setOnCameraIdleListener(mCM);
            mMap.setOnMarkerClickListener(mCM);
            mMap.setInfoWindowAdapter(mCM.getMarkerManager());
            mMap.setOnInfoWindowClickListener(mCM);

            mCM.setOnClusterClickListener(MapsActivity.this);
            mCM.setOnClusterItemClickListener(MapsActivity.this);
            mCM.setOnClusterInfoWindowClickListener(MapsActivity.this);

            //sets info window adapter.
            mCM.getMarkerCollection().setOnInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
            {
                @Override
                public View getInfoWindow(Marker marker)
                {
                    LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                    final View view = inflater.inflate(R.layout.newinfo, null);

                    TextView titleTV = (TextView) view.findViewById(R.id.title);
                    TextView snippetTV = (TextView) view.findViewById((R.id.snippet));

                    titleTV.setText(clickedItem.getTitle());
                    snippetTV.setText(clickedItem.getSnippet());

                    return view;
                }

                @Override
                public View getInfoContents (Marker marker)
                {
                    return null;
                }
            });
            //Example method of how to add cluster items (markers) to the cluster manager.
            addItems();

            mMap.getUiSettings().setZoomControlsEnabled(true);
            // Move the camera instantly to stmu with a zoom of 15.
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stmu, 15));
            if(day.equals("1")) {
                //this is all shaky and the map is retarded...fix it
                GroundOverlayOptions fridayMap = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.fobcafriday))
                        .position(stmu, 650f, 625f)
                        .bearing(7);

                // Add an overlay to the map, retaining a handle to the GroundOverlay object.
                GroundOverlay imageOverlay = mMap.addGroundOverlay(fridayMap);

            }else {
                //this is all shaky and the map is retarded...fix it
                GroundOverlayOptions satMap = new GroundOverlayOptions()
                        .image(BitmapDescriptorFactory.fromResource(R.drawable.fobcasat))
                        .position(stmu, 650f, 625f)
                        .bearing(7);
                // Add an overlay to the map, retaining a handle to the GroundOverlay object.
                GroundOverlay imageOverlay = mMap.addGroundOverlay(satMap);
            }



}
        //This method is used for testing and example purposes. This method shows the general way
        // to add markers in the form of MyItems and hoow to add them to the marker array for filtering. -- Lynntonio
        private void addItems()
        {
            //starting coordinates for now...
            double lat = 29.45449260178782;
            double lng = -98.56678047528071;

            //Add ten cluster Items in close proximity, for now...
            for(int i = 0; i < 10; i++)
            {
                double offset = i / 60000d;
                lat = lat + offset;
                lng = lng + offset;

                if ( i % 2 == 0)
                {
                    // MyItems should consist of (lat, ,lng, String title, String tag, BitmapDescriptor bmd)
                    MyItem offsetItem = new MyItem(lat, lng, "Booth #" + i,
                            "Example Booth #" + i + "\n" +
                                    "Product: Chicken on a stick\n" +
                                    "Price: 6 tickets\n" +
                                    "Status: Open(tag: Seafood)",
                            "Seafood",
                            BitmapDescriptorFactory.fromResource(R.drawable.food));
                    //always add to ClusterManager mCM
                    mCM.addItem(offsetItem);
                    // always add to arralist markers.
                    markers.add(offsetItem);
                }
                else
                    {
                    MyItem offsetItem = new MyItem(lat, lng, "Booth #" + i,
                            "Example Booth #" + i + "\n" +
                                    "Product: Chicken on a stick\n" +
                                    "Price: 6 tickets\n" +
                                    "Status: Open (tag: Beef)",
                            "Beef",
                            BitmapDescriptorFactory.fromResource(R.drawable.food));
                        mCM.addItem(offsetItem);
                        markers.add(offsetItem);
                    }
            }

        }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster)
    {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        Collection<MyItem> myItemMarker= cluster.getItems();

        for (ClusterItem item: myItemMarker)
        {
            LatLng itemPosition = item.getPosition();
            builder.include(itemPosition);
        }

        final LatLngBounds bounds = builder.build();

        try { mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));}
        catch (Exception error)
        {

        }

        return true;
    }

    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {

    }

    @Override
    public boolean onClusterItemClick(MyItem myItem) {
            clickedItem = myItem;
        return false;
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

