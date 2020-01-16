package com.example.android.bhawanlaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;


public class MainActivity extends AppCompatActivity
        implements LoginFragment.loginCI,
        SignUpFragment.signUpCI,
        ProfileSetUpFragment.profileSetUpCI,
        RoomSelectFragment.roomSelectCI,
        SubmitTaskFragment.submitTaskFragmentCI,
        LaundrymanRoomSelect.laundrymanRoomSelectCI{

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;
    Toolbar toolbar;
    private static DrawerLayout drawerLayout;
    private static ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        insertNavigationDrawer();
        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME, loginFragment);
        ft.commit();
    }

    public void insertNavigationDrawer(){
        toolbar = findViewById(R.id.TOOLBAR);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.DRAWER_LAYOUT);

        NavigationView navigationView = findViewById(R.id.NAVIGATION_VIEW);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch(menuItem.getItemId()){
                    case R.id.Ax_QUEUE_MENU_ITEM:
                        Bundle aq = new Bundle();
                        aq.putString("laundry_room", "Ax");
                        QueueFragment queueFragmentAx = new QueueFragment();
                        queueFragmentAx.setArguments(aq);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.MAIN_FRAME, queueFragmentAx);
                        ft.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.Ax_REQUESTS_MENU_ITEM:
                        Bundle ar = new Bundle();
                        ar.putString("laundry_room", "Ax");
                        RequestsFragment requestsFragmentAx = new RequestsFragment();
                        requestsFragmentAx.setArguments(ar);
                        FragmentTransaction f = getSupportFragmentManager().beginTransaction();
                        f.replace(R.id.MAIN_FRAME, requestsFragmentAx);
                        f.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;


                    case R.id.B_QUEUE_MENU_ITEM:
                        Bundle bq = new Bundle();
                        bq.putString("laundry_room", "B");
                        QueueFragment queueFragmentB = new QueueFragment();
                        queueFragmentB.setArguments(bq);
                        FragmentTransaction ftb = getSupportFragmentManager().beginTransaction();
                        ftb.replace(R.id.MAIN_FRAME, queueFragmentB);
                        ftb.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.B_REQUESTS_MENU_ITEM:
                        Bundle bb = new Bundle();
                        bb.putString("laundry_room", "B");
                        RequestsFragment requestsFragment = new RequestsFragment();
                        requestsFragment.setArguments(bb);
                        FragmentTransaction ftB = getSupportFragmentManager().beginTransaction();
                        ftB.replace(R.id.MAIN_FRAME, requestsFragment);
                        ftB.commit();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }


                return true;
            }
        });

        //CHANGE THIS LATER TO APPEAR ONLY ON LAUNDRYMAN'S PAGE
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);





    }


    public static void lockDrawer(){
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }

    public static void unlockDrawer() {
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onBackPressed() {

        if( drawerLayout!=null && drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{super.onBackPressed();}
    }

    @Override
    public void openSignUp(){
        SignUpFragment signUpFragment = new SignUpFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME , signUpFragment);
        ft.commit();
    }


    @Override
    public void openLoginScreen() {
        LoginFragment loginFragment = new LoginFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.replace(R.id.MAIN_FRAME, loginFragment);
        ft.commit();
    }

    @Override
    public void openProfileSetUp() {
        ProfileSetUpFragment profileSetUpFragment = new ProfileSetUpFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ft.replace(R.id.MAIN_FRAME, profileSetUpFragment);
        ft.commit();
    }

    @Override
    public void openRoomSelectFragment() {
        lockDrawer();
        RoomSelectFragment roomSelectFragment = new RoomSelectFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME, roomSelectFragment);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void openLaundrymanRoomSelectFragment() {
        LaundrymanRoomSelect laundrymanRoomSelect = new LaundrymanRoomSelect();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME, laundrymanRoomSelect);
        unlockDrawer();
        ft.commitAllowingStateLoss();
    }

    @Override
    public void openLaundryRoom(boolean isRoomB, String n, String r, int numBuckets) {
            SubmitTaskFragment submitTaskFragment = new SubmitTaskFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("nameOfStudent", n);
            bundle.putString("roomOfStudent", r);
            bundle.putInt("bucketsInQueue", numBuckets);

            if(isRoomB){
                bundle.putString("Laundry_Room", "B");
                submitTaskFragment.setArguments(bundle);
                ft.replace(R.id.MAIN_FRAME, submitTaskFragment);
                ft.addToBackStack(null);
                ft.commit();
            } else {
                bundle.putString("Laundry_Room", "Ax");
                submitTaskFragment.setArguments(bundle);
                ft.replace(R.id.MAIN_FRAME, submitTaskFragment);
                ft.addToBackStack(null);
                ft.commit();
            }
    }

    @Override
    public void openRequestSentFragment() {
        RequestSentFragment requestSentFragment = new RequestSentFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME, requestSentFragment);
        ft.addToBackStack(null);
        ft.commit();
    }

    @Override
    public void openLaundrymanRoom(boolean isB) {

        Bundle b = new Bundle();

        if(isB){
            b.putString("laundry_room", "B");
        } else {
            b.putString("laundry_room", "Ax");
        }

        RequestsFragment requestsFragment = new RequestsFragment();
        requestsFragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME, requestsFragment);
        //ft.addToBackStack(null);
        ft.commit();

    }


    public void setActionBarTitle(String title){
        getSupportActionBar().setTitle(title);
    }
}
