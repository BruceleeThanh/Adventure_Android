package studio.crazybt.adventure.activities;

import android.graphics.PorterDuff;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;

import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.HomePageAdapter;

public class HomePageActivity extends AppCompatActivity {

    private TabLayout tlHomePage;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        toolbar = (Toolbar) findViewById(R.id.tbHomePage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tlHomePage = (TabLayout) findViewById(R.id.tlHomePage);
        tlHomePage.addTab(tlHomePage.newTab().setIcon(R.drawable.ic_public_gray_24dp));
        tlHomePage.addTab(tlHomePage.newTab().setIcon(R.drawable.ic_view_list_gray_24dp));
        tlHomePage.addTab(tlHomePage.newTab().setIcon(R.drawable.ic_group_gray_24dp));
        tlHomePage.addTab(tlHomePage.newTab().setIcon(R.drawable.ic_notifications_gray_24dp));
        tlHomePage.setTabGravity(TabLayout.GRAVITY_FILL);
        final int tabSelectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary);
        final int tabUnselectedIconColor = ContextCompat.getColor(this.getBaseContext(), R.color.primary_background_content);
        tlHomePage.getTabAt(0).getIcon().setColorFilter(tabSelectedIconColor, PorterDuff.Mode.SRC_IN);

        final ViewPager vpHomePage = (ViewPager) findViewById(R.id.vpHomePage);
        vpHomePage.setOffscreenPageLimit(3);
        final HomePageAdapter homePageAdapter = new HomePageAdapter(getSupportFragmentManager(), tlHomePage.getTabCount());
        vpHomePage.setAdapter(homePageAdapter);
        vpHomePage.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tlHomePage));
        tlHomePage.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int temp = tab.getPosition();
                vpHomePage.setCurrentItem(tab.getPosition());
                tab.getIcon().setColorFilter(tabSelectedIconColor, PorterDuff.Mode.SRC_IN);
                homePageAdapter.notifyDataSetChanged();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setColorFilter(tabUnselectedIconColor, PorterDuff.Mode.SRC_IN);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_home_page, menu);
        return true;
    }
}
