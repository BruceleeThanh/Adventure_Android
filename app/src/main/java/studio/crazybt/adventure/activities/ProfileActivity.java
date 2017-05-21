package studio.crazybt.adventure.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.ProfileFragment;
import studio.crazybt.adventure.libs.CommonConstants;

public class ProfileActivity extends AppCompatActivity {

    private boolean isDefaultUser = false;

    public static Intent newInstance(Context context, String idUser, String username) {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra(CommonConstants.KEY_ID_USER, idUser);
        intent.putExtra(CommonConstants.KEY_USERNAME, username);
        return intent;
    }

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

        ProfileFragment profileFragment = ProfileFragment.newInstance(idUser, username);
        FragmentController.replaceFragment(this, R.id.rlProfile, profileFragment);

        isDefaultUser = idUser.equals(CommonConstants.VAL_ID_DEFAULT);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }else if(id == R.id.itemIconRightToolbar){
            if(isDefaultUser){
                this.editProfileClick();
            }else{

            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_profile, menu);
        if(isDefaultUser){
            MenuItem item = menu.findItem(R.id.itemIconRightToolbar);
            item.setTitle(R.string.edit_profile_info);
            item.setIcon(R.drawable.ic_mode_edit_white_24dp);
        }
        return true;
    }

    private void editProfileClick(){
        startActivity(InputActivity.newInstance(getBaseContext(), CommonConstants.ACT_EDIT_PROFILE_INFO));
    }
}
