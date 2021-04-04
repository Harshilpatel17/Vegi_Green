package com.rajaryan.aryansh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
public class HomeActivity extends AppCompatActivity {


        private ChipNavigationBar chipNavigationBar;
        private Fragment fragment = null;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_home);

            chipNavigationBar = findViewById(R.id.chipNavigation);

            chipNavigationBar.setItemSelected(R.id.home, true);
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HOme()).commit();

            chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
                @Override
                public void onItemSelected(int i) {
                    switch (i) {
                        case R.id.home:
                            fragment = new HOme();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HOme()).commit();
                            break;
                        case R.id.cart:
                            fragment = new Search();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new Search()).commit();
                            break;
                        case R.id.profile:
                            fragment = new Profile();
                            getSupportFragmentManager().beginTransaction().replace(R.id.container, new Profile()).commit();
                            break;
                    }
                    DatabaseReference databaseReference= (DatabaseReference) FirebaseDatabase.getInstance().getReference("User Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Name");
                    databaseReference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot ds) {
                            if(!ds.exists()){
                                Intent i=new Intent(HomeActivity.this,Profile_Edit.class);
                                startActivity(i);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            });
        }
    }