package studio.crazybt.adventure.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.ProfileFragment;
import studio.crazybt.adventure.libs.CommonConstants;

public class ProfileActivity extends AppCompatActivity {

    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        String idUser = CommonConstants.VAL_ID_DEFAULT;
        String username = CommonConstants.VAL_USERNAME_DEFAUT;
        if (getIntent().getStringExtra("ID_USER") != null) {
            idUser = getIntent().getStringExtra("ID_USER").toString();
        }
        if (getIntent().getStringExtra("USERNAME") != null) {
            username = getIntent().getStringExtra("USERNAME").toString();
        }
        Bundle bundle = new Bundle();
        bundle.putString(CommonConstants.KEY_ID_USER, idUser);
        bundle.putString(CommonConstants.KEY_USERNAME, username);
        ProfileFragment profileFragment = new ProfileFragment();
        profileFragment.setArguments(bundle);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment(R.id.rlProfile, profileFragment);
        fragmentController.commit();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_profile, menu);
        return true;
    }
}
