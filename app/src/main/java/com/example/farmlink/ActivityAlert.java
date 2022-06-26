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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityAlert extends AppCompatActivity {

    int position;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

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


        myText1.setText(Variables.productNames.get(position));
        myText2.setText("Price: "+Variables.productPrices.get(position)+"€/Kg");


        String name = Variables.productNames.get(position);
        String price = Variables.productPrices.get(position);

        db = FirebaseFirestore.getInstance();

        Button buttonOne = findViewById(R.id.alert);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Variables.notify.add(position);
                Variables.notify = removeDuplicates(Variables.notify);
                Map<String, Object> not = new HashMap<>();
                for(int p = 0 ; p < Variables.notify.size() ; p++){
                    not.put(String.valueOf(p),Variables.notify.get(p));
                }
                db.collection("productAlert").document(Variables.mAuth.getUid())
                        .set(not);

                Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(activityIntent);
            }
        });
    }

    public static <T> ArrayList<T> removeDuplicates(ArrayList<T> list)
    {

        // Create a new ArrayList
        ArrayList<T> newList = new ArrayList<T>();

        // Traverse through the first list
        for (T element : list) {

            // If this element is not present in newList
            // then add it
            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        // return the new list
        return newList;
    }
}