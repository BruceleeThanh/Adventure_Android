package studio.crazybt.adventure.activities;

import android.content.Context;
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
import studio.crazybt.adventure.fragments.ConversationFragment;
import studio.crazybt.adventure.fragments.MessageFragment;
import studio.crazybt.adventure.fragments.SearchUserMessageFragment;
import studio.crazybt.adventure.helpers.FragmentController;
import studio.crazybt.adventure.libs.CommonConstants;

public class MessageActivity extends AppCompatActivity {

    private int typeShow = 0;


    public static Intent newInstance(Context context, int typeShow) {
        Bundle args = new Bundle();
        args.putInt(CommonConstants.KEY_TYPE_SHOW, typeShow);
        Intent intent = new Intent(context, MessageActivity.class);
        intent.putExtra(CommonConstants.KEY_DATA, args);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        loadInstance();
        initControls();
    }

    private void loadInstance() {
        Intent intent = getIntent();
        if (intent.hasExtra(CommonConstants.KEY_DATA)) {
            Bundle bundle = intent.getBundleExtra(CommonConstants.KEY_DATA);
            typeShow = bundle.getInt(CommonConstants.KEY_TYPE_SHOW, typeShow);
            if (typeShow == CommonConstants.ACT_VIEW_CONVERSATION) {
                showConversation();
            } else if (typeShow == CommonConstants.ACT_VIEW_MESSAGE) {
                showMessage();
            }
        }

    }

    private void initControls() {
        ButterKnife.bind(this);
    }

    private void showMessage() {
        FragmentController.addFragment_Animation(this, R.id.rlMessageActivity, new MessageFragment());
    }

    private void showConversation() {
        FragmentController.addFragment_BackStack_Animation(this, R.id.rlMessageActivity, ConversationFragment.newInstance());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
