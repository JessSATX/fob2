package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        LatLng stmu = new LatLng(-98.564527, -98.56427);
//this is all shaky and the map is retarded...fix it
        GroundOverlayOptions newarkMap = new GroundOverlayOptions()
                .image(BitmapDescriptorFactory.fromResource(R.drawable.fobca))
                .position(stmu, 8600f, 6500f);

// Add an overlay to the map, retaining a handle to the GroundOverlay object.
       GroundOverlay imageOverlay = mMap.addGroundOverlay(newarkMap);

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

        // Add a marker in Sydney and move the camera
        LatLng stmu = new LatLng(29.45177, -98.56427);
        mMap.addMarker(new MarkerOptions().position(stmu).title("Fiesta Oyster Bake"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(stmu));
        mMap.getUiSettings().setZoomControlsEnabled(true);
        // Move the camera instantly to stmu with a zoom of 15.
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(stmu, 15));
    }
}
