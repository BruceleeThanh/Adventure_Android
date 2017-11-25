package studio.crazybt.adventure.activities;

import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import io.socket.client.Socket;

import org.json.JSONObject;

import io.realm.Realm;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.adapters.TabLayoutAdapter;
import studio.crazybt.adventure.fragments.TabFriendHomePageFragment;
import studio.crazybt.adventure.fragments.TabNewfeedHomePageFragment;
import studio.crazybt.adventure.fragments.TabNotificationsHomePageFragment;
import studio.crazybt.adventure.fragments.TabPublicTripsHomePageFragment;
import studio.crazybt.adventure.helpers.PicassoHelper;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.ApiParams;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.models.User;
import studio.crazybt.adventure.services.AdventureRequest;
import studio.crazybt.adventure.utils.BadgeTabLayout;
import studio.crazybt.adventure.utils.RLog;
import studio.crazybt.adventure.utils.RealmUtils;
import studio.crazybt.adventure.utils.SharedPref;
import studio.crazybt.adventure.utils.StringUtil;
import studio.crazybt.adventure.utils.ToastUtil;

public class HomePageActivity extends AppCompatActivity {

    private BadgeTabLayout tlHomePage;
    private Toolbar toolbar;
    private NavigationView navView;
    public DrawerLayout dlHomePage;
    private View navHeader;
    private ImageView ivUserCover;
    private ImageView ivUserAvatar;
    private TextView tvUserName;
    private Realm realm;
    private String idUser;

    private final int MESSAGE = 1;

    private static int notificationCount = 0;

    private Socket mSocket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        initControls();
        initEvents();
        initActionBar();
        initNavigationDrawer();
        initTablayout();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mSocket = ((RealmUtils) getApplication()).getSocket();
        mSocket.connect();
        mSocket.emit(ApiConstants.SOCKET_USER_ONLINE, idUser);
    }

    private void initControls() {
        idUser = SharedPref.getInstance(getBaseContext()).getString(ApiConstants.KEY_ID, null);
        realm = Realm.getDefaultInstance();

        navView = (NavigationView) findViewById(R.id.navView);
        // nav header set item
        navHeader = navView.getHeaderView(0);
        ivUserCover = (ImageView) navHeader.findViewById(R.id.ivUserCover);
        ivUserAvatar = (ImageView) navHeader.findViewById(R.id.ivUserAvatar);
        tvUserName = (TextView) navHeader.findViewById(R.id.tvUserName);
    }

    private void initEvents() {
        ivUserAvatar.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                loadEditApiRoot();
                return false;
            }
        });
    }

    private void initActionBar() {
        toolbar = (Toolbar) findViewById(R.id.tbHomePage);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initNavigationDrawer() {

        User storageUser = realm.where(User.class).equalTo("id", SharedPref.getInstance(this).getString(ApiConstants.KEY_ID, "")).findFirst();
        PicassoHelper.execPicasso_CoverImage(getBaseContext(), storageUser.getCover(), ivUserCover);
        PicassoHelper.execPicasso_ProfileImage(getBaseContext(), storageUser.getAvatar(), ivUserAvatar);
        final String userName = storageUser.getFullName();
        tvUserName.setText(userName);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                // Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                //Closing drawer on item click
                dlHomePage.closeDrawers();

                int itemId = menuItem.getItemId();
                if (itemId == R.id.itemProfile) {
                    startActivity(ProfileActivity.newInstance(getBaseContext(), CommonConstants.VAL_ID_DEFAULT, userName));
                } else if (itemId == R.id.itemTrip) {
                    startActivity(TripListActivity.newInstance(getBaseContext()));
                } else if (itemId == R.id.itemLogout) {
                    SharedPref.getInstance(getBaseContext()).putString(ApiConstants.KEY_TOKEN, "");
                    Intent intent1 = new Intent(HomePageActivity.this, SplashActivity.class);
                    startActivity(intent1);
                    finish();
                } else if (itemId == R.id.itemGroup) {
                    startActivity(GroupActivity.newInstance(getBaseContext()));
                }
                return true;
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

    private void initTablayout() {
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
                RLog.i("Tab at: " + tab.getPosition());
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
                    notificationCount = notiCount;
                    tlHomePage.with(3).hasBadge().badgeCount(notiCount).build();
                }
            }
        });
    }

    private void tabSelectedAction(int position) {
        if (position == 3) {
            if (notificationCount > 0) {
                String token = SharedPref.getInstance(this).getString(ApiConstants.KEY_TOKEN, "");
                String url = ApiConstants.getUrl(ApiConstants.API_VIEWED_NOTIFICATION);
                ApiParams params = ApiParams.getBuilder();
                params.put(ApiConstants.KEY_TOKEN, token);
                AdventureRequest adventureRequest = new AdventureRequest(this, Request.Method.POST, url, params, false);
                adventureRequest.setOnAdventureRequestListener(new AdventureRequest.OnAdventureRequestListener() {
                    @Override
                    public void onAdventureResponse(JSONObject response) {

                    }

                    @Override
                    public void onAdventureError(int errorCode, String errorMsg) {

                    }
                });
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu_home_page, menu);
        menu.findItem(R.id.itemMessage).getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(MessageActivity.newInstance(getBaseContext(), CommonConstants.ACT_VIEW_CONVERSATION));
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
    }

    private void loadEditApiRoot() {
        LayoutInflater li = LayoutInflater.from(HomePageActivity.this);
        View dialogView = li.inflate(R.layout.dialog_edit_api_root_url, null);
        final Dialog dialog = new Dialog(HomePageActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(dialogView);
        dialog.setCanceledOnTouchOutside(true);
        final EditText etApiRoot = (EditText) dialog.findViewById(R.id.etApiRoot);
        final EditText etApiRootImages = (EditText) dialog.findViewById(R.id.etApiRootImages);
        Button btnCancelEdit = (Button) dialog.findViewById(R.id.btnCancelEdit);
        Button btnConfirmEdit = (Button) dialog.findViewById(R.id.btnConfirmEdit);

        etApiRoot.setText(ApiConstants.getApiRoot());
        etApiRootImages.setText(ApiConstants.getApiRootImages());

        btnCancelEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnConfirmEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ApiConstants.setApiRoot(StringUtil.getText(etApiRoot));
                ApiConstants.setApiRootImages(StringUtil.getText(etApiRootImages));
                dialog.dismiss();
                ToastUtil.showToast(getBaseContext(), "Đã lưu url.");
            }
        });
        dialog.show();
    }
}
