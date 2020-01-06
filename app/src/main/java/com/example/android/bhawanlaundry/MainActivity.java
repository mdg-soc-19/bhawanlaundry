package com.example.android.bhawanlaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements LoginFragment.loginCI, SignUpFragment.signUpCI, ProfileSetUpFragment.profileSetUpCI, RoomSelectFragment.roomSelectCI, SubmitTaskFragment.submitTaskFragmentCI{

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        LoginFragment loginFragment = new LoginFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME, loginFragment);
        ft.commit();
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
        RoomSelectFragment roomSelectFragment = new RoomSelectFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.MAIN_FRAME, roomSelectFragment);
        ft.commit();
    }

    @Override
    public void openLaundryRoom(boolean isRoomB, String n, String r) {
            SubmitTaskFragment submitTaskFragment = new SubmitTaskFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("nameOfStudent", n);
            bundle.putString("roomOfStudent", r);

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

}
