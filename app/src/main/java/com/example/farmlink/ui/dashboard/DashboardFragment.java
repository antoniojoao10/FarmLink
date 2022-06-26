package com.example.farmlink.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.farmlink.ActivityAlert;
import com.example.farmlink.ActivityBuying;
import com.example.farmlink.R;
import com.example.farmlink.Variables;
import com.example.farmlink.databinding.FragmentDashboardBinding;
import com.example.farmlink.ui.home.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DashboardFragment extends Fragment implements  MyAdapterD.OnFruitListener {

    private FragmentDashboardBinding binding;
    private RecyclerView recycle;
    public MyAdapterD myAdapter;
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
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        recycle = root.findViewById(R.id.recycleSearchID);

        Variables.imagesNamesFilter.clear();
        Variables.productNamesFilter.clear();
        Variables.productPricesFilter.clear();
        Variables.imagesNamesFilter.addAll(Variables.imagesNames);
        Variables.productNamesFilter.addAll(Variables.productNames);
        Variables.productPricesFilter.addAll(Variables.productPrices);
        myAdapter = new MyAdapterD(context, this::onFruitClick);
        recycle.setAdapter(myAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(context));

        swipeRefreshLayout= root.findViewById(R.id.refreshDashboard);
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
                                                            Variables.imagesNamesFilter.clear();
                                                            Variables.productNamesFilter.clear();
                                                            Variables.productPricesFilter.clear();
                                                            Variables.imagesNamesFilter.addAll(Variables.imagesNames);
                                                            Variables.productNamesFilter.addAll(Variables.productNames);
                                                            Variables.productPricesFilter.addAll(Variables.productPrices);

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


        SearchView search = root.findViewById(R.id.searchID);
        search.setQueryHint("Escrever Aqui");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Variables.imagesNamesFilter.clear();
                Variables.productNamesFilter.clear();
                Variables.productPricesFilter.clear();
                Variables.imagesNamesFilter.addAll(Variables.imagesNames);
                Variables.productNamesFilter.addAll(Variables.productNames);
                Variables.productPricesFilter.addAll(Variables.productPrices);
                if( s != "" || s != null ) {
                    for (int i = 0; i < Variables.productNames.size(); i++) {
                        String tmp = Variables.productNames.get(i);
                        if (findSimilarity(tmp, s) < 0.5) {
                            Variables.productNamesFilter.remove(Variables.productNames.get(i));
                            Variables.productPricesFilter.remove(Variables.productPrices.get(i));
                            Variables.imagesNamesFilter.remove(Variables.imagesNames.get(i));
                        }
                    }
                }
                recycle.setAdapter(myAdapter);
                recycle.setLayoutManager(new LinearLayoutManager(context));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Variables.imagesNamesFilter.clear();
                Variables.productNamesFilter.clear();
                Variables.productPricesFilter.clear();
                Variables.imagesNamesFilter.addAll(Variables.imagesNames);
                Variables.productNamesFilter.addAll(Variables.productNames);
                Variables.productPricesFilter.addAll(Variables.productPrices);
                if( s != "" || s != null) {
                    for (int i = 0; i < Variables.productNames.size(); i++) {
                        String tmp = Variables.productNames.get(i);
                        if (findSimilarity(tmp, s) < 0.5) {
                            Variables.productNamesFilter.remove(Variables.productNames.get(i));
                            Variables.productPricesFilter.remove(Variables.productPrices.get(i));
                            Variables.imagesNamesFilter.remove(Variables.imagesNames.get(i));
                        }
                    }
                }
                recycle.setAdapter(myAdapter);
                recycle.setLayoutManager(new LinearLayoutManager(context));
                System.out.println("Filter" + Variables.productNamesFilter);
                System.out.println(Variables.productNames);
                return false;
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public static double findSimilarity(String x, String y) {
        if (x == null || y == null) {
            throw new IllegalArgumentException("Strings must not be null");
        }

        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) {
            // optionally ignore case if needed
            return (maxLength - getLevenshteinDistance(x, y)) / maxLength;
        }
        return 1.0;
    }

    public static int getLevenshteinDistance(String X, String Y)
    {
        int m = X.length();
        int n = Y.length();

        int[][] T = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            T[i][0] = i;
        }
        for (int j = 1; j <= n; j++) {
            T[0][j] = j;
        }

        int cost;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                cost = X.charAt(i - 1) == Y.charAt(j - 1) ? 0: 1;
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1),
                        T[i - 1][j - 1] + cost);
            }
        }

        return T[m][n];
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