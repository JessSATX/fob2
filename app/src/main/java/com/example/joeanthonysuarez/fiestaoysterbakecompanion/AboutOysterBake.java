package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Calendar;

public class AboutOysterBake extends AppCompatActivity implements OnMapReadyCallback {

    TextView fobTitle1;
    GoogleMap mMap;
    private final String TAG = MapsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_oyster_bake);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        int currentFOBCount = (Calendar.getInstance().get(Calendar.YEAR)) - 1916;

        fobTitle1 = findViewById(R.id.fob_title_1);

        fobTitle1.setText(Integer.toString(currentFOBCount) + " Years and Counting");

    }

        @Override
        public void onMapReady(GoogleMap googleMap) {
            {
                mMap = googleMap;
                mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                mMap.getUiSettings().setAllGesturesEnabled(false);
                // Move the camera instantly to stmu with a zoom of 15.
                LatLng gate1 = new LatLng(29.454482242994565, -98.56836338953309);
                LatLng gate2 = new LatLng(29.44891046566121, -98.56295840989685);
                LatLng gate3 = new LatLng(29.45126734383246, -98.56090615697059);
                LatLng stmu = new LatLng(29.45249260178782, -98.56478047528071);

                mMap.addMarker(new MarkerOptions()
                        .title("Fiesta Oyster Bake NW36th Entrance")
                        .position(gate1));

                mMap.addMarker(new MarkerOptions()
                        .title("Fiesta Oyster Bake Culebra Entrance")
                        .position(gate2));

                mMap.addMarker(new MarkerOptions()
                        .title("Fiesta Oyster Bake Camino Santa Maria Entrance")
                        .position(gate3));


                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stmu, 15));
            }

        }
    }
