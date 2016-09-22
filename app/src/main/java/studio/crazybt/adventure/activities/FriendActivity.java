package studio.crazybt.adventure.activities;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.FriendAdapter;
import studio.crazybt.adventure.adapters.TripAdapter;

public class FriendActivity extends AppCompatActivity {

    private TabLayout tlFriend;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        toolbar = (Toolbar) findViewById(R.id.tbFriend);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getString(R.string.tb_friend));

        this.setTablayout();
    }

    public void setTablayout(){
        tlFriend = (TabLayout) findViewById(R.id.tlFriend);
        tlFriend.addTab(tlFriend.newTab().setText(getResources().getString(R.string.request_tablayout_friend)));
        tlFriend.addTab(tlFriend.newTab().setText(getResources().getString(R.string.suggestion_tablayout_friend)));
        tlFriend.addTab(tlFriend.newTab().setText(getResources().getString(R.string.all_tablayout_friend)));
        tlFriend.addTab(tlFriend.newTab().setText(getResources().getString(R.string.following_tablayout_friend)));
        tlFriend.addTab(tlFriend.newTab().setText(getResources().getString(R.string.follower_tablayout_friend)));
        tlFriend.setTabGravity(TabLayout.GRAVITY_FILL);
        //final int tabSelectedTextColor = ContextCompat.getColor(this.getBaseContext(), R.color.white);
        //final int tabUnselectedTextColor = ContextCompat.getColor(this.getBaseContext(), R.color.secondary_text);

        final ViewPager vpFriend = (ViewPager) findViewById(R.id.vpFriend);
        vpFriend.setOffscreenPageLimit(3);
        final FriendAdapter friendAdapter = new FriendAdapter(getSupportFragmentManager(), tlFriend.getTabCount());
        vpFriend.setAdapter(friendAdapter);
        vpFriend.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlFriend));
        tlFriend.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpFriend.setCurrentItem(tab.getPosition());
                friendAdapter.notifyDataSetChanged();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
