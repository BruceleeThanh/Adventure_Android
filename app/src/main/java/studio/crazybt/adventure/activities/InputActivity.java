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
import studio.crazybt.adventure.fragments.ProfileBasicInfoFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.libs.CommonConstants;

public class InputActivity extends AppCompatActivity {

    private static int typeShow = 0;
    private CreateTripFragment createTripFragment;

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

        initControls();
        loadInstance();
    }

    private void loadInstance(){
        Intent intent = getIntent();
        typeShow = intent.getIntExtra(CommonConstants.KEY_TYPE_SHOW, typeShow);
        if (typeShow == CommonConstants.ACT_CREATE_STATUS) { // create normal status
            initCreateStatus();
        } else if (typeShow == CommonConstants.ACT_CREATE_TRIP) { // create normal trip
            initCreateTrip();
        } else if (typeShow == CommonConstants.ACT_CREATE_DIARY_TRIP) { // create diary trip
            String idTrip = intent.getStringExtra(ApiConstants.KEY_ID_TRIP);
            initCreateDiaryTrip(idTrip);
        } else if (typeShow == CommonConstants.ACT_CREATE_DISCUSS_TRIP) { // create discuss trip - status in trip
            String idTrip = intent.getStringExtra(ApiConstants.KEY_ID_TRIP);
            initCreateDiscussTrip(idTrip);
        } else if (typeShow == CommonConstants.ACT_EDIT_PROFILE_INFO) { // edit profile info
            initEditProfileInfo();
        } else if(typeShow == CommonConstants.ACT_VIEW_PROFILE_INFO){ // view profile info
            //String idUser = intent.getStringExtra(ApiConstants.KEY_ID_USER);
            //initViewProfileBasicInfo(idUser);
        } else if(typeShow == CommonConstants.ACT_CREATE_GROUP){ // create group
            initCreateGroup();
        } else if(typeShow == CommonConstants.ACT_CREATE_STATUS_GROUP) { // create status in group
            String idGroup = intent.getStringExtra(ApiConstants.KEY_ID_GROUP);
            initCreateStatusGroup(idGroup);
        }  else if(typeShow == CommonConstants.ACT_CREATE_TRIP_GROUP) { // create status in group
            String idGroup = intent.getStringExtra(ApiConstants.KEY_ID_GROUP);
            initCreateTripGroup(idGroup);
        }
    }

    private void initControls(){
        ButterKnife.bind(this);
    }

    private void initCreateStatus() {
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, CreateStatusFragment.newInstance());
    }

    private void initCreateTrip() {
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, CreateTripFragment.newInstance());
    }

    private void initCreateDiaryTrip(String idTrip) {
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, CreateDiaryTripFragment.newInstance(idTrip));
    }

    private void initCreateDiscussTrip(String idTrip) {
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, CreateStatusFragment.newInstance_Trip(idTrip));
    }

    private void initEditProfileInfo() {
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, EditProfileInfoFragment.newInstance(CommonConstants.ACT_EDIT_PROFILE_INFO, CommonConstants.VAL_ID_DEFAULT));
    }

    private void initViewProfileBasicInfo(String idUser){
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, ProfileBasicInfoFragment.newInstance(idUser));
    }

    private void initCreateGroup(){
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, CreateGroupFragment.newInstance());
    }

    private void initCreateStatusGroup(String idGroup){
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, CreateStatusFragment.newInstance_Group(idGroup));
    }

    private void initCreateTripGroup(String idGroup){
        FragmentController.replaceFragment_BackStack(this, R.id.rlInput, CreateTripFragment.newInstance_Group(idGroup));
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
