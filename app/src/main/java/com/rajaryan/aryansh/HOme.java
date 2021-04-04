package com.rajaryan.aryansh;

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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HOme#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HOme extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView productCatRecycler, prodItemRecycler;
    Adapter adapter;
    Button article;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HOme() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HOme.
     */
    // TODO: Rename and change types and number of parameters
    public static HOme newInstance(String param1, String param2) {
        HOme fragment = new HOme();
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
        View view=inflater.inflate(R.layout.fragment_h_ome, container, false);
        prodItemRecycler=view.findViewById(R.id.product_recycler);
        prodItemRecycler.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        Query query2= FirebaseDatabase.getInstance().getReference().child("Products");
        article=view.findViewById(R.id.article);
        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),Pdf.class);
                startActivity(i);
            }
        });
        FirebaseRecyclerOptions<Product_Details> options2 =
                new FirebaseRecyclerOptions.Builder<Product_Details>()
                        .setQuery(query2,Product_Details.class)
                        .build();
        adapter=new Adapter(options2);
        prodItemRecycler.setAdapter(adapter);
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }
    public class Adapter extends FirebaseRecyclerAdapter<Product_Details,Adapter.viewholder>{
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
            Glide.with(getActivity()).load(product_details.getImage()).fitCenter().into(viewholder.imageView);
            final String key=getRef(i).getKey().toString();
            viewholder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(getActivity(),Productdetails.class);
                    i.putExtra("Id",key);
                    startActivity(i);
                }
            });
            viewholder.Price.setText(product_details.getPrice());
            viewholder.Name.setText(product_details.getName());
            viewholder.des.setText(product_details.getSize());
        }
        @NonNull
        @Override
        public viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.products_row_item, parent, false);
            return new viewholder(view);
        }

        public class viewholder extends RecyclerView.ViewHolder{
            ImageView imageView;
            TextView Price,Name,des;
            public viewholder(@NonNull View itemView) {
                super(itemView);
                imageView=itemView.findViewById(R.id.prod_image);
                Price=itemView.findViewById(R.id.prod_price);
                Name=itemView.findViewById(R.id.prod_name);
                des=itemView.findViewById(R.id.prod_qty);
            }
        }
    }
}
