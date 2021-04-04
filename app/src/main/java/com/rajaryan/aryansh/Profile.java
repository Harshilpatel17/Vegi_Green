package com.rajaryan.aryansh;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

import static maes.tech.intentanim.CustomIntent.customType;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Profile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Profile extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    String id;
    TextView Name,Email,dis;
    RecyclerView rec;
    Button edit,logout;
    Adapter adapter;
    CircleImageView m;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Profile() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Profile.
     */
    // TODO: Rename and change types and number of parameters
    public static Profile newInstance(String param1, String param2) {
        Profile fragment = new Profile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_profile, container, false);
        Name=view.findViewById(R.id.n11);
        Email=view.findViewById(R.id.e11);
        dis=view.findViewById(R.id.des1);
        edit=view.findViewById(R.id.add);
        logout=view.findViewById(R.id.profile);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),Profile_Edit.class);
                customType(getActivity(),"bottom-to-up");
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });
        m=view.findViewById(R.id.m);
        id = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Query databaseReference= FirebaseDatabase.getInstance().getReference("User Data").orderByChild("UserId").equalTo(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds:snapshot.getChildren()){
                    String name=""+ds.child("Name").getValue().toString();
                    String email=""+ds.child("Email").getValue().toString();
                    String add=""+ds.child("Address").getValue().toString();
                    String contact=""+ds.child("Contact Number").getValue().toString();
                    String pincode=""+ds.child("PinCode").getValue().toString();
                    String des=""+ds.child("Description").getValue().toString();
                    String type=""+ds.child("Type").getValue().toString();
                    if(ds.hasChild("Gender")){
                        String gender=""+ds.child("Gender").getValue().toString();
                        if(gender.equals("Male")){
                            m.setImageResource(R.drawable.male_selected);
                        }
                        if (gender.equals("Female")){
                            m.setImageResource(R.drawable.female_selected);
                        }
                    }
                    Name.setText(name);
                    Email.setText(email);
                    dis.setText(des);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        RecyclerView recyclerView =view.findViewById(R.id.rec);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        Query query2= FirebaseDatabase.getInstance().getReference().child("User Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Orders");
        FirebaseRecyclerOptions<Product_Details> options2 =
                new FirebaseRecyclerOptions.Builder<Product_Details>()
                        .setQuery(query2,Product_Details.class)
                        .build();
        adapter=new Adapter(options2);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    public class Adapter extends FirebaseRecyclerAdapter<Product_Details,Adapter.viewholder> {

        /**
         * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
         * {@link FirebaseRecyclerOptions} for configuration options.
         *
         * @param options
         */
        public Adapter(@NonNull FirebaseRecyclerOptions<Product_Details> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull viewholder viewholder, int i, @NonNull Product_Details product_details) {
            Glide.with(getContext()).load(product_details.getImage()).fitCenter().into(viewholder.image);
            viewholder.name.setText(product_details.getName().toString());
            viewholder.price.setText(product_details.getPrice());
            viewholder.calori.setText(product_details.getQuantity());
            final String key=getRef(i).getKey().toString();
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),Productdetails.class);
                    i.putExtra("Id",key);
                    startActivity(i);
                }
            });
        }

        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.shoping_item, parent, false);

            return new Adapter.viewholder(view);        }

        public class viewholder extends RecyclerView.ViewHolder {
            ImageView image;
            TextView name,price,calori;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                name=itemView.findViewById(R.id.name);
                price=itemView.findViewById(R.id.price);
                calori=itemView.findViewById(R.id.calori);
                image=itemView.findViewById(R.id.pic);
            }
        }
    }
}
