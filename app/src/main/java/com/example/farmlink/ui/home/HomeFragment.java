package com.example.farmlink.ui.home;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.drawable.IconCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.farmlink.ActivityAlert;
import com.example.farmlink.ActivityBuying;
import com.example.farmlink.R;
import com.example.farmlink.Variables;
import com.example.farmlink.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment implements  MyAdapter.OnFruitListener{
    private RecyclerView recycle;
    public MyAdapter myAdapter;
    private FragmentHomeBinding binding;
    private Context context;
    SwipeRefreshLayout swipeRefreshLayout;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
    }
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("AMS", "Farm", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = context.getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        recycle = root.findViewById(R.id.recycleID);


        myAdapter = new MyAdapter(context, this::onFruitClick);
        recycle.setAdapter(myAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(context));

        swipeRefreshLayout= root.findViewById(R.id.refreshHome);
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

                                                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "AMS")
                                                                                    .setSmallIcon(R.drawable.all_fruits_and_vegetables_in_basket_background_transparent_ved4qx)
                                                                                    .setContentTitle("FarmLink Restock")
                                                                                    .setContentText(Variables.productNames.get(pos) + " has been restock!!")
                                                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                                                                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(context);
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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onFruitClick(int position) {
        if(Double.parseDouble(Variables.productQuantaty.get(position)) == 0.0 ){
            Intent activityIntent = new Intent(getContext(), ActivityAlert.class);
            activityIntent.putExtra("pos", position);
            startActivity(activityIntent);
        }else{
            Intent activityIntent = new Intent(getContext(), ActivityBuying.class);
            activityIntent.putExtra("pos", position);
            startActivity(activityIntent);
        }
    }
}