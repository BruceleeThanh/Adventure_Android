package studio.crazybt.adventure.activities;

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
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.ApiConstants;
import studio.crazybt.adventure.services.AdventureRequest;

public class InputActivity extends AppCompatActivity {

    private static int typeShow = 0;
    private FragmentController fragmentController;
    private CreateStatusFragment createStatusFragment;
    private CreateTripFragment createTripFragment;
    private CreateDiaryTripFragment createDiaryTripFragment;

    @BindView(R.id.tbInput)
    Toolbar tbInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);
        ButterKnife.bind(this);
        setSupportActionBar(tbInput);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        typeShow = intent.getIntExtra("TYPE_SHOW", typeShow);
        if (typeShow == 1) {
            initCreateStatus();
        } else if (typeShow == 2) {
            initCreateTrip();
        } else if (typeShow == 3) {
            String idTrip = intent.getStringExtra(ApiConstants.KEY_ID_TRIP);
            initCreateDiaryTrip(idTrip);
        }
    }

    private void initCreateStatus() {
        createStatusFragment = new CreateStatusFragment();
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_status));
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack(R.id.rlInput, createStatusFragment);
        fragmentController.commit();
    }

    private void initCreateTrip() {
        createTripFragment = new CreateTripFragment();
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_trip));
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack(R.id.rlInput, createTripFragment);
        fragmentController.commit();
    }

    private void initCreateDiaryTrip(String idTrip) {
        Bundle bundle =new Bundle();
        bundle.putString(ApiConstants.KEY_ID_TRIP, idTrip);
        createDiaryTripFragment = new CreateDiaryTripFragment();
        createDiaryTripFragment.setArguments(bundle);
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_diary_trip));
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack(R.id.rlInput, createDiaryTripFragment);
        fragmentController.commit();
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.itemPost:
                if (typeShow == 1) {
                    item.setEnabled(false);
                    createStatusFragment.uploadStatus();
                } else if (typeShow == 2) {
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
                } else if (typeShow == 3) {
                    createDiaryTripFragment.uploadDiary();
                    if(createDiaryTripFragment.getAdventureRequest() != null){
                        item.setEnabled(false);
                        createDiaryTripFragment.getAdventureRequest().setOnNotifyResponseReceived(new AdventureRequest.OnNotifyResponseReceived() {
                            @Override
                            public void onNotify() {
                                item.setEnabled(true);
                            }
                        });
                    }
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
