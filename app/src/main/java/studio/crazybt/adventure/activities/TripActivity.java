package studio.crazybt.adventure.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TripAdapter;

public class TripActivity extends AppCompatActivity {

    private TabLayout tlTrip;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        toolbar = (Toolbar) findViewById(R.id.tbTrip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tlTrip = (TabLayout) findViewById(R.id.tlTrip);
        tlTrip.addTab(tlTrip.newTab().setText(getResources().getString(R.string.schedule_tablayout_trip)));
        tlTrip.addTab(tlTrip.newTab().setText(getResources().getString(R.string.map_tablayout_trip)));
        tlTrip.addTab(tlTrip.newTab().setText(getResources().getString(R.string.discuss_tablayout_trip)));
        tlTrip.addTab(tlTrip.newTab().setText(getResources().getString(R.string.diary_tablayout_trip)));
        tlTrip.setTabGravity(TabLayout.GRAVITY_FILL);
        //final int tabSelectedTextColor = ContextCompat.getColor(this.getBaseContext(), R.color.white);
        //final int tabUnselectedTextColor = ContextCompat.getColor(this.getBaseContext(), R.color.secondary_text);

        final ViewPager vpTrip = (ViewPager) findViewById(R.id.vpTrip);
        vpTrip.setOffscreenPageLimit(3);
        final TripAdapter tripAdapter = new TripAdapter(getSupportFragmentManager(), tlTrip.getTabCount());
        vpTrip.setAdapter(tripAdapter);
        vpTrip.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlTrip));
//        tlTrip.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                int temp = tab.getPosition();
//                vpHomePage.setCurrentItem(tab.getPosition());
//                tab.getIcon().setColorFilter(tabSelectedTextColor, PorterDuff.Mode.SRC_IN);
//                homePageAdapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//                tab.getIcon().setColorFilter(tabUnselectedTextColor, PorterDuff.Mode.SRC_IN);
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_trip, menu);
        return true;
    }
}
