package com.rajaryan.aryansh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class Profile_Edit extends AppCompatActivity {
    EditText Name,Email,Number,Address,Pincode,dis;
    ImageView m,f;
    String name;
    String gender1;
    String type;
    Button done;
    String type1;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile__edit);
        Name=findViewById(R.id.con_name);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        gender1="Not Selected";
        type1="Normal";
        Email=findViewById(R.id.con_email);
        Number=findViewById(R.id.contact_con);
        Address=findViewById(R.id.col_add);
        Pincode=findViewById(R.id.pinb_con);
        dis=findViewById(R.id.dis);
        m=findViewById(R.id.m);
        f=findViewById(R.id.f);
        done=findViewById(R.id.save_con);
        m.setImageResource(R.drawable.male_selected);
        databaseReference= FirebaseDatabase.getInstance().getReference("User Data");
        Query query=FirebaseDatabase.getInstance().getReference("User Data").orderByChild("UserId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds: dataSnapshot.getChildren()){


                    String name=""+ds.child("Name").getValue().toString();
                    String email=""+ds.child("Email").getValue().toString();
                    String add=""+ds.child("Address").getValue().toString();
                    String contact=""+ds.child("Contact Number").getValue().toString();
                    String pincode=""+ds.child("PinCode").getValue().toString();
                    String des=""+ds.child("Description").getValue().toString();
                    type=""+ds.child("Type").getValue().toString();

                    if(ds.hasChild("Type")){
                        if(type.equals("Normal")){
                            type1=type;
                        }
                        else {
                            type1="Paid";
                        }

                    }
                    if(ds.hasChild("Gender")){
                        String gender=""+ds.child("Gender").getValue().toString();
                        if(gender.equals("Male")){
                            m.setImageResource(R.drawable.male_selected);
                            f.setImageResource(R.drawable.female);
                            gender1="Male";
                        }
                        else {
                            gender1="Female";
                            m.setImageResource(R.drawable.male);
                            f.setImageResource(R.drawable.female_selected);
                        }
                    }
                    else {
                        gender1="Not Selected";
                    }

                    Name.setText(name);
                    Email.setText(email);
                    Address.setText(add);
                    Number.setText(contact);
                    Pincode.setText(pincode);
                    dis.setText(des);
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.setImageResource(R.drawable.male_selected);
                f.setImageResource(R.drawable.female);
                gender1="Male";
                HashMap<Object,String> hashMap=new HashMap<>();
                hashMap.put("Gender","Male");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Gender").setValue("Male");
            }
        });
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m.setImageResource(R.drawable.male);
                f.setImageResource(R.drawable.female_selected);
                HashMap<Object,String> hashMap=new HashMap<>();
                gender1="Female";
                hashMap.put("Gender","Female");
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Gender").setValue("Female");
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(gender1.equals("Not Selected")){
                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid().toString()).child("Gender").setValue("Male");
                    Toast.makeText(Profile_Edit.this,"Please Select Gender",Toast.LENGTH_LONG).show();                }

                else if(TextUtils.isEmpty(Name.getText().toString())){
                    Toast.makeText(Profile_Edit.this,"Please Enter Valid Name",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(Email.getText().toString())){
                    Toast.makeText(Profile_Edit.this,"Please Enter Valid Email",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(Number.getText().toString())){
                    Toast.makeText(Profile_Edit.this,"Please Enter Valid Number",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(Address.getText().toString())){
                    Toast.makeText(Profile_Edit.this,"Please Enter Valid Address",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(Pincode.getText().toString())){
                    Toast.makeText(Profile_Edit.this,"Please Enter Valid Pincode",Toast.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(dis.getText().toString())){
                    Toast.makeText(Profile_Edit.this,"Please Enter Valid Description",Toast.LENGTH_LONG).show();
                }
                else{
                    HashMap<Object,String> hashMap=new HashMap<>();


                    if(gender1.equals("Not Selected")){
                        Toast.makeText(Profile_Edit.this,"Please Select Gender",Toast.LENGTH_LONG).show();
                    }
                    hashMap.put("Name",Name.getText().toString());
                    hashMap.put("Email",Email.getText().toString());
                    hashMap.put("Address",Address.getText().toString());
                    hashMap.put("UserId",FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                    hashMap.put("Contact Number",Number.getText().toString());
                    hashMap.put("PinCode",Pincode.getText().toString());
                    hashMap.put("Description",dis.getText().toString());

                    hashMap.put("Gender",gender1);
                    hashMap.put("Type",type1);
                    databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMap);
                    Intent i=new Intent(Profile_Edit.this,MainActivity.class);
                    startActivity(i);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    public void back(View view) {
        onBackPressed();
    }
}
