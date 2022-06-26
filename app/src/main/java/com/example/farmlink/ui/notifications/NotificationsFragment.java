package com.example.farmlink.ui.notifications;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.farmlink.MainActivity;
import com.example.farmlink.R;
import com.example.farmlink.Variables;
import com.example.farmlink.databinding.FragmentNotificationsBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.transition.MaterialElevationScale;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private FirebaseFirestore db;

    ImageView imageView;
    Button button;
    TextView name;
    TextView price;
    TextView quant;
    Uri image;
    ActivityResultLauncher<String> mGetContent;
    ActivityResultLauncher<Intent> mGetPermission;
    ProgressDialog progressDialog;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotificationsViewModel notificationsViewModel =
                new ViewModelProvider(this).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        button = root.findViewById(R.id.sellAddImage);
        imageView = root.findViewById(R.id.sellImage);

        mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                imageView.setImageURI(result);
                image = result;
                System.out.println(image);

            }
        });

        mGetPermission = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if( result.getResultCode() == MainActivity.RESULT_OK)
                {
                    Toast.makeText(getContext(), "Permisson Given", Toast.LENGTH_SHORT).show();
                }
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile(view);
            }
        });

        name = root.findViewById(R.id.sellName);
        price = root.findViewById(R.id.sellPrice);
        quant = root.findViewById(R.id.sellQuantity);
        Button buttonOne = root.findViewById(R.id.sellAdd);
        buttonOne.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("Uploading...");
                progressDialog.show();
                db = FirebaseFirestore.getInstance();
                Map<String, Object> imageNData = new HashMap<>();
                for(int i = 0; i < Variables.imagesNames.size() ; i++){
                    imageNData.put(String.valueOf(i),Variables.imagesNames.get(i));
                }
                imageNData.put(String.valueOf(Variables.imagesNames.size()), String.valueOf(name.getText()));
                Variables.imagesNames.add(String.valueOf(name.getText()));
                Map<String, Object> nameData = new HashMap<>();
                for(int i = 0; i < Variables.productNames.size() ; i++){
                    nameData.put(String.valueOf(i),Variables.productNames.get(i));
                }
                nameData.put(String.valueOf(Variables.productNames.size()), String.valueOf(name.getText()));
                Variables.productNames.add(String.valueOf(name.getText()));
                Map<String, Object> priceData = new HashMap<>();
                for(int i = 0; i < Variables.productPrices.size() ; i++){
                    priceData.put(String.valueOf(i),Variables.productPrices.get(i));
                }
                priceData.put(String.valueOf(Variables.productPrices.size()), String.valueOf(price.getText()));
                Variables.productPrices.add(String.valueOf(price.getText()));
                Map<String, Object> quanData = new HashMap<>();
                for(int i = 0; i < Variables.productQuantaty.size() ; i++){
                    quanData.put(String.valueOf(i),Variables.productQuantaty.get(i));
                }
                quanData.put(String.valueOf(Variables.productQuantaty.size()), String.valueOf(quant.getText()));
                Variables.productQuantaty.add(String.valueOf(quant.getText()));
                Map<String, Object> sellerData = new HashMap<>();
                for(int i = 0; i < Variables.productSeller.size() ; i++){
                    sellerData.put(String.valueOf(i),Variables.productSeller.get(i));
                }
                sellerData.put(String.valueOf(Variables.productSeller.size()), String.valueOf(Variables.mAuth.getUid()));
                Variables.productSeller.add(String.valueOf(Variables.mAuth.getUid()));
                db.collection("productSeller").document("m12PQNSDu7RAcBBXfeIp")
                        .set(sellerData);

                db.collection("imagesNames").document("rWKqqK0qXkZfvRXYsd13")
                        .set(imageNData)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("productsNames").document("5dDdgVtYIZHlp2jQNwLJ")
                                        .set(nameData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                db.collection("productsPrices").document("gQbdwDvxAkGqZYCra3oV")
                                                        .set(priceData)
                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void aVoid) {
                                                                db.collection("productsQuantity").document("1iYoNxyjhr6De9tlCVKI")
                                                                        .set(quanData)
                                                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                            @Override
                                                                            public void onSuccess(Void aVoid) {
                                                                                StorageReference storageReference = FirebaseStorage.getInstance().getReference("fruitsImages/"+String.valueOf(name.getText()) + ".jpg");

                                                                                storageReference.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                                                    @Override
                                                                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                                                        imageView.setImageURI(null);
                                                                                        if(progressDialog.isShowing()) progressDialog.dismiss();
                                                                                        Toast.makeText(getContext(), "Product uploaded", Toast.LENGTH_SHORT).show();
                                                                                        Intent activityIntent = new Intent(getContext(), MainActivity.class);
                                                                                        startActivity(activityIntent);
                                                                                    }
                                                                                }).addOnFailureListener(new OnFailureListener() {
                                                                                    @Override
                                                                                    public void onFailure(@NonNull Exception e) {
                                                                                        if(progressDialog.isShowing()) progressDialog.dismiss();
                                                                                        Toast.makeText(getContext(), "Image upload failed", Toast.LENGTH_SHORT).show();
                                                                                    }
                                                                                });
                                                                            }
                                                                        })
                                                                        .addOnFailureListener(new OnFailureListener() {
                                                                            @Override
                                                                            public void onFailure(@NonNull Exception e) {
                                                                                Toast.makeText(getContext(), "Failed to publish", Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        });
                                                            }
                                                        })
                                                        .addOnFailureListener(new OnFailureListener() {
                                                            @Override
                                                            public void onFailure(@NonNull Exception e) {
                                                                Toast.makeText(getContext(), "Failed to publish", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to publish", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), "Failed to publish", Toast.LENGTH_SHORT).show();
                            }
                        });

            }
        });

        return root;
    }

    private void selectFile(View view) {
        takePermission();
        mGetContent.launch("image/*");
    }

    private void takePermissions() {
        if(Build.VERSION.SDK_INT== Build.VERSION_CODES.R){
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getContext().getPackageName())));
                mGetPermission.launch(intent);
            } catch ( Exception e){
                e.printStackTrace();
            }
        }
        else{
            ActivityCompat.requestPermissions(getActivity() ,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
        }
    }

    private boolean isPermissionGranted(){
        if(Build.VERSION.SDK_INT==Build.VERSION_CODES.R){
            return Environment.isExternalStorageManager();
        }
        else{
            int readExternalStoragePermission = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
            return readExternalStoragePermission== PackageManager.PERMISSION_GRANTED;
        }
    }

    public void takePermission(){
        if(isPermissionGranted()){
            Toast.makeText(getContext(), "Permission Already", Toast.LENGTH_SHORT).show();
        }
        else {
            takePermissions();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            if(requestCode ==101){
                boolean readExternalStorage = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                if( readExternalStorage ) {
                    Toast.makeText(getContext(), "Permission Allowed In Android", Toast.LENGTH_SHORT);
                }else{
                    takePermission();
                }
            }

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}