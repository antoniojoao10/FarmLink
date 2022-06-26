package com.example.farmlink;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivitySold extends AppCompatActivity {
    private RecyclerView recycle;
    public MyAdapterSold myAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sold);

        Variables.names = new ArrayList<>();
        for (String i : Variables.productSold.keySet()) {
            Variables.names.add(i);
        }

        FloatingActionButton backButton = (FloatingActionButton)this.findViewById(R.id.floatingActionButtonBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        recycle = findViewById(R.id.recyclerSold);


        myAdapter = new MyAdapterSold(getApplicationContext(), this::onFruitClick);
        recycle.setAdapter(myAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        swipeRefreshLayout= findViewById(R.id.refreshSold);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
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
                                                            for( int i = 0; i < document.getData().size() ; i++) {
                                                                String pos = Integer.toString(i);
                                                                Variables.notify.add(Integer.parseInt(String.valueOf(document.getData().get(pos))));
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
                                                            myAdapter.notifyDataSetChanged();
                                                            swipeRefreshLayout.setRefreshing(false);
                                                        }
                                                    }
                                                });
                                    }
                                }
                            }
                        });

            }
        });
    }

    public void onFruitClick(int position) {

    }
}