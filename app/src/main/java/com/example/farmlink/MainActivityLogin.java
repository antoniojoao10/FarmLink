package com.example.farmlink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivityLogin extends AppCompatActivity {
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_login);

        Variables.mAuth = FirebaseAuth.getInstance();

        EditText email = findViewById(R.id.editTextTextEmailAddress);
        EditText password = findViewById(R.id.editTextTextPassword);

        Button buttonOne = findViewById(R.id.button_login);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println(email.getText().toString());
                System.out.println(password.getText().toString());
                if(!(email.getText().toString().equals("") || password.getText().toString().equals(""))) {
                    Variables.mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(MainActivityLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Variables.imagesNames = new ArrayList<String>();
                                        Variables.productNames = new ArrayList<String>();
                                        Variables.productPrices = new ArrayList<String>();
                                        Variables.productQuantaty = new ArrayList<String>();
                                        Variables.productSeller = new ArrayList<String>();
                                        Variables.productBought = new HashMap<>();
                                        Variables.productSold = new HashMap<>();
                                        Variables.imagesNamesFilter = new ArrayList<>();
                                        Variables.productNamesFilter = new ArrayList<>();
                                        Variables.productPricesFilter = new ArrayList<>();
                                        Variables.notify = new ArrayList<>();

                                        db = FirebaseFirestore.getInstance();
                                        db.collection("imagesNames").document("rWKqqK0qXkZfvRXYsd13")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            for( int i = 0; i < document.getData().size() ; i++) {
                                                                String pos = Integer.toString(i);
                                                                Variables.imagesNames.add(String.valueOf(document.getData().get(pos)));
                                                            }
                                                        }
                                                    }
                                                });

                                        db.collection("productsNames").document("5dDdgVtYIZHlp2jQNwLJ")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            for( int i = 0; i < document.getData().size() ; i++) {
                                                                String pos = Integer.toString(i);
                                                                Variables.productNames.add(String.valueOf(document.getData().get(pos)));
                                                            }
                                                        }
                                                    }
                                                });

                                        db.collection("productsPrices").document("gQbdwDvxAkGqZYCra3oV")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            for( int i = 0; i < document.getData().size() ; i++) {
                                                                String pos = Integer.toString(i);
                                                                Variables.productPrices.add(String.valueOf(document.getData().get(pos)));
                                                            }
                                                        }
                                                    }
                                                });

                                        db.collection("productSeller").document("m12PQNSDu7RAcBBXfeIp")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            for( int i = 0; i < document.getData().size() ; i++) {
                                                                String pos = Integer.toString(i);
                                                                Variables.productSeller.add(String.valueOf(document.getData().get(pos)));
                                                            }
                                                        }
                                                    }
                                                });



                                        db.collection("productsQuantity").document("1iYoNxyjhr6De9tlCVKI")
                                                .get()
                                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                        if (task.isSuccessful()) {
                                                            DocumentSnapshot document = task.getResult();
                                                            for( int i = 0; i < document.getData().size() ; i++) {
                                                                String pos = Integer.toString(i);
                                                                Variables.productQuantaty.add(String.valueOf(document.getData().get(pos)));
                                                            }
                                                            db.collection("productSold").document(Variables.mAuth.getUid())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot document = task.getResult();
                                                                                for( String i : document.getData().keySet() ) {
                                                                                    Variables.productSold.put(String.valueOf(i),String.valueOf(document.getData().get(i)));
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                            db.collection("productAlert").document(Variables.mAuth.getUid())
                                                                    .get()
                                                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                            if (task.isSuccessful()) {
                                                                                DocumentSnapshot document = task.getResult();
                                                                                for( String i : document.getData().keySet() ) {
                                                                                    Variables.notify.add(Integer.parseInt(String.valueOf(document.getData().get(i))));
                                                                                }
                                                                            }
                                                                        }
                                                                    });
                                                            FirebaseUser currentUser = Variables.mAuth.getCurrentUser();
                                                            if ( currentUser != null ){
                                                                db.collection("productBought").document(Variables.mAuth.getUid())
                                                                        .get()
                                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    DocumentSnapshot document = task.getResult();
                                                                                    for( String i : document.getData().keySet() ) {
                                                                                        Variables.productBought.put(String.valueOf(i),String.valueOf(document.getData().get(i)));
                                                                                    }

                                                                                    for( int i = 0 ; i < Variables.notify.size() ; i++ ){
                                                                                        if( !Variables.productQuantaty.get(Variables.notify.get(i)).equals("0.0") ){
                                                                                            int pos = Variables.notify.get(i);

                                                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "AMS")
                                                                                                    .setSmallIcon(R.drawable.all_fruits_and_vegetables_in_basket_background_transparent_ved4qx)
                                                                                                    .setContentTitle("FarmLink Restock")
                                                                                                    .setContentText(Variables.productNames.get(pos) + " has been restock!!")
                                                                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                                                                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                                                                                            managerCompat.notify(1, builder.build());
                                                                                            Variables.notify.remove(i);

                                                                                            Map<String, Object> not = new HashMap<>();
                                                                                            for(int p = 0 ; p < Variables.notify.size() ; p++){
                                                                                                not.put(String.valueOf(p),Variables.notify.get(p));
                                                                                            }
                                                                                            db.collection("productAlert").document(Variables.mAuth.getUid())
                                                                                                    .set(not);
                                                                                        }
                                                                                    }
                                                                                    Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                                                    startActivity(activityIntent);
                                                                                }
                                                                            }
                                                                        });
                                                            }
                                                        }
                                                    }
                                                });
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivityLogin.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(MainActivityLogin.this, "Failed to Login - email or password is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button register = findViewById(R.id.button_regiter);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!(email.getText().toString().equals("") || password.getText().toString().equals(""))) {
                    Variables.mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                            .addOnCompleteListener(MainActivityLogin.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = Variables.mAuth.getCurrentUser();
                                        Map<String, Object> data = new HashMap<>();
                                        db.collection("productBought").document(Variables.mAuth.getUid()).set(data);
                                        db.collection("productSold").document(Variables.mAuth.getUid()).set(data);
                                        db.collection("productAlert").document(Variables.mAuth.getUid()).set(data);
                                        Toast.makeText(MainActivityLogin.this, user + " registered", Toast.LENGTH_SHORT).show();
                                        Intent activityIntent = new Intent(getApplicationContext(), MainActivityLogin.class);
                                        startActivity(activityIntent);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(MainActivityLogin.this, "Failed to Register - Password weak", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }else{
                    Toast.makeText(MainActivityLogin.this, "Failed to Register - email or password is empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        Variables.imagesNames = new ArrayList<String>();
        Variables.productNames = new ArrayList<String>();
        Variables.productPrices = new ArrayList<String>();
        Variables.productQuantaty = new ArrayList<String>();
        Variables.productSeller = new ArrayList<String>();
        Variables.productBought = new HashMap<>();
        Variables.productSold = new HashMap<>();
        Variables.imagesNamesFilter = new ArrayList<>();
        Variables.productNamesFilter = new ArrayList<>();
        Variables.productPricesFilter = new ArrayList<>();
        Variables.notify = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        db.collection("imagesNames").document("rWKqqK0qXkZfvRXYsd13")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            for( int i = 0; i < document.getData().size() ; i++) {
                                String pos = Integer.toString(i);
                                Variables.imagesNames.add(String.valueOf(document.getData().get(pos)));
                            }
                        }
                    }
                });

        db.collection("productsNames").document("5dDdgVtYIZHlp2jQNwLJ")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            for( int i = 0; i < document.getData().size() ; i++) {
                                String pos = Integer.toString(i);
                                Variables.productNames.add(String.valueOf(document.getData().get(pos)));
                            }
                        }
                    }
                });

        db.collection("productsPrices").document("gQbdwDvxAkGqZYCra3oV")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            for( int i = 0; i < document.getData().size() ; i++) {
                                String pos = Integer.toString(i);
                                Variables.productPrices.add(String.valueOf(document.getData().get(pos)));
                            }
                        }
                    }
                });

        db.collection("productSeller").document("m12PQNSDu7RAcBBXfeIp")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            for( int i = 0; i < document.getData().size() ; i++) {
                                String pos = Integer.toString(i);
                                Variables.productSeller.add(String.valueOf(document.getData().get(pos)));
                            }
                        }
                    }
                });



        db.collection("productsQuantity").document("1iYoNxyjhr6De9tlCVKI")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            for( int i = 0; i < document.getData().size() ; i++) {
                                String pos = Integer.toString(i);
                                Variables.productQuantaty.add(String.valueOf(document.getData().get(pos)));
                            }
                            FirebaseUser currentUser = Variables.mAuth.getCurrentUser();
                            if ( currentUser != null ){
                                db.collection("productSold").document(Variables.mAuth.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    for( String i : document.getData().keySet() ) {
                                                        Variables.productSold.put(String.valueOf(i),String.valueOf(document.getData().get(i)));
                                                    }
                                                }
                                            }
                                        });
                                db.collection("productAlert").document(Variables.mAuth.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    for( String i : document.getData().keySet() ) {
                                                        Variables.notify.add(Integer.parseInt(String.valueOf(document.getData().get(i))));
                                                    }
                                                }
                                            }
                                        });
                                db.collection("productBought").document(Variables.mAuth.getUid())
                                        .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if (task.isSuccessful()) {
                                                    DocumentSnapshot document = task.getResult();
                                                    for( String i : document.getData().keySet() ) {
                                                        Variables.productBought.put(String.valueOf(i),String.valueOf(document.getData().get(i)));
                                                    }

                                                    for( int i = 0 ; i < Variables.notify.size() ; i++ ){
                                                        if( !Variables.productQuantaty.get(Variables.notify.get(i)).equals("0.0") ){
                                                            int pos = Variables.notify.get(i);

                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "AMS")
                                                                    .setSmallIcon(R.drawable.all_fruits_and_vegetables_in_basket_background_transparent_ved4qx)
                                                                    .setContentTitle("FarmLink Restock")
                                                                    .setContentText(Variables.productNames.get(pos) + " has been restock!!")
                                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(getApplicationContext());
                                                            managerCompat.notify(1, builder.build());
                                                            Variables.notify.remove(i);

                                                            Map<String, Object> not = new HashMap<>();
                                                            for(int p = 0 ; p < Variables.notify.size() ; p++){
                                                                not.put(String.valueOf(p),Variables.notify.get(p));
                                                            }
                                                            db.collection("productAlert").document(Variables.mAuth.getUid())
                                                                    .set(not);
                                                        }
                                                    }
                                                    Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(activityIntent);
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });

    }
}