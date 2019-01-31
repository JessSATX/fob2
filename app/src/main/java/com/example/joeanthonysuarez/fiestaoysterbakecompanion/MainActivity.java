package com.example.joeanthonysuarez.fiestaoysterbakecompanion;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.content.Context;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.content.res.Resources.Theme;

import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        // Setup spinner
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setAdapter(new MyAdapter(
                toolbar.getContext(),
                new String[]{
                        "Home",
                        "Map",
                        "Showtimes",
                        "About",
                        "FAQ"
                }));

        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // When the given dropdown item is selected, show its contents in the
                // container view.
                if (position == 0) // Home tab content
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, HomeTab.newInstance(position + 1))
                            .commit();
                } else if (position == 1) // Map content
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, MapTab.newInstance(position + 1))
                            .commit();
                } else if (position == 2) // Showtimes content
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, ShowtimesTab.newInstance(position + 1))
                            .commit();
                } else if (position == 3) // About content
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, AboutTab.newInstance(position + 1))
                            .commit();
                } else if (position == 4) // FAQ content
                {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, FAQTab.newInstance(position + 1))
                            .commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private static class MyAdapter extends ArrayAdapter<String> implements ThemedSpinnerAdapter {
        private final ThemedSpinnerAdapter.Helper mDropDownHelper;

        public MyAdapter(Context context, String[] objects) {
            super(context, android.R.layout.simple_list_item_1, objects);
            mDropDownHelper = new ThemedSpinnerAdapter.Helper(context);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            View view;

            if (convertView == null) {
                // Inflate the drop down using the helper's LayoutInflater
                LayoutInflater inflater = mDropDownHelper.getDropDownViewInflater();
                view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
            } else {
                view = convertView;
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getItem(position));

            return view;
        }

        @Override
        public Theme getDropDownViewTheme() {
            return mDropDownHelper.getDropDownViewTheme();
        }

        @Override
        public void setDropDownViewTheme(Theme theme) {
            mDropDownHelper.setDropDownViewTheme(theme);
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class HomeTab extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Button buyTicketsButton;
        public Button followOnTwitterButton;
        public Button followOnInstagramButton;
        public Button likeOnFacebookButton;

        public HomeTab() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static HomeTab newInstance(int sectionNumber) {
            HomeTab fragment = new HomeTab();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            // currentYear = 2097;   // Play around with different year values.

            String welcomeTextString = "Welcome to the ";
            welcomeTextString = welcomeTextString + Integer.toString(currentYear);
            welcomeTextString = welcomeTextString + " Fiesta Oyster Bake!!!";

            int yearOnesDigit = (currentYear - 1916) % 100;
            String yearCountTextString = "The Fiesta Oyster Bake is celebrating its " + Integer.toString(currentYear - 1916);
            switch (yearOnesDigit) {
                case 1:
                case 21:
                case 31:
                case 41:
                case 51:
                case 61:
                case 71:
                case 81:
                case 91:
                    yearCountTextString = yearCountTextString + "st ";
                    break;
                case 2:
                case 22:
                case 32:
                case 42:
                case 52:
                case 62:
                case 72:
                case 82:
                case 92:
                    yearCountTextString = yearCountTextString + "nd ";
                    break;
                case 3:
                case 23:
                case 33:
                case 43:
                case 53:
                case 63:
                case 73:
                case 83:
                case 93:
                    yearCountTextString = yearCountTextString + "rd ";
                    break;
                default:
                    yearCountTextString = yearCountTextString + "th ";
                    break;
            }
            yearCountTextString = yearCountTextString + "year. The party's just begun at the heartbeat of Fiesta.";

            View rootView = inflater.inflate(R.layout.home_tab, container, false);
            TextView welcomeText = (TextView) rootView.findViewById(R.id.welcome_text);
            TextView yearCountText = (TextView) rootView.findViewById(R.id.year_count_text);
            buyTicketsButton = (Button) rootView.findViewById(R.id.buy_tickets_button);
            followOnTwitterButton = (Button) rootView.findViewById(R.id.twitter_button);
            followOnInstagramButton = (Button) rootView.findViewById(R.id.instagram_button);
            likeOnFacebookButton = (Button) rootView.findViewById(R.id.facebook_button);

            welcomeText.setText(welcomeTextString);
            yearCountText.setText(yearCountTextString);
            buyTicketsButton.setOnClickListener(this);
            followOnTwitterButton.setOnClickListener(this);
            followOnInstagramButton.setOnClickListener(this);
            likeOnFacebookButton.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View view) {
            Uri webpage = null;
            Intent buttonIntent;
            PackageManager packageManager;
            List<ResolveInfo> buttonActivities;
            boolean isIntentSafe;

            switch (view.getId()) {
                case R.id.buy_tickets_button:
                    webpage = Uri.parse("http://oysterbake.com/tickets-2/");
                    break;
                case R.id.twitter_button:
                    webpage = Uri.parse("https://twitter.com/Oyster_Bake");
                    break;
                case R.id.instagram_button:
                    webpage = Uri.parse("https://www.instagram.com/oysterbake/");
                    break;
                case R.id.facebook_button:
                    webpage = Uri.parse("https://www.facebook.com/FiestaOysterBake/");
                    break;
                default:
                    Toast.makeText(view.getContext(), "Could not complete action", Toast.LENGTH_SHORT).show();
            }

            buttonIntent = new Intent(Intent.ACTION_VIEW, webpage);
            packageManager = view.getContext().getPackageManager();
            buttonActivities = packageManager.queryIntentActivities(buttonIntent, PackageManager.MATCH_DEFAULT_ONLY);
            isIntentSafe = (buttonActivities.size() > 0);
            if (isIntentSafe) {
                startActivity(buttonIntent);
            }
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class MapTab extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public MapTab() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static MapTab newInstance(int sectionNumber) {
            MapTab fragment = new MapTab();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.map_tab, container, false);

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class ShowtimesTab extends Fragment implements View.OnClickListener {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public Button stage1;
        public Button stage2;
        public Button stage3;
        public Button stage4;
        public Button stage5;

        public ShowtimesTab() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static ShowtimesTab newInstance(int sectionNumber) {
            ShowtimesTab fragment = new ShowtimesTab();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.showtimes_tab, container, false);

            stage1 = (Button) rootView.findViewById(R.id.Stage1);
            stage2 = (Button) rootView.findViewById(R.id.Stage2);
            stage3 = (Button) rootView.findViewById(R.id.Stage3);
            stage4 = (Button) rootView.findViewById(R.id.Stage4);
            stage5 = (Button) rootView.findViewById(R.id.Stage5);

            stage1.setOnClickListener(this);
            stage2.setOnClickListener(this);
            stage3.setOnClickListener(this);
            stage4.setOnClickListener(this);
            stage5.setOnClickListener(this);

            return rootView;
        }

        @Override
        public void onClick(View view) {
            String stageNum = "";

            switch (view.getId()) {
                case R.id.Stage1:
                    stageNum = "1";
                    break;
                case R.id.Stage2:
                    stageNum = "2";
                    break;
                case R.id.Stage3:
                    stageNum = "3";
                    break;
                case R.id.Stage4:
                    stageNum = "4";
                    break;
                case R.id.Stage5:
                    stageNum = "5";
                    break;
            }

            // New intent to go to ScheduleList.java Activity
            Intent intent = new Intent(getActivity(), ScheduleList.class);

            // Send the stage number as a string.
            intent.putExtra("STAGE_NUM", stageNum);
            startActivity(intent);
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class AboutTab extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public AboutTab() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static AboutTab newInstance(int sectionNumber) {
            AboutTab fragment = new AboutTab();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.about_tab, container, false);

            return rootView;
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class FAQTab extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private ExpandableListView expandableListView;
        private ExpandableListViewAdapter expandableListViewAdapter;
        private List<String> listDataGroup;
        private HashMap<String, List<String>> listDataChild;

        public FAQTab() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static FAQTab newInstance(int sectionNumber) {
            FAQTab fragment = new FAQTab();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.faq_tab, container, false);

            // Initialize views.
            initViews(rootView);

            // Initialize listeners.
            initListeners(rootView);

            // Initialize objects.
            initObjects();

            // Prepare list data.
            initListData();

            return rootView;
        }

        private void initViews(View view) {
            expandableListView = view.findViewById(R.id.expandableListView);
        }

        private void initListeners(final View view) {
            expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                @Override
                public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                    Toast.makeText(view.getContext(), listDataGroup.get(groupPosition) + " : " + listDataChild.get(listDataGroup.get(groupPosition)).get(childPosition), Toast.LENGTH_SHORT).show();

                    return false;
                }
            });

            expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
                @Override
                public void onGroupExpand(int groupPosition) {
                    Toast.makeText(view.getContext(), listDataGroup.get(groupPosition) + " " + getString(R.string.text_expanded), Toast.LENGTH_SHORT).show();
                }
            });

            expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
                @Override
                public void onGroupCollapse(int groupPosition) {
                    Toast.makeText(view.getContext(), listDataGroup.get(groupPosition) + " " + getString(R.string.text_collapsed), Toast.LENGTH_SHORT).show();
                }
            });
        }

        private void initObjects() {
            // Initialize list of groups.
            listDataGroup = new ArrayList<>();

            // Initialize list of child.
            listDataChild = new HashMap<>();

            // Initialize adapter object.
            expandableListViewAdapter = new ExpandableListViewAdapter(this.getContext(), listDataGroup, listDataChild);

            // Set the adapter to the expandable list view.
            expandableListView.setAdapter(expandableListViewAdapter);
        }

        // Dummy data prepared.
        private void initListData() {
            // Add group data.
            listDataGroup.add(getString(R.string.test_drop_down));

            String[] strings;

            // Test the drop down.
            List<String> alcoholList = new ArrayList<>();
            strings = getResources().getStringArray(R.array.test_array_1);
            for (String string : strings) {
                alcoholList.add(string);
            }

            // Add child data.
            listDataChild.put(listDataGroup.get(0), alcoholList);

            expandableListViewAdapter.notifyDataSetChanged();
        }
    }
}
