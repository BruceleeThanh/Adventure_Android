package studio.crazybt.adventure.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.MessageFragment;
import studio.crazybt.adventure.helpers.FragmentController;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.tbMessage)
    Toolbar tbMessage;
    private static int typeShow = 0;
    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        setSupportActionBar(tbMessage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.message_btn_profile));
        Intent intent = getIntent();
        typeShow = intent.getIntExtra("TYPE_SHOW", typeShow);
        switch (typeShow){
            case 1:
                this.showMessage();
                break;
            case 2:
                break;
        }
    }

    private void showMessage(){
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack_Animation(R.id.rlMessage, new MessageFragment());
        fragmentController.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_message, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
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
}
