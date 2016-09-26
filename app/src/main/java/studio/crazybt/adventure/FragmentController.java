package studio.crazybt.adventure;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class FragmentController {

    public FragmentManager fragmentManager;
    public FragmentTransaction fragmentTransaction;

    public FragmentController(AppCompatActivity activity){
        fragmentManager = activity.getSupportFragmentManager();
        this.beginTransaction();
    }

    public void beginTransaction(){
        fragmentTransaction = fragmentManager.beginTransaction();
    }

    public void setCustomAnimations(){
        fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
    }

    public void addFragment(int id, Object obj){
        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName());
    }

    public void addFragment_BackStack(int id, Object obj){
        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName()).addToBackStack(obj.getClass().getName());
    }

    public void removeFragment(Object obj){
        fragmentTransaction.remove((Fragment) obj);
    }

    public void commit(){
        fragmentTransaction.commit();
    }

    public void removeAll(){
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }
}
