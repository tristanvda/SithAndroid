package com.grietenenknapen.sithandroid.util;

import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.grietenenknapen.sithandroid.R;
import com.grietenenknapen.sithandroid.game.usecase.FlowDetails;
import com.grietenenknapen.sithandroid.game.usecase.GameUseCase;
import com.grietenenknapen.sithandroid.game.usecase.UseCase;
import com.grietenenknapen.sithandroid.ui.fragments.gameflow.GameFlowFragment;

import java.lang.annotation.Retention;

import static com.grietenenknapen.sithandroid.util.FragmentUtils.Animation.ANIMATE_NONE;
import static com.grietenenknapen.sithandroid.util.FragmentUtils.Animation.ANIMATE_SLIDE_DOWN;
import static com.grietenenknapen.sithandroid.util.FragmentUtils.Animation.ANIMATE_SLIDE_LEFT;
import static com.grietenenknapen.sithandroid.util.FragmentUtils.Animation.ANIMATE_SLIDE_RIGHT;
import static com.grietenenknapen.sithandroid.util.FragmentUtils.Animation.ANIMATE_SLIDE_UP;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * Created by tristan.
 * <p/>
 * This class handles all the basic fragment operations
 */
public class FragmentUtils {

    @Retention(SOURCE)
    @IntDef({ANIMATE_SLIDE_LEFT, ANIMATE_SLIDE_RIGHT, ANIMATE_SLIDE_UP, ANIMATE_SLIDE_DOWN, ANIMATE_NONE})

    public @interface Animation {
        int ANIMATE_SLIDE_LEFT = 1;
        int ANIMATE_SLIDE_RIGHT = 2;
        int ANIMATE_SLIDE_UP = 3;
        int ANIMATE_SLIDE_DOWN = 4;
        int ANIMATE_NONE = 0;
    }

    /**
     * Adds ore replaces a fragment to the given activity. The fragment will be added to the container.
     *
     * @param activity    target activity
     * @param fClass      class of the fragment
     * @param containerId id of the target container
     * @param tag         tag to identify the fragment
     */
    public static void replaceOrAddFragment(final AppCompatActivity activity,
                                            final Class<? extends Fragment> fClass,
                                            final int containerId,
                                            final String tag,
                                            final boolean addToBackStack) {

        handleFragmentTransaction(activity, fClass, containerId, tag, ANIMATE_NONE, null, addToBackStack);
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
    public static void replaceOrAddFragment(final AppCompatActivity activity,
                                            final Class<? extends Fragment> fClass,
                                            final int containerId,
                                            final String tag,
                                            final int animation,
                                            final boolean addToBackStack) {

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
    public static void replaceOrAddFragment(final AppCompatActivity activity,
                                            final Class<? extends Fragment> fClass,
                                            final int containerId,
                                            final String tag,
                                            final int animation,
                                            final Bundle args,
                                            final boolean addToBackStack) {

        handleFragmentTransaction(activity, fClass, containerId, tag, animation, args, addToBackStack);
    }


    private static void handleFragmentTransaction(final AppCompatActivity activity,
                                                  final Class<? extends Fragment> fClass,
                                                  final int containerId,
                                                  final String tag,
                                                  final int animation,
                                                  final Bundle args,
                                                  final boolean addToBackStack) {

        // Initialize fragment transaction
        final FragmentManager fm = activity.getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();
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

    @SuppressWarnings("unchecked")
    public static <U extends UseCase, T extends Fragment & GameFlowFragment<U>>
    void handleGameFlowFragmentTransaction(final AppCompatActivity activity,
                                           final Class<T> fClass,
                                           final int containerId,
                                           final Bundle bundle,
                                           final FlowDetails flowDetails,
                                           final U gameUseCase,
                                           final int fragmentAnimation) {

        final FragmentManager fm = activity.getSupportFragmentManager();
        final Fragment fragment = fm.findFragmentById(R.id.container);

        final FragmentTransaction ft = fm.beginTransaction();
        if (isNewTask(flowDetails, fragment, fClass)) {
            final Fragment newFragment = Fragment.instantiate(activity, fClass.getName());
            newFragment.setArguments(bundle);
            ((T) newFragment).setUseCase(gameUseCase);
            FragmentUtils.setAnimation(ft, fragmentAnimation);
            ft.replace(containerId, newFragment).commit();
        } else {
            if (fragment.isDetached()) {
                ((T) fragment).setUseCase(gameUseCase);
                ft.attach(fragment).commit();
            }
        }
    }

    private static <T extends Fragment & GameFlowFragment> boolean isNewTask(final FlowDetails flowDetails,
                                                                             final Fragment fragment,
                                                                             final Class<T> tClass) {
        return fragment == null
                || !(tClass.isInstance(fragment))
                || !fragment.isRemoving()
                || ((GameFlowFragment) fragment).isNewTask(flowDetails);
    }


    public static void showDialogFragment(final AppCompatActivity activity,
                                          final Class<? extends DialogFragment> f,
                                          final String tag,
                                          final Bundle args) {

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


    public static void setAnimation(final FragmentTransaction ft, final int animation) {
        switch (animation) {
            case ANIMATE_SLIDE_LEFT:
                ft.setCustomAnimations(
                        R.anim.slide_in_right,
                        R.anim.slide_out_left,
                        R.anim.slide_in_left,
                        R.anim.slide_out_right);
                break;
            case ANIMATE_SLIDE_RIGHT:
                ft.setCustomAnimations(
                        R.anim.slide_in_left,
                        R.anim.slide_out_right,
                        R.anim.slide_in_right,
                        R.anim.slide_out_left);
                break;
            case ANIMATE_SLIDE_DOWN:
                ft.setCustomAnimations(
                        R.anim.slide_in_up,
                        R.anim.slide_out_down,
                        R.anim.slide_in_down,
                        R.anim.slide_out_up);
                break;
            case ANIMATE_SLIDE_UP:
                ft.setCustomAnimations(
                        R.anim.slide_in_down,
                        R.anim.slide_out_up,
                        R.anim.slide_in_up,
                        R.anim.slide_out_down);
                break;
            case ANIMATE_NONE:
                break;

        }
    }

    public static boolean validateFragment(final Fragment fragment) {
        return fragment != null && fragment.isAdded() && !fragment.isRemoving()
                && fragment.getView() != null && fragment.getActivity() != null;
    }

    public static Fragment findFragmentByTag(final FragmentManager manager, String tag) {
        return manager.findFragmentByTag(tag);
    }

    public static void clearFragmentBackStack(final FragmentManager fragmentManager) {
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}
