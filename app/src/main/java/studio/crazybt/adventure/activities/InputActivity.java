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
import studio.crazybt.adventure.fragments.CreateStatusFragment;
import studio.crazybt.adventure.helpers.FragmentController;

public class InputActivity extends AppCompatActivity {

    private static int typeShow = 0;
    private FragmentController fragmentController;
    private CreateStatusFragment createStatusFragment;

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
        switch (typeShow){
            case 1:
                this.showCreateStatus();
                break;
            case 2:

                break;
            default:
                break;
        }
    }

    private void showCreateStatus(){
        createStatusFragment = new CreateStatusFragment();
        getSupportActionBar().setTitle(getResources().getString(R.string.title_tb_create_status));
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack(R.id.rlInput, createStatusFragment);
        fragmentController.commit();
    }



    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.itemPost:
                item.setEnabled(false);
                createStatusFragment.uploadStatus();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
