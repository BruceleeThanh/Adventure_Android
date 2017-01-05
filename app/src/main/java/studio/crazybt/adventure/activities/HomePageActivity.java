package studio.crazybt.adventure.activities;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TabLayoutAdapter;
import studio.crazybt.adventure.fragments.TabFriendHomePageFragment;
import studio.crazybt.adventure.fragments.TabNewfeedHomePageFragment;
import studio.crazybt.adventure.fragments.TabNotificationsHomePageFragment;
import studio.crazybt.adventure.fragments.TabPublicTripsHomePageFragment;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.utils.BadgeTabLayout;
import studio.crazybt.adventure.utils.SharedPref;

public class HomePageActivity extends AppCompatActivity {

    private BadgeTabLayout tlHomePage;
    private Toolbar toolbar;
    private NavigationView navView;
    public DrawerLayout dlHomePage;
    private View navHeader;
    private LinearLayout llUserCover;
    private ImageView ivUserAvatar;
    private TextView tvUserName;

    private static final int MESSAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = (Toolbar) findViewById(R.id.tbHomePage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        this.setNavigationDrawer();
        this.setTablayout();
    }

    public void setNavigationDrawer() {
        navView = (NavigationView) findViewById(R.id.navView);
        // nav header set item
        navHeader = navView.getHeaderView(0);
        llUserCover = (LinearLayout) navHeader.findViewById(R.id.llUserCover);
        ivUserAvatar = (ImageView) navHeader.findViewById(R.id.ivUserAvatar);
        tvUserName = (TextView) navHeader.findViewById(R.id.tvUserName);
        final String userName = SharedPref.getInstance(this).getString(ApiConstants.KEY_FIRST_NAME, "") + " " + SharedPref.getInstance(this).getString(ApiConstants.KEY_LAST_NAME, "");
        tvUserName.setText(userName);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                dlHomePage.closeDrawers();
                // Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    // Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.itemProfile:
                        Intent intent = new Intent(HomePageActivity.this, ProfileActivity.class);
                        intent.putExtra(CommonConstants.KEY_ID_USER, CommonConstants.VAL_ID_DEFAULT);
                        intent.putExtra(CommonConstants.KEY_USERNAME, userName);
                        startActivity(intent);
                        return true;
                    case R.id.itemLogout:
                        SharedPref.getInstance(getBaseContext()).putString(ApiConstants.KEY_TOKEN, "");
                        Intent intent1 = new Intent(HomePageActivity.this, SplashActivity.class);
                        startActivity(intent1);
                        finish();
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }

            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        dlHomePage = (DrawerLayout) findViewById(R.id.dlHomePage);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, dlHomePage, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        dlHomePage.addDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    public void setTablayout() {
        final int tabSelectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary);
        final int tabUnselectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary_background_content);
        tlHomePage = (BadgeTabLayout) findViewById(R.id.tlHomePage);
        tlHomePage.setTabGravity(TabLayout.GRAVITY_FILL);

        ViewPager vpHomePage = (ViewPager) findViewById(R.id.vpHomePage);
        TabLayoutAdapter tabLayoutAdapter = new TabLayoutAdapter(getSupportFragmentManager());
        tabLayoutAdapter.addFragment(new TabPublicTripsHomePageFragment());
        tabLayoutAdapter.addFragment(new TabNewfeedHomePageFragment());
        tabLayoutAdapter.addFragment(new TabFriendHomePageFragment());
        TabNotificationsHomePageFragment tabNoti = new TabNotificationsHomePageFragment();
        tabLayoutAdapter.addFragment(tabNoti);
        vpHomePage.setAdapter(tabLayoutAdapter);
        vpHomePage.setOffscreenPageLimit(3);
        tlHomePage.setupWithViewPager(vpHomePage);
        tlHomePage.with(0).noBadge().icon(R.drawable.ic_public_gray_24dp).build();
        tlHomePage.with(1).noBadge().icon(R.drawable.ic_view_list_gray_24dp).build();
        tlHomePage.with(2).noBadge().icon(R.drawable.ic_group_gray_24dp).build();
        tlHomePage.with(3).noBadge().icon(R.drawable.ic_notifications_gray_24dp).build();
        tlHomePage.with(0).noBadge().iconColor(tabSelectedIconColor).build();
        vpHomePage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlHomePage));
        tlHomePage.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tlHomePage.with(tab).noBadge().iconColor(tabSelectedIconColor).build();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tlHomePage.with(tab).iconColor(tabUnselectedIconColor).build();
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        tabNoti.setOnBadgeNotificationRefreshListener(new TabNotificationsHomePageFragment.OnBadgeNotificationRefresh() {
            @Override
            public void onBadgeNotificationChange(int notiCount) {
                if (notiCount > 0) {
                    tlHomePage.with(3).hasBadge().badgeCount(notiCount).build();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemMessage:
                Intent intent = new Intent(this, MessageActivity.class);
                intent.putExtra("TYPE_SHOW", MESSAGE);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
