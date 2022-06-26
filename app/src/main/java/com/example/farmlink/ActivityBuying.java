package com.example.farmlink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityBuying extends AppCompatActivity {

    int position;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buying);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            position = extras.getInt("pos");
        }

        FloatingActionButton backButton = (FloatingActionButton)this.findViewById(R.id.floatingActionButtonBack2);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference photoReference= storageReference.child("fruitsImages/" + Variables.imagesNames.get(position) + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                ImageView myImage = findViewById(R.id.alertImage);
                myImage.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });

        TextView myText1 = findViewById(R.id.alertTitle);
        TextView myText2 = findViewById(R.id.alertPrice);
        TextView myText3 = findViewById(R.id.buyAv);
        TextView myText4 = findViewById(R.id.buyQuantity);

        myText1.setText(Variables.productNames.get(position));
        myText2.setText("Price: "+Variables.productPrices.get(position)+"€/Kg");
        myText3.setText("Available: "+Variables.productQuantaty.get(position)+"Kg");


        String name = Variables.productNames.get(position);
        String price = Variables.productPrices.get(position);
        String av = Variables.productQuantaty.get(position);

        db = FirebaseFirestore.getInstance();

        Button buttonOne = findViewById(R.id.alert);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double quant = Double.parseDouble(String.valueOf(myText4.getText()));
                double avai = Double.parseDouble(av);

                int randomNum = ThreadLocalRandom.current().nextInt(0, 1000 + 1);
                Variables.productBought.put(Variables.productBought.size()+"+"+String.valueOf(Variables.productNames.get(position)), String.valueOf(quant));
                Map<String, Object> boughtData = new HashMap<>();
                for(String i : Variables.productBought.keySet()){
                    boughtData.put(String.valueOf(i),Variables.productBought.get(i));
                }
                db.collection("productBought").document(Variables.mAuth.getUid())
                        .set(boughtData);



                Variables.productSold = new HashMap<>();
                db.collection("productSold").document(Variables.productSeller.get(position))
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    for( String i : document.getData().keySet() ) {
                                        Variables.productSold.put(String.valueOf(i),String.valueOf(document.getData().get(i)));
                                    }
                                    double resx;
                                    if( Variables.productSold.get(String.valueOf(Variables.productNames.get(position))) != null)
                                    {
                                        BigDecimal ax = BigDecimal.valueOf(Double.parseDouble(Variables.productSold.get(String.valueOf(Variables.productNames.get(position)))));
                                        BigDecimal bx = BigDecimal.valueOf(quant);
                                        BigDecimal cx = ax.add(bx);
                                        resx = cx.doubleValue();
                                    }else{
                                        resx = quant;
                                    }
                                    Variables.productSold.put(String.valueOf(Variables.productNames.get(position)), String.valueOf(resx));
                                    Map<String, Object> soldData = new HashMap<>();
                                    for(String i : Variables.productSold.keySet()){
                                        soldData.put(String.valueOf(i),Variables.productSold.get(i));
                                    }
                                    db.collection("productSold").document(Variables.productSeller.get(position))
                                            .set(soldData);
                                }
                            }
                        });



                if ( quant == avai) {
                    BigDecimal a = BigDecimal.valueOf(avai);
                    BigDecimal b = BigDecimal.valueOf(quant);
                    BigDecimal c = a.subtract(b);
                    double res = c.doubleValue();
                    Variables.productQuantaty.set(position, String.valueOf(res));
                    Map<String, Object> quanData = new HashMap<>();
                    for(int i = 0; i < Variables.productQuantaty.size() ; i++){
                        quanData.put(String.valueOf(i),Variables.productQuantaty.get(i));
                    }
                    db.collection("productsQuantity").document("1iYoNxyjhr6De9tlCVKI")
                            .set(quanData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ActivityBuying.this, "Product bought", Toast.LENGTH_SHORT).show();
                                    Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(activityIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ActivityBuying.this, "Failed to buy", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else if( 0 < quant  && quant <= avai ){
                    BigDecimal a = BigDecimal.valueOf(avai);
                    BigDecimal b = BigDecimal.valueOf(quant);
                    BigDecimal c = a.subtract(b);
                    double res = c.doubleValue();
                    Variables.productQuantaty.set(position, String.valueOf(res));
                    Map<String, Object> quanData = new HashMap<>();
                    for(int i = 0; i < Variables.productQuantaty.size() ; i++){
                        quanData.put(String.valueOf(i),Variables.productQuantaty.get(i));
                    }
                    db.collection("productsQuantity").document("1iYoNxyjhr6De9tlCVKI")
                            .set(quanData).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ActivityBuying.this, "Product bought", Toast.LENGTH_SHORT).show();
                                    Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivity(activityIntent);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ActivityBuying.this, "Failed to buy", Toast.LENGTH_SHORT).show();
                                }
                            });

                }else{
                    Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(activityIntent);
                    Toast.makeText(ActivityBuying.this, "Quantity is invalid", Toast.LENGTH_SHORT).show();
                }







            }
        });
    }
}