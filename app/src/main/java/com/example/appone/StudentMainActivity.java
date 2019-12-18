package com.example.appone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import org.w3c.dom.Text;

public class StudentMainActivity extends AppCompatActivity {

    Fragment fragment;
    FragmentManager fm;
    FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getSupportActionBar().hide();
        setContentView(R.layout.activity_student_main);

        fragment = new RoomSelectFragment();
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.add(R.id.STUDENT_FRAME_MAIN, fragment);
        ft.commit();

    }

    public void enterRoom(boolean isRoomB){
        fragment = new TaskEnterFragment();
        Bundle bundle = new Bundle();
        if(isRoomB) {
            bundle.putString("lRoom","B");
        } else {
            bundle.putString("lRoom","Ax");
        }
        fragment.setArguments(bundle);
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.STUDENT_FRAME_MAIN, fragment);

        ft.commit();

    }
}
