package studio.crazybt.adventure.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.CreateDiaryTripFragment;
import studio.crazybt.adventure.fragments.CreateStatusFragment;
import studio.crazybt.adventure.fragments.CreateTripFragment;
import studio.crazybt.adventure.fragments.EditProfileInfoFragment;
import studio.crazybt.adventure.fragments.ProfileBasicInfoFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;
import studio.crazybt.adventure.services.AdventureRequest;

public class InputActivity extends AppCompatActivity {

    private static int typeShow = 0;
    private CreateStatusFragment createStatusFragment;
    private CreateTripFragment createTripFragment;
    private CreateDiaryTripFragment createDiaryTripFragment;
    private EditProfileInfoFragment editProfileInfoFragment;

    @BindView(R.id.tbInput)
    Toolbar tbInput;

    public static Intent newInstance(Context context, int typeShow) {
        Intent intent = new Intent(context, InputActivity.class);
        intent.putExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        return intent;
    }

    public static Intent newInstance_ForTrip(Context context, int typeShow, String idTrip) {
        Intent intent = new Intent(context, InputActivity.class);
        intent.putExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        intent.putExtra(ApiConstants.KEY_ID_TRIP, idTrip);
        return intent;
    }

    public static Intent newInstance_ForUser(Context context, int typeShow, String idUser) {
        Intent intent = new Intent(context, InputActivity.class);
        intent.putExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        intent.putExtra(CommonConstants.KEY_ID_USER, idUser);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        setSupportActionBar(tbInput);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        typeShow = intent.getIntExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        if (typeShow == CommonConstants.ACT_CREATE_STATUS) {
            initCreateStatus();
        } else if (typeShow == CommonConstants.ACT_CREATE_TRIP) {
            initCreateTrip();
        } else if (typeShow == CommonConstants.ACT_CREATE_DIARY_TRIP) {
            String idTrip = intent.getStringExtra(ApiConstants.KEY_ID_TRIP);
            initCreateDiaryTrip(idTrip);
        } else if (typeShow == CommonConstants.ACT_CREATE_DISCUSS_TRIP) {
            String idTrip = intent.getStringExtra(ApiConstants.KEY_ID_TRIP);
            initCreateDiscussTrip(idTrip);
        } else if (typeShow == CommonConstants.ACT_EDIT_PROFILE_INFO) {
            initEditProfileInfo();
        } else if(typeShow == CommonConstants.ACT_VIEW_PROFILE_INFO){

        }
    }

    private void initCreateStatus() {
        createStatusFragment = new CreateStatusFragment();
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_status));
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createStatusFragment);
    }

    private void initCreateTrip() {
        createTripFragment = new CreateTripFragment();
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_trip));
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createTripFragment);
    }

    private void initCreateDiaryTrip(String idTrip) {
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstants.KEY_ID_TRIP, idTrip);
        createDiaryTripFragment = new CreateDiaryTripFragment();
        createDiaryTripFragment.setArguments(bundle);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_diary_trip));
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createDiaryTripFragment);
    }

    private void initCreateDiscussTrip(String idTrip) {
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstants.KEY_ID_TRIP, idTrip);
        createStatusFragment = new CreateStatusFragment();
        createStatusFragment.setArguments(bundle);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_discuss_trip));
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createStatusFragment);
    }

    private void initEditProfileInfo() {
        getSupportActionBar().setTitle(getResources().getString(R.string.title_edit_profile_info));
        editProfileInfoFragment = EditProfileInfoFragment.newInstance(CommonConstants.ACT_EDIT_PROFILE_INFO, CommonConstants.VAL_ID_DEFAULT);
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, editProfileInfoFragment);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_input, menu);

        if (typeShow == CommonConstants.ACT_EDIT_PROFILE_INFO) {
            MenuItem menuItem = menu.findItem(R.id.itemPost);
            menuItem.setTitle(getResources().getString(R.string.update_btn_edit_profile_info));
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.itemPost:
                if (typeShow == CommonConstants.ACT_CREATE_STATUS) {
                    item.setEnabled(false);
                    createStatusFragment.uploadStatus();
                    createStatusFragment.getRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                        @Override
                        public void onNotify() {
                            item.setEnabled(true);
                        }
                    });
                } else if (typeShow == CommonConstants.ACT_CREATE_TRIP) {
                    createTripFragment.uploadTrip();
                    if (createTripFragment.getRequest() != null) {
                        item.setEnabled(false);
                        createTripFragment.getRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                            @Override
                            public void onNotify() {
                                item.setEnabled(true);
                            }
                        });
                    }
                } else if (typeShow == CommonConstants.ACT_CREATE_DIARY_TRIP) {
                    createDiaryTripFragment.uploadDiary();
                    if (createDiaryTripFragment.getAdventureRequest() != null) {
                        item.setEnabled(false);
                        createDiaryTripFragment.getAdventureRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                            @Override
                            public void onNotify() {
                                item.setEnabled(true);
                            }
                        });
                    }
                } else if (typeShow == CommonConstants.ACT_CREATE_DISCUSS_TRIP) {
                    item.setEnabled(false);
                    createStatusFragment.uploadStatus();
                    createStatusFragment.getRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                        @Override
                        public void onNotify() {
                            item.setEnabled(true);
                        }
                    });
                } else if(typeShow == CommonConstants.ACT_EDIT_PROFILE_INFO){
                    item.setEnabled(false);
                    editProfileInfoFragment.updateProfile();
                    editProfileInfoFragment.getAdventureRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                        @Override
                        public void onNotify() {
                            item.setEnabled(true);
                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
