package the.fiesta.OysterBake.Companion;


import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.ArrayList;
import java.util.List;

public class MyItem implements ClusterItem {

    private final LatLng mPosition;
    private final String mTitle;
    private final String mSnippet;
    private List<String> mTag = new ArrayList<>();
    private final BitmapDescriptor mIcon;
    private boolean visibility = true;

    public MyItem(double lat, double lng) {
        mPosition = new LatLng(lat, lng);
        mTitle = "";
        mSnippet = "";
        mIcon = BitmapDescriptorFactory.fromResource(R.drawable.food);
    }

    public MyItem(double lat, double lng, String title, String snippet, String tag, BitmapDescriptor bitmap)
    {
        mPosition = new LatLng(lat, lng);
        mTitle = title;
        mSnippet = snippet;
        mTag.add(tag);
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

    public boolean isVisible() {return this.visibility;}

    public void setVisibility(boolean v) { this.visibility = v;}

    public List<String> getTags() {return mTag;}

    public void addTag(String tag)
    {
        mTag.add(tag);
    }

    public void removeTag(int index)
    {
        mTag.remove(index);
    }

    public BitmapDescriptor getIcon() {return mIcon;}


}
