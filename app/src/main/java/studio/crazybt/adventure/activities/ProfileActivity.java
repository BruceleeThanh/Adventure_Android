package studio.crazybt.adventure.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import studio.crazybt.adventure.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.ProfileFragment;

public class ProfileActivity extends AppCompatActivity {

    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment(R.id.rlProfile, new ProfileFragment());
        fragmentController.commit();
    }
}
