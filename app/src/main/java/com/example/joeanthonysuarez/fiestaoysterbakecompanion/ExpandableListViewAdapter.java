package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context context;
    private String[] headerNames = {"Oyster Bake Hours", "Parking", "Park & Ride", "Family Friendly Event"};
    private String[][] childNames = {{"Friday April 12: 5:00-11:00 PM (Fireworks start at 10:30 PM", "Saturday April 13: 12:00-11:00 PM"},
            {"We appreciate the patience of our neighbors each year as we conduct the Oyster Bake. Because we are located in a residential area, parking is somewhat congested around the event. We offer limited on-campus parking for $20."},
            {"We highly encourage everyone to take the VIA Park and Ride located at Crossroads. Each passenger will be charged a $5 roundtrip fee and will receive a coupon for a free soda, water or Red Bull at the Oyster Bake."},
            {"The Oyster Bake is a family friendly event. We encourage families to attend Friday evening as we feature entertainment for families and the Integrity Roofing and Siding Fireworks Spectacular at the end of the night. On both days, kids and kids at heart can visit our carnival area known as Shuckie Street, named for our Oyster Bake mascot. We offer rides and games for children of all ages. Also, children 12 years old and younger, receive FREE admission to Oyster Bake."}};

    public ExpandableListViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getGroupCount() {
        return headerNames.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return childNames[groupPosition].length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return headerNames[groupPosition];
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return childNames[groupPosition][childPosition];
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);

        textView.setText(headerNames[groupPosition]);
        textView.setPadding(100, 0, 0, 0);
        textView.setTextSize(24);

        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = new TextView(context);

        textView.setText(childNames[groupPosition][childPosition]);
        textView.setPadding(25, 0, 0, 0);
        textView.setTextSize(16);

        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}