package com.example.farmlink;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class MyAdapterSold extends RecyclerView.Adapter<MyAdapterSold.MyViewHolder> implements Filterable {

    Context context;
    private OnFruitListener monFruitListener;

    public MyAdapterSold(Context ct, OnFruitListener onFruitListener){
        context = ct;
        monFruitListener = onFruitListener;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);

        return new MyViewHolder(view, monFruitListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        System.out.println(Variables.names);
        holder.myText1.setText(Variables.names.get(position));
        holder.myText3.setText(Variables.productSold.get(Variables.names.get(position))+"Kg");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference photoReference= storageReference.child("fruitsImages/" + Variables.imagesNames.get(Variables.productNames.indexOf(Variables.names.get(position))) + ".jpg");
        final long ONE_MEGABYTE = 1024 * 1024 * 5;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                holder.myImage.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context.getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return Variables.names.size();
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener{

        TextView myText1, myText2, myText3;
        ImageView myImage;

        OnFruitListener onFruitListener;

        public MyViewHolder(@NonNull View itemView, OnFruitListener onFruitListener) {
            super(itemView);
            myText1 = itemView.findViewById(R.id.titleHome);
            myText3 = itemView.findViewById(R.id.priceHome);
            myImage = itemView.findViewById(R.id.imageHome);
            this.onFruitListener = onFruitListener;

            itemView.setOnClickListener(this::onClick);
        }

        @Override
        public void onClick(View view) {
            onFruitListener.onFruitClick(getAdapterPosition());
        }
    }

    public interface OnFruitListener{
        void onFruitClick(int position);
    }
}
