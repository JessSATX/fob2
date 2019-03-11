package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;

public class MyMarkerRender extends DefaultClusterRenderer<MyItem> {
    private final Context mContext;
    private boolean isDraggable;

    public MyMarkerRender(Context context, GoogleMap map, ClusterManager<MyItem> clusterManager)
    {
        super(context, map, clusterManager);
        mContext = context;
    }

    @Override
    protected void onBeforeClusterItemRendered(MyItem item, MarkerOptions markerOptions)
    {
        markerOptions.icon(item.getIcon());
        markerOptions.draggable(isDraggable);  // THIS IS UNIVERSAL!!!
    }

    public void setDraggable (boolean bool)
    {
        isDraggable = bool;
    }
}