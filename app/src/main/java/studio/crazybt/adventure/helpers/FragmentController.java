package studio.crazybt.adventure.helpers;

import android.app.Activity;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import studio.crazybt.adventure.R;

/**
 * Created by Brucelee Thanh on 26/08/2016.
 */
public class FragmentController {

//    public void addFragment(int id, Object obj){
//        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName());
//    }
    public static void replaceFragment(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void replaceFragment_Animation(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName());
        fragmentTransaction.commit();
    }

//    public void addFragment_BackStack(int id, Object obj){
//        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName()).addToBackStack(obj.getClass().getName());
//    }
    public static void replaceFragment_BackStack(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName()).addToBackStack(obj.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void replaceFragment_BackStack_Animation(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.replace(id, (Fragment) obj, obj.getClass().getName()).addToBackStack(obj.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void addFragment(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id, (Fragment) obj, obj.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void addFragment_BackStack(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(id, (Fragment) obj, obj.getClass().getName()).addToBackStack(obj.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void addFragment_Animation(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(id, (Fragment) obj, obj.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void addFragment_BackStack_Animation(FragmentActivity fragmentActivity, int id, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left, R.anim.enter_from_left, R.anim.exit_to_right);
        fragmentTransaction.add(id, (Fragment) obj, obj.getClass().getName()).addToBackStack(obj.getClass().getName());
        fragmentTransaction.commit();
    }

    public static void removeFragment(FragmentActivity fragmentActivity, Object obj){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove((Fragment) obj);
        fragmentTransaction.commit();
    }

    public static void removeAll(FragmentActivity fragmentActivity){
        FragmentManager fragmentManager = fragmentActivity.getSupportFragmentManager();
        for (int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i) {
            fragmentManager.popBackStack();
        }
    }
}
