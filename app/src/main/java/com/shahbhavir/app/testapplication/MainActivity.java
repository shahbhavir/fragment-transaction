package com.shahbhavir.app.testapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends FragmentActivity{

    private android.support.v4.app.FragmentTransaction fragmentTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                FragmentManager manager = getSupportFragmentManager();
                if (manager != null) {
                    Fragment currentFragment = MainActivity.this.getCurrentFragment();
                    if (currentFragment != null) {
                        currentFragment.onResume();
                        System.out.println("Backstack: " + currentFragment.getClass().getCanonicalName());
                    }
                }
            }
        });


        // Replace with A
        System.out.println("Replace with A");
        replaceFragmentInDefaultLayout(AFragment.newInstance("One", "Two"), false, true);

        // Replace with B and addToBackStack
        System.out.println("Replace with B");
        replaceFragmentInDefaultLayout(BFragment.newInstance("One", "Two"), true, true);

        // Replace with C
        System.out.println("Replace with C");
        replaceFragmentInDefaultLayout(CFragment.newInstance("One", "Two"), false, true);

        // Pressed Back button
        System.out.println("Pressed back button");
        popBackStack();

        // Replace with B and addToBackStack
        System.out.println("Replace with B");
        replaceFragmentInDefaultLayout(BFragment.newInstance("One", "Two"), true, true);
    }

    public Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.fragment);
    }

    public void replaceFragmentInDefaultLayout(Fragment fragmentToBeLoaded,
                                               boolean addToBackStack, boolean allowStateLoss) {
        if (!getSupportFragmentManager().isDestroyed()) {
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.fragment, fragmentToBeLoaded,
                    fragmentToBeLoaded.getClass().getCanonicalName());

            if (addToBackStack) {
                fragmentTransaction.addToBackStack(fragmentToBeLoaded.getClass().getCanonicalName());
            }
            if (allowStateLoss) {
                fragmentTransaction.commitAllowingStateLoss();
            } else {
                fragmentTransaction.commit();
            }
        }
    }

    public void popBackStack() {
        if (getSupportFragmentManager() != null) {
            getSupportFragmentManager().popBackStack();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
