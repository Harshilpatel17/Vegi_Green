package com.rajaryan.aryansh;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

        import android.content.Context;
        import android.content.Intent;
        import android.net.ConnectivityManager;
        import android.net.NetworkInfo;
        import android.net.Uri;
        import android.os.Bundle;
        import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Productdetails extends AppCompatActivity {
    ImageView Plus,Minus;
    int num;
    TextView quant;
    TextView Name,Price,Details,Status;
    ImageView pic;
    String p1;
    String image;
    String name,price;
    String id;
    String quant1;
    final int UPI_PAYMENT = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_productdetails);
        Plus=findViewById(R.id.imageView9);
        Minus=findViewById(R.id.imageView8);
        quant=findViewById(R.id.textView14);
        Status=findViewById(R.id.textView7);
        Name=findViewById(R.id.textView11);
        Price=findViewById(R.id.textView12);
        Details=findViewById(R.id.textView13);
        pic=findViewById(R.id.imageView7);
        Intent i=getIntent();
        id=i.getStringExtra("Id");
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Products").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {

                Name.setText(ds.child("Name").getValue().toString());
                Status.setText(ds.child("State").getValue().toString());
                Price.setText(ds.child("Price").getValue().toString());
                Details.setText(ds.child("Description").getValue().toString());
                Glide.with(getApplicationContext()).load(ds.child("Image").getValue().toString()).fitCenter().into(pic);
                name=Name.getText().toString();
                price=ds.child("pr").getValue().toString();
                image=ds.child("Image").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        num=250;
        Plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                num=num+250;
                quant.setText(num+"gm");
                quant1=quant.getText().toString();
            }
        });
        Minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(num==250){
                    Toast.makeText(getApplicationContext(),"Thats Minimum Anount Of Quantity",Toast.LENGTH_SHORT).show();
                }
                else {
                    num=num-250;
                    quant.setText(num+"gm");
                    quant1=quant.getText().toString();
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        num=250;
    }

    void payUsingUpi(  String name,String upiId, String note, String amount) {
        Log.e("main ", "name "+name +"--up--"+upiId+"--"+ note+"--"+amount);
        Uri uri = Uri.parse("upi://pay").buildUpon()
                .appendQueryParameter("pa", upiId)
                .appendQueryParameter("pn", name)
                //.appendQueryParameter("mc", "")
                //.appendQueryParameter("tid", "02125412")
                //.appendQueryParameter("tr", "25584584")
                .appendQueryParameter("tn", note)
                .appendQueryParameter("am", amount)
                .appendQueryParameter("cu", "INR")
                //.appendQueryParameter("refUrl", "blueapp")
                .build();


        Intent upiPayIntent = new Intent(Intent.ACTION_VIEW);
        upiPayIntent.setData(uri);

        // will always show a dialog to user to choose an app
        Intent chooser = Intent.createChooser(upiPayIntent, "Pay with");

        // check if intent resolves
        if(null != chooser.resolveActivity(getPackageManager())) {
            startActivityForResult(chooser, UPI_PAYMENT);
        } else {
            Toast.makeText(Productdetails.this,"No UPI app found, please install one to continue",Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("main ", "response "+resultCode );
        /*
       E/main: response -1
       E/UPI: onActivityResult: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPIPAY: upiPaymentDataOperation: txnId=AXI4a3428ee58654a938811812c72c0df45&responseCode=00&Status=SUCCESS&txnRef=922118921612
       E/UPI: payment successfull: 922118921612
         */
        switch (requestCode) {
            case UPI_PAYMENT:
                if ((RESULT_OK == resultCode) || (resultCode == 11)) {
                    if (data != null) {
                        String trxt = data.getStringExtra("response");
                        Log.e("UPI", "onActivityResult: " + trxt);
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add(trxt);
                        upiPaymentDataOperation(dataList);
                    } else {
                        Log.e("UPI", "onActivityResult: " + "Return data is null");
                        ArrayList<String> dataList = new ArrayList<>();
                        dataList.add("nothing");
                        upiPaymentDataOperation(dataList);
                    }
                } else {
                    //when user simply back without payment
                    Log.e("UPI", "onActivityResult: " + "Return data is null");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    upiPaymentDataOperation(dataList);
                }
                break;
        }
    }

    private void upiPaymentDataOperation(ArrayList<String> data) {
        if (isConnectionAvailable(Productdetails.this)) {
            String str = data.get(0);
            Log.e("UPIPAY", "upiPaymentDataOperation: "+str);
            String paymentCancel = "";
            if(str == null) str = "discard";
            String status = "";
            String approvalRefNo = "";
            String response[] = str.split("&");
            for (int i = 0; i < response.length; i++) {
                String equalStr[] = response[i].split("=");
                if(equalStr.length >= 2) {
                    if (equalStr[0].toLowerCase().equals("Status".toLowerCase())) {
                        status = equalStr[1].toLowerCase();
                    }
                    else if (equalStr[0].toLowerCase().equals("ApprovalRefNo".toLowerCase()) || equalStr[0].toLowerCase().equals("txnRef".toLowerCase())) {
                        approvalRefNo = equalStr[1];
                    }
                }
                else {
                    paymentCancel = "Payment cancelled by user.";
                }
            }

            if (status.equals("success")) {
                //Code to handle successful transaction here.
                Toast.makeText(Productdetails.this, "Transaction successful.", Toast.LENGTH_SHORT).show();
                String dateStr = "04/05/2010";

                SimpleDateFormat curFormater = new SimpleDateFormat("dd/MM/yyyy");
                Date dateObj = null;
                try {
                    dateObj = curFormater.parse(dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                SimpleDateFormat postFormater = new SimpleDateFormat("MMMM dd, yyyy");

                String newDateStr = postFormater.format(dateObj);
                DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("User Data").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                HashMap<Object,String> hashMap=new HashMap<>();
                hashMap.put("Name",name);
                hashMap.put("Price",p1);
                hashMap.put("Date",newDateStr);
                hashMap.put("Image",image);
                hashMap.put("Id",FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                hashMap.put("Quantity",quant1);
                databaseReference.child("Orders").push().setValue(hashMap);
                Log.e("UPI", "payment successfull: "+approvalRefNo);
            }
            else if("Payment cancelled by user.".equals(paymentCancel)) {
                Toast.makeText(Productdetails.this, "Payment cancelled by user.", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "Cancelled by user: "+approvalRefNo);

            }
            else {
                Toast.makeText(Productdetails.this, "Transaction failed.Please try again", Toast.LENGTH_SHORT).show();
                Log.e("UPI", "failed payment: "+approvalRefNo);

            }
        } else {
            Log.e("UPI", "Internet issue: ");
            Toast.makeText(Productdetails.this, "Internet connection is not available. Please check and try again", Toast.LENGTH_SHORT).show();
        }
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfo = connectivityManager.getActiveNetworkInfo();
            if (netInfo != null && netInfo.isConnected()
                    && netInfo.isConnectedOrConnecting()
                    && netInfo.isAvailable()) {
                return true;
            }
        }
        return false;
    }

    public void pay() {
        int total_price;
        int qu=0;
        int pr=Integer.parseInt(price);
        int c=qu/250;
        total_price=c*pr;
         p1=String.valueOf(total_price);
        payUsingUpi(name,"yashrajmehta3027@okicici",
                "Aryansh", p1);
    }

    public void dia(View view) {
        int total_price;
        int qu=0;
        int pr=Integer.parseInt(price);
        int c=qu/250;
        total_price=c*pr;
        String p1=String.valueOf(total_price);
        BottomSheetDialog bottomSheetDialog=new BottomSheetDialog(Productdetails.this,R.style.BottomSheetDialogTheme);
        View bottomsheet= LayoutInflater.from(getApplicationContext()).inflate(R.layout.place_order, (FrameLayout)findViewById(R.id.container));
        TextView Price=bottomsheet.findViewById(R.id.pay);
        Price.setText("Total Amount Of Payment: "+Price);
        Button done=bottomsheet.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
        bottomSheetDialog.setContentView(bottomsheet);
        bottomSheetDialog.show();
    }
}
