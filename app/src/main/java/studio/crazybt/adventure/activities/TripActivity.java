package studio.crazybt.adventure.activities;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TabLayoutAdapter;
import studio.crazybt.adventure.fragments.TabDiaryTripFragment;
import studio.crazybt.adventure.fragments.TabDiscussTripFragment;
import studio.crazybt.adventure.fragments.TabMapTripFragment;
import studio.crazybt.adventure.fragments.TabMembersTripFragment;
import studio.crazybt.adventure.fragments.TabScheduleTripFragment;

public class TripActivity extends AppCompatActivity {

    private TabLayout tlTrip;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip);

        toolbar = (Toolbar) findViewById(R.id.tbTrip);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Đi đi lại lại Hà Nội - Sài Gòn 15/08 - 15/09");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        this.setTablayout();
    }

    public void setTablayout(){
        tlTrip = (TabLayout) findViewById(R.id.tlTrip);
        tlTrip.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPager vpTrip = (ViewPager) findViewById(R.id.vpTrip);
        TabLayoutAdapter tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(new TabScheduleTripFragment(), getResources().getString(R.string.schedule_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabMapTripFragment(), getResources().getString(R.string.map_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabDiscussTripFragment(), getResources().getString(R.string.discuss_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabDiaryTripFragment(), getResources().getString(R.string.diary_tablayout_trip));
        tabLayoutAdapter.addFragment(new TabMembersTripFragment(), getResources().getString(R.string.members_tablayout_trip));
        vpTrip.setAdapter(tabLayoutAdapter);
        tlTrip.setupWithViewPager(vpTrip);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_trip, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
