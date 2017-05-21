package studio.crazybt.adventure.activities;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.MessageFragment;
import studio.crazybt.adventure.fragments.SearchUserFragment;
import studio.crazybt.adventure.helpers.FragmentController;

public class MessageActivity extends AppCompatActivity {

    @BindView(R.id.tbMessage)
    Toolbar tbMessage;
    private static int typeShow = 0;
    private SearchUserFragment searchUserFragment = null;

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
        FragmentController.replaceFragment_BackStack_Animation(this, R.id.rlMessage, new MessageFragment());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        searchUserFragment = SearchUserFragment.newInstance();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_menu_message, menu);
        MenuItem searchItem = menu.findItem(R.id.svSearchUser);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchUserFragment.loadData(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchUserFragment.loadData(newText);
                return false;
            }
        });


        // Define the listener
        MenuItemCompat.OnActionExpandListener expandListener = new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                FragmentController.removeFragment(MessageActivity.this, searchUserFragment);
                return true;  // Return true to collapse action view
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                FragmentController.addFragment_Animation(MessageActivity.this, R.id.rlMessage, searchUserFragment);
                return true;  // Return true to expand action view
            }
        };
        MenuItemCompat.setOnActionExpandListener(searchItem, expandListener);

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
