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
import studio.crazybt.adventure.fragments.TabAllFriendFragment;
import studio.crazybt.adventure.fragments.TabFollowersFriendFragment;
import studio.crazybt.adventure.fragments.TabFollowingsFriendFragment;
import studio.crazybt.adventure.fragments.TabRequestsFriendFragment;
import studio.crazybt.adventure.fragments.TabSuggestionsFriendFragment;

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
        tlFriend.setTabGravity(TabLayout.GRAVITY_FILL);
        ViewPager vpFriend = (ViewPager) findViewById(R.id.vpFriend);
        TabLayoutAdapter tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(new TabRequestsFriendFragment(), getResources().getString(R.string.request_tablayout_friend));
        tabLayoutAdapter.addFragment(new TabSuggestionsFriendFragment(), getResources().getString(R.string.suggestion_tablayout_friend));
        tabLayoutAdapter.addFragment(new TabAllFriendFragment(), getResources().getString(R.string.all_tablayout_friend));
        tabLayoutAdapter.addFragment(new TabFollowingsFriendFragment(), getResources().getString(R.string.following_tablayout_friend));
        tabLayoutAdapter.addFragment(new TabFollowersFriendFragment(), getResources().getString(R.string.follower_tablayout_friend));
        vpFriend.setAdapter(tabLayoutAdapter);
        tlFriend.setupWithViewPager(vpFriend);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_friend, menu);
        return true;
    }
}
