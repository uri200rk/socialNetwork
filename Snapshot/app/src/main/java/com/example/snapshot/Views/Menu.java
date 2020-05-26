package com.example.snapshot.Views;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.snapshot.R;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        changeFragment(ListPublication.newInstance());

    }



    public void activateNum(View v) {

        switch(v.getId())
        {
            case R.id.btnChat:
                Toast.makeText(getApplicationContext(),"Boton chat presionado",Toast.LENGTH_SHORT).show();
                changeFragment(ListChats.newInstance());
                break;
            case R.id.btnUsers:
                Toast.makeText(getApplicationContext(),"Boton users presionado",Toast.LENGTH_SHORT).show();
                changeFragment(ListUsers.newInstance());

                break;
            case R.id.btnHome:
                Toast.makeText(getApplicationContext(),"Boton home presionado",Toast.LENGTH_SHORT).show();
                changeFragment(ListPublication.newInstance());

                break;
            case R.id.btnPlubication:
                Toast.makeText(getApplicationContext(),"Boton publication presionado",Toast.LENGTH_SHORT).show();
                Intent i=new Intent(getApplicationContext(), UploadMedia.class);
                startActivity(i);

                break;
            case R.id.btnProfile:
                Toast.makeText(getApplicationContext(),"Boton profile presionado",Toast.LENGTH_SHORT).show();
                changeFragment(Profile.newInstance());

                break;

        }
    }

    private void changeFragment(Fragment fragment){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);
        transaction.commit();

    }


}
