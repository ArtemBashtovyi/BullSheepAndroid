package com.moshenskyi.bullsheepandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.moshenskyi.bullsheepandroid.pref.UserPrefManager;

public class PetNameActivity extends AppCompatActivity {


    private Button submitBn;
    private EditText petNameEt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pet_name);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        submitBn = findViewById(R.id.loginScreenSubmitBn);
        petNameEt = findViewById(R.id.loginScreenPetNameEt);


        submitBn.setOnClickListener(v -> {
            String petName = petNameEt.getText().toString();

            UserPrefManager.getInstance().writeHealthPoint(1000);
            if (UserPrefManager.getInstance().getMoodPoint() < 0) {
                UserPrefManager.getInstance().writeMoodPoint(10);
            }

            UserPrefManager.getInstance().writeName(petName);
            Intent intent = new Intent(PetNameActivity.this, MainActivity.class);
            PetNameActivity.this.startActivity(intent);


            Log.i("PET NAME", UserPrefManager.getInstance().getName());
            Log.i("PET HEALTH", String.valueOf(UserPrefManager.getInstance().getHealthPoint()));
            Log.i("PET MOOD", String.valueOf(UserPrefManager.getInstance().getMoodPoint()));
        });

    }

}
