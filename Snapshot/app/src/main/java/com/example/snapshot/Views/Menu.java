package com.example.snapshot.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.snapshot.R;

/**
 * Activity that contains menu
 * @author oriol
 * @version 1.0
 */
public class Menu extends AppCompatActivity {

    /**
     * initialize elements
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        changeFragment(ListPublication.newInstance());

    }

    /**
     * cases for press butons
     * @param v
     */
    public void activateNum(View v) {

        switch(v.getId())
        {
            case R.id.btnChat:
                changeFragment(ListChats.newInstance());
                break;
            case R.id.btnUsers:
                changeFragment(ListUsers.newInstance());

                break;
            case R.id.btnHome:
                changeFragment(ListPublication.newInstance());

                break;
            case R.id.btnPlubication:
                Intent i=new Intent(getApplicationContext(), UploadMedia.class);
                startActivity(i);

                break;
            case R.id.btnProfile:
                changeFragment(Profile.newInstance());

                break;

        }
    }

    /**
     * Method to change fragment
     * @param fragment
     */
    private void changeFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();

    }


}
