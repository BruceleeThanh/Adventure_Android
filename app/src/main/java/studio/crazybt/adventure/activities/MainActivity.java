package studio.crazybt.adventure.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import studio.crazybt.adventure.FragmentController;
import studio.crazybt.adventure.R;
import studio.crazybt.adventure.fragments.SplashFragment;

public class MainActivity extends AppCompatActivity {

    private FragmentController fragmentController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        fragmentController = new FragmentController(this);
        fragmentController.addFragment_BackStack(R.id.rlMain, new SplashFragment());
        fragmentController.commit();
    }

    @Override
    public void onBackPressed() {
        fragmentController = new FragmentController(this);
        fragmentController.removeAll();
        fragmentController.addFragment(R.id.rlMain, new SplashFragment());
        fragmentController.commit();
    }
}
