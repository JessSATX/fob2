package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.graphics.Bitmap;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private final String mTag;
    private final BitmapDescriptor mIcon;

    public MyItem(LatLng position) {
        mPosition = position;
        mTitle = "";
        mSnippet = "";
        mTag = "";
        mIcon = BitmapDescriptorFactory.fromResource(R.drawable.food);
    }

    public MyItem(LatLng position, String title, String snippet, String tag, BitmapDescriptor bitmap)
    {
        mPosition = position;
        mTitle = title;
        mSnippet = snippet;
        mTag = tag;
        mIcon = bitmap;

    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }

    @Override
    public String getSnippet() {
        return mSnippet;
    }

    public String getTag() {return mTag;}

    public BitmapDescriptor getIcon() {return mIcon;}


}
