package com.grietenenknapen.sithandroid.util;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.grietenenknapen.sithandroid.R;

/**
 * Created by tristan.
 * <p/>
 * This class handles all the basic fragment operations
 */
public class FragmentUtils {
    public static final int ANIMATE_SLIDE_LEFT = 1;
    public static final int ANIMATE_SLIDE_RIGHT = 2;
    public static final int ANIMATE_SLIDE_UP = 3;
    public static final int ANIMATE_SLIDE_DOWN = 4;
    public static final int ANIMATE_NONE = 0;

    /**
     * Adds ore replaces a fragment to the given activity. The fragment will be added to the container.
     *
     * @param activity    target activity
     * @param fClass      class of the fragment
     * @param containerId id of the target container
     * @param tag         tag to identify the fragment
     */
    public static void replaceOrAddFragment(AppCompatActivity activity, Class<? extends Fragment> fClass, int containerId, String tag, boolean addToBackStack) {
        handleFragmentTransaction(activity, fClass, containerId, tag, FragmentUtils.ANIMATE_NONE, null, addToBackStack);
    }

    /**
     * Adds ore replaces a fragment to the given activity. The fragment will be added to the container.
     *
     * @param activity    target activity
     * @param fClass      class of the fragment
     * @param containerId id of the target container
     * @param tag         tag to identify the fragment
     * @param animation   type of animation
     */
    public static void replaceOrAddFragment(AppCompatActivity activity, Class<? extends Fragment> fClass, int containerId, String tag, int animation, boolean addToBackStack) {
        handleFragmentTransaction(activity, fClass, containerId, tag, animation, null, addToBackStack);
    }

    /**
     * Adds ore replaces a fragment to the given activity. The fragment will be added to the container.
     *
     * @param activity    target activity
     * @param fClass      class of the fragment
     * @param containerId id of the target container
     * @param tag         tag to identify the fragment
     * @param animation   type of animation
     * @param args        extra args to be submitted to the fragment
     */
    public static void replaceOrAddFragment(AppCompatActivity activity, Class<? extends Fragment> fClass, int containerId, String tag, int animation, Bundle args, boolean addToBackStack) {
        handleFragmentTransaction(activity, fClass, containerId, tag, animation, args, addToBackStack);
    }


    private static void handleFragmentTransaction(AppCompatActivity activity, Class<? extends Fragment> fClass, int containerId, String tag, int animation, Bundle args, boolean addToBackStack) {
        // Initialize fragment transaction
        FragmentManager fm = activity.getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        // Check if the fragment is already initialized
        Fragment fragment = fm.findFragmentByTag(tag);
        if (fragment == null || fragment.isRemoving()) {
            setAnimation(ft, animation);
            // If not, instantiate and add it to the activity
            if (args == null) {
                fragment = Fragment.instantiate(activity, fClass.getName());
            } else {
                fragment = Fragment.instantiate(activity, fClass.getName(), args);
            }
            if (addToBackStack) {
                ft.replace(containerId, fragment, tag).addToBackStack(fClass.getName()).commit();
            } else {
                ft.replace(containerId, fragment, tag).commit();
            }
        } else {
            // If it exists, simply attach it in order to show it
            if (fragment.isDetached()) {
                ft.attach(fragment).commit();
            }
        }
    }

    public static void showDialogFragment(AppCompatActivity activity, Class<? extends DialogFragment> f, String tag, Bundle args) {
        final FragmentTransaction ft = activity.getSupportFragmentManager().beginTransaction();
        final Fragment prev = activity.getSupportFragmentManager().findFragmentByTag(
                tag);
        if (prev != null) {
            ((DialogFragment) prev).dismiss();
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        final DialogFragment newFragment = (DialogFragment) DialogFragment.instantiate(activity, f.getName());
        if (args != null) {
            newFragment.setArguments(args);
        }
        newFragment.show(activity.getSupportFragmentManager(), tag);
    }


    public static void setAnimation(FragmentTransaction ft, int animation) {
        switch (animation) {
            case FragmentUtils.ANIMATE_SLIDE_LEFT:
                ft.setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;
            case FragmentUtils.ANIMATE_SLIDE_RIGHT:
                ft.setCustomAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case FragmentUtils.ANIMATE_SLIDE_DOWN:
                ft.setCustomAnimations(
                        R.anim.slide_in_up,
                        R.anim.slide_out_down,
                        R.anim.slide_in_down,
                        R.anim.slide_out_up);
                break;
            case FragmentUtils.ANIMATE_SLIDE_UP:
                ft.setCustomAnimations(
                        R.anim.slide_in_down,
                        R.anim.slide_out_up,
                        R.anim.slide_in_up,
                        R.anim.slide_out_down);
                break;
            case FragmentUtils.ANIMATE_NONE:
                break;

        }
    }

    public static boolean validateFragment(Fragment fragment) {
        return fragment != null && fragment.isAdded() && !fragment.isRemoving()
                && fragment.getView() != null && fragment.getActivity() != null;
    }

    public static Fragment findFragmentByTag(FragmentManager manager, String tag) {
        return manager.findFragmentByTag(tag);
    }

    public static void clearFragmentBackStack(FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
