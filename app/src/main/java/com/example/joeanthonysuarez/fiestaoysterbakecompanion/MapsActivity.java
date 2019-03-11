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
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This shows how to create a simple activity with a raw MapView and add a marker to it. This
 * requires forwarding all the important lifecycle methods onto MapView.
 */
public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback,
                                                                ClusterManager.OnClusterClickListener<MyItem>,
                                                                ClusterManager.OnClusterItemClickListener<MyItem>,
                                                                ClusterManager.OnClusterInfoWindowClickListener<MyItem> {

    Button refreshButton;
    public static String day;
    private GoogleMap mMap;
    private ClusterManager<MyItem> mCM;
    private static final String TAG = MapsActivity.class.getSimpleName();
    private MyItem currentItem;
    StringBuilder DescriptionOfBooth = new StringBuilder();
    private DatabaseReference firebaseReference;
    HashMap<Integer, String> allDescriptions = new HashMap<>();
    List<Integer> boothNumbers = new ArrayList<>();

    //ArrayList of Marker class, for collecting the references to marker objects. -- Lynntonio
    List<MyItem> markers = new ArrayList<MyItem>();
    private MyItem clickedItem;

    //used in filtering purposes.
    List<MyItem> removedMarkers = new ArrayList<>();

    //This is used to save the variables in that we use for the markers
    String markerTitle = "intitial";
    Double markerLat = 0.0;
    Double markerLang = 0.0;
    String markerTags = "defaultTag";
    Integer imageTag = 1;


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
                final String[] filterArray = new String[]{"Chicken", "Beef", "Seafood", "Veggies", "Sweets", "Beverages", "Alcoholic Beverages", "Other"};
                //Boolean Array for selected items. Index contents are meant to correspond with those "filterArray". -- Lynntonio
                final boolean[] filterB = new boolean[]{true, true, true, true, true, true, true, true};

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
                        for (int i = 0; i < markers.size(); i++) {
                            for (int x = 0; x < filterArray.length; x++) {
                                if (markers.get(i).getTag() == filterArray[x]) {
                                    if (filterB[x] == false) {
                                        removedMarkers.add(markers.get(i));
                                        mCM.removeItem(markers.get(i));
                                    }
                                }
                            }


                        }

                        for (int i = 0; i < removedMarkers.size(); i++) {
                            for (int x = 0; x < filterArray.length; x++) {
                                if (markers.get(i).getTag() == filterArray[x]) {
                                    if (filterB[x] == true) {
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
        // set up connection to firebase

        //If it is Friday Map
        if(day.equals("1")) {

            firebaseReference = FirebaseDatabase.getInstance().getReference();

            firebaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getMarkerDataFriday(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //backupMap
                }
            });
        }
        //If it is Saturday Map
        else
        {
            firebaseReference = FirebaseDatabase.getInstance().getReference();

            firebaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    getMarkerDataSaturday(dataSnapshot);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    //backupMap
                }
            });
        }


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
        renderer.setMinClusterSize(1);

        //sets info window adapter.
        mCM.getMarkerCollection().setOnInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                final View view = inflater.inflate(R.layout.newinfo, null);

                TextView titleTV = (TextView) view.findViewById(R.id.title);
                TextView snippetTV = (TextView) view.findViewById((R.id.snippet));

                titleTV.setText(clickedItem.getTitle());
                snippetTV.setText(clickedItem.getSnippet());

                return view;
            }

            @Override
            public View getInfoContents(Marker marker) {
                return null;
            }
        });

        //Example method of how to add cluster items (markers) to the cluster managers

        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Move the camera instantly to stmu with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stmu, 15));
        if (day.equals("1")) {
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


    //This method is used for testing and example purposes. This method shows the general way
    // to add markers in the form of MyItems and hoow to add them to the marker array for filtering. -- Lynntonio


    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {
        LatLngBounds.Builder builder = LatLngBounds.builder();
        Collection<MyItem> myItemMarker = cluster.getItems();

        for (ClusterItem item : myItemMarker) {
            LatLng itemPosition = item.getPosition();
            builder.include(itemPosition);
        }

        final LatLngBounds bounds = builder.build();

        try {
            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception error) {

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

    /**
     * This function is essentially a large switch, using the photo of the imageTags, you must pass in
     * the right number to get the image you want.
     * @param imageTag
     * @return
     */
    public BitmapDescriptor getImageFromTag(int imageTag) {
        BitmapDescriptor bitmap;

        switch (imageTag) {
            case 1:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.ambulance_all_sec);
                break;
            case 2:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.atm);
                break;
            case 3:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.beer_all);
                break;
            case 4:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.cans_recycle);
                break;
            case 5:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.chicken_on_a_stick_all_sec);
                break;
            case 6:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.coupon_booth);
                break;
            case 7:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.drink_section1_multi_booth);
                break;
            case 8:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.drink_section3);
                break;
            case 9:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.first_aid);
                break;
            case 10:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.first_aid_sec1);
                break;
            case 11:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.food_sec1);
                break;
            case 12:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.food_sec3);
                break;
            case 13:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.food_sec3_small);
                break;
            case 14:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.food_sec3_small2);
                break;
            case 15:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.gate_icon);
                break;
            case 16:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.lostnfound);
                break;
            case 17:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.oysters);
                break;
            case 18:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.port_a_john_handicap);
                break;
            case 19:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.port_a_john_sec1);
                break;
            case 20:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.red_bull_sec2);
                break;
            case 21:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec1_green_flag);
                break;
            case 22:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec2_flag);
                break;
            case 23:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec2_food_booth);
                break;
            case 24:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec3_flag);
                break;
            case 25:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec4_yelllowflag);
                break;
            case 26:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec5_food_long);
                break;
            case 27:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec5_food_small);
                break;
            case 28:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec5_purple_flag);
                break;
            case 29:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.soda_pin_all);
                break;
            case 30:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.souvin_sec2);
                break;
            case 31:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sweepstakes_sec3);
                break;
            case 32:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.v_i_p);
                break;
            case 33:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.vol_app_area);
                break;
            case 34:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.water_sec1);
                break;
            default:
                bitmap = BitmapDescriptorFactory.fromResource(R.drawable.sec1_green_flag);
        }

        return bitmap;
    }

    /**
     * Add all the attributes to the item
     * @param lat
     * @param lng
     * @param title
     * @param snippet
     * @param tag
     * @param bitmap
     * @return
     */
    public MyItem addAttributesToItem(double lat, double lng, String title, String snippet, String tag, BitmapDescriptor bitmap) {
        MyItem markerToAdd = new MyItem(lat, lng, title, snippet, tag, bitmap);
        return markerToAdd;
    }

    /**
     * We have to get the booth numbers and compare the hashmaps keys to the numbers
     * if they match, then concatenate that corresponding keys description to the markers description
     * @param boothNumbers
     * @return description for the entire marker (including all booths)
     */
    public String giveDescriptionsToMarkers(List<Integer> boothNumbers) {
        String markersFullDescription = new String();
        for (Map.Entry<Integer, String> entry : allDescriptions.entrySet()) {
            for (int i = 0; i < boothNumbers.size(); i++) {
                if (entry.getKey().equals(boothNumbers.get(i))) {
                    markersFullDescription = markersFullDescription.concat(entry.getValue());
                }
            }
        }
//         We can use this to trace the HashMap of the Descriptions incase something goes wrong
//        for (Map.Entry<Integer,String> name: allDescriptions.entrySet()){
//
//            String key =name.toString();
//            String value = allDescriptions.get(name);
//            System.out.println("The keys and values are"
//                    +key + " " + value);
//        }
        return markersFullDescription;
    }

    /**
     * This function you can use to populate the Friday map
     * @param dataSnapshot
     */
    public void getMarkerDataFriday(DataSnapshot dataSnapshot) {
        FillDescriptionDataMapFriday(dataSnapshot);

        for (DataSnapshot markersFromFB : dataSnapshot.child("MarkersFriday").getChildren()) {

            //new booth, new boothNumbers
            boothNumbers.clear();

            //get the values for Lat,Long
            String markerPlaceHolderLatLang[] = markersFromFB.child("Position").getValue().toString().split(",");
            markerLat = Double.parseDouble(markerPlaceHolderLatLang[0]);
            markerLang = Double.parseDouble(markerPlaceHolderLatLang[1]);

            //get the title,imageTag and corresponding booths
            markerTitle = markersFromFB.child("Title").getValue().toString();
            imageTag = Integer.parseInt(markersFromFB.child("Image").getValue().toString());
            String allBoothValues[] = markersFromFB.child("Booths").getValue().toString().split(",");

            //put those booths in a list so we can access all of them for this specific marker
            for (int i = 0; i < allBoothValues.length; i++) {
                boothNumbers.add(Integer.parseInt(allBoothValues[i]));
            }

            //add all the attributes that we got from above code to a MyItem object
            currentItem = addAttributesToItem(markerLat, markerLang, markerTitle, giveDescriptionsToMarkers(boothNumbers), "Beef", getImageFromTag(imageTag));
            markers.add(currentItem);
            mCM.addItem(currentItem);
        }

    }

    /**
     * This function used to actually populate the Saturday Map
     * @param dataSnapshot
     */
    public void getMarkerDataSaturday(DataSnapshot dataSnapshot) {
        FillDescriptionDataMapSaturday(dataSnapshot);

        for (DataSnapshot markersFromFB : dataSnapshot.child("MarkersSaturday").getChildren()) {

            //new booth, new boothNumbers
            boothNumbers.clear();

            //get the values for Lat,Long
            String markerPlaceHolderLatLang[] = markersFromFB.child("Position").getValue().toString().split(",");
            markerLat = Double.parseDouble(markerPlaceHolderLatLang[0]);
            markerLang = Double.parseDouble(markerPlaceHolderLatLang[1]);

            //get the title,imageTag and corresponding booths
            markerTitle = markersFromFB.child("Title").getValue().toString();
            imageTag = Integer.parseInt(markersFromFB.child("Image").getValue().toString());
            String allBoothValues[] = markersFromFB.child("Booths").getValue().toString().split(",");

            //put those booths in a list so we can access all of them for this specific marker
            for (int i = 0; i < allBoothValues.length; i++) {
                boothNumbers.add(Integer.parseInt(allBoothValues[i]));
            }

            //add all the attributes that we got from above code to a MyItem object
            currentItem = addAttributesToItem(markerLat, markerLang, markerTitle, giveDescriptionsToMarkers(boothNumbers), "Beef", getImageFromTag(imageTag));
            markers.add(currentItem);
            mCM.addItem(currentItem);
        }

    }

    /**
     * This fuction used to fill the HashMap with values for Friday
     * @param dataSnapshot
     */
    public void FillDescriptionDataMapFriday(DataSnapshot dataSnapshot) {
        for (DataSnapshot boothsFromFB : dataSnapshot.child("BoothsFriday").getChildren()) {
            DescriptionOfBooth = DescriptionOfBooth.delete(0, DescriptionOfBooth.length());
            DescriptionOfBooth = DescriptionOfBooth.append(boothsFromFB.child("Coupon Count").getValue().toString());
            DescriptionOfBooth = DescriptionOfBooth.append(" Coupons");
            DescriptionOfBooth = DescriptionOfBooth.append("\n");
            DescriptionOfBooth = DescriptionOfBooth.append(boothsFromFB.child("Description").getValue().toString());
            DescriptionOfBooth = DescriptionOfBooth.append("\n");
            DescriptionOfBooth = DescriptionOfBooth.append("Status: ");
            DescriptionOfBooth = DescriptionOfBooth.append(boothsFromFB.child("Status").getValue().toString());
            DescriptionOfBooth = DescriptionOfBooth.append("\n\n");

            allDescriptions.put(Integer.parseInt(boothsFromFB.child("Booth Number").getValue().toString()), DescriptionOfBooth.toString());
        }
    }

    /**
     * Used to fill theHashMap with values for Saturday
     * @param dataSnapshot
     */
    public void FillDescriptionDataMapSaturday(DataSnapshot dataSnapshot) {
        for (DataSnapshot boothsFromFB : dataSnapshot.child("BoothsSaturday").getChildren()) {
            //Here we are going and getting all the 3 attributes and structuring it for each booths description
            DescriptionOfBooth = DescriptionOfBooth.delete(0, DescriptionOfBooth.length());
            DescriptionOfBooth = DescriptionOfBooth.append(boothsFromFB.child("Coupon Count").getValue().toString());
            DescriptionOfBooth = DescriptionOfBooth.append(" Coupons");
            DescriptionOfBooth = DescriptionOfBooth.append("\n");
            DescriptionOfBooth = DescriptionOfBooth.append(boothsFromFB.child("Description").getValue().toString());
            DescriptionOfBooth = DescriptionOfBooth.append("\n");
            DescriptionOfBooth = DescriptionOfBooth.append("Status: ");
            DescriptionOfBooth = DescriptionOfBooth.append(boothsFromFB.child("Status").getValue().toString());
            DescriptionOfBooth = DescriptionOfBooth.append("\n\n");

            //We then get tht booth number, its corresponding description and saving it into a hashMap
            allDescriptions.put(Integer.parseInt(boothsFromFB.child("Booth Number").getValue().toString()), DescriptionOfBooth.toString());
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

