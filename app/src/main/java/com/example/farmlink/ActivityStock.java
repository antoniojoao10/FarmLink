package com.example.farmlink;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class ActivityStock extends AppCompatActivity {

    int position;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock);

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

                BigDecimal a = BigDecimal.valueOf(avai);
                BigDecimal b = BigDecimal.valueOf(quant);
                BigDecimal c = a.add(b);
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
                                Toast.makeText(ActivityStock.this, "Stock added", Toast.LENGTH_SHORT).show();
                                Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(activityIntent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ActivityStock.this, "Failed to add stock", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}