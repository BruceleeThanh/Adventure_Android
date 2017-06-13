package studio.crazybt.adventure.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.CreateDiaryTripFragment;
import studio.crazybt.adventure.fragments.CreateGroupFragment;
import studio.crazybt.adventure.fragments.CreateStatusFragment;
import studio.crazybt.adventure.fragments.CreateTripFragment;
import studio.crazybt.adventure.fragments.EditProfileInfoFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;

public class InputActivity extends AppCompatActivity {

    private static int typeShow = 0;
    private CreateStatusFragment createStatusFragment;
    private CreateTripFragment createTripFragment;
    private CreateDiaryTripFragment createDiaryTripFragment;
    private EditProfileInfoFragment editProfileInfoFragment;
    private CreateGroupFragment createGroupFragment;

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

    public static Intent newInstance_ForGroup(Context context, int typeShow, String idGroup) {
        Intent intent = new Intent(context, InputActivity.class);
        intent.putExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        intent.putExtra(ApiConstants.KEY_ID_GROUP, idGroup);
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

        } else if(typeShow == CommonConstants.ACT_CREATE_GROUP){
            initCreateGroup();
        }
    }

    private void initCreateStatus() {
        createStatusFragment = new CreateStatusFragment();
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createStatusFragment);
    }

    private void initCreateTrip() {
        createTripFragment = new CreateTripFragment();
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createTripFragment);
    }

    private void initCreateDiaryTrip(String idTrip) {
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstants.KEY_ID_TRIP, idTrip);
        createDiaryTripFragment = new CreateDiaryTripFragment();
        createDiaryTripFragment.setArguments(bundle);
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createDiaryTripFragment);
    }

    private void initCreateDiscussTrip(String idTrip) {
        Bundle bundle = new Bundle();
        bundle.putString(ApiConstants.KEY_ID_TRIP, idTrip);
        createStatusFragment = new CreateStatusFragment();
        createStatusFragment.setArguments(bundle);
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createStatusFragment);
    }

    private void initEditProfileInfo() {
        editProfileInfoFragment = EditProfileInfoFragment.newInstance(CommonConstants.ACT_EDIT_PROFILE_INFO, CommonConstants.VAL_ID_DEFAULT);
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, editProfileInfoFragment);
    }

    private void initCreateGroup(){
        createGroupFragment = new CreateGroupFragment();
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, createGroupFragment);
    }

    private void initCreateStatusGroup(String idGroup){

    }

    private void initCreateTripGroup(String idGroup){

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.cancel_create_post)
                    .setMessage(R.string.msg_confirm_cancel_create_post)
                    .setPositiveButton(R.string.confirm_cancel_create_post, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.reject_cancel_create_post, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .show();
        } else {
            super.onBackPressed();
        }
    }
}
